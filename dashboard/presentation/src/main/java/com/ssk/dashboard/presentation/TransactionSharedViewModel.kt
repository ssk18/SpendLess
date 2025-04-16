package com.ssk.dashboard.presentation

import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.core.domain.exception.UserNotLoggedInException
import com.ssk.core.domain.model.Transaction
import com.ssk.core.domain.model.TransactionType
import com.ssk.core.domain.model.User
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.domain.repository.ITransactionsRepository
import com.ssk.core.domain.repository.IUserRepository
import com.ssk.core.domain.utils.InstantFormatter
import com.ssk.core.domain.utils.Result
import com.ssk.core.presentation.designsystem.model.RecurringTypeUI
import com.ssk.core.presentation.designsystem.model.TransactionCategoryTypeUI
import com.ssk.core.presentation.ui.UiText
import com.ssk.core.presentation.ui.components.ExpensesFormatUi
import com.ssk.core.presentation.ui.components.toDomain
import com.ssk.core.presentation.ui.components.toUi
import com.ssk.core.presentation.ui.textAsFlow
import com.ssk.dashboard.presentation.create_transaction.CreateTransactionAction
import com.ssk.dashboard.presentation.create_transaction.CreateTransactionEvent
import com.ssk.dashboard.presentation.create_transaction.CreateTransactionState
import com.ssk.dashboard.presentation.create_transaction.components.TransactionTypeOptions
import com.ssk.dashboard.presentation.dashboard.DashboardAction
import com.ssk.dashboard.presentation.dashboard.DashboardEvent
import com.ssk.dashboard.presentation.dashboard.DashboardState
import com.ssk.dashboard.presentation.dashboard.DashboardState.AmountSettings
import com.ssk.dashboard.presentation.dashboard.utils.AmountFormatter
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionSharedViewModel(
    private val userRepository: IUserRepository,
    private val transactionRepository: ITransactionsRepository,
    private val sessionRepository: ISessionRepository
) : ViewModel() {

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState = _dashboardState.asStateFlow()

    private val _createTransactionState = MutableStateFlow(CreateTransactionState())
    val createTransactionState = _createTransactionState.asStateFlow()

    private val _transactionsEvent = Channel<CreateTransactionEvent>()
    val transactionsEvent = _transactionsEvent.receiveAsFlow()

    private val _dashboardEvent = Channel<DashboardEvent>()
    val dashboardEvent = _dashboardEvent.receiveAsFlow()

    private var userId: Long? = null

    init {
        getDashBoardData()
        observeTransactionFields()
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.NavigateToAllTransactions -> TODO()
            is DashboardAction.NavigateToCreateTransaction -> {
                _dashboardState.update {
                    it.copy(showCreateTransactionSheet = true)
                }
            }

            DashboardAction.NavigateToPinPrompt -> TODO()
            DashboardAction.NavigateToSettings -> navigateToSettings()
            DashboardAction.OnShowAllTransactionsClicked -> TODO()
            is DashboardAction.UpdateExportBottomSheet -> {
                _dashboardState.update { currentState ->
                    currentState.copy(
                        showCreateTransactionSheet = action.showSheet
                    )
                }
            }
        }
    }

    fun onAction(action: CreateTransactionAction) {
        when (action) {
            CreateTransactionAction.OnBottomSheetCloseClicked -> {
                _dashboardState.update {
                    it.copy(showCreateTransactionSheet = false)
                }
            }

            CreateTransactionAction.OnCreateTransactionClicked -> createTransaction()

            is CreateTransactionAction.OnExpenseCategorySelected -> {
                _createTransactionState.update {
                    it.copy(
                        expenseCategory = action.category
                    )
                }
            }

            is CreateTransactionAction.OnRepeatingCategorySelected -> {
                _createTransactionState.update {
                    it.copy(
                        repeatingCategory = action.repeatingCategory
                    )
                }
            }

            is CreateTransactionAction.OnTransactionTypeSelected -> {
                _createTransactionState.update {
                    it.copy(
                        transactionType = action.transactionType
                    )
                }
            }
        }
    }

    private fun navigateToSettings() {
        _dashboardEvent.trySend(DashboardEvent.NavigateToSettings)
    }

    private fun getDashBoardData() {
        viewModelScope.launch {
            val username = getUsername()
            _dashboardState.update { currentState ->
                currentState.copy(
                    username = username
                )
            }

            userRepository.getUserAsFlow(username)
                .collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            userId = result.data.userId
                            sessionRepository.startSession(result.data.settings.sessionExpiryDuration)
                            setAmountSettings(result.data)
                            setTransactionAnalytics()
                        }

                        is Result.Error -> {
                            _dashboardEvent.send(DashboardEvent.ShowSnackbar(UiText.DynamicString("Error fetching user details")))
                        }
                    }
                }
        }
    }

    private fun getUsername(): String {
        return sessionRepository.getLoggedInUsername() ?: throw UserNotLoggedInException()
    }

    private fun setAmountSettings(user: User) {
        _dashboardState.update { currentState ->
            currentState.copy(
                amountSettings = AmountSettings(
                    expensesFormat = user.settings.expensesFormat.toUi(),
                    currency = user.settings.currency,
                    decimalSeparator = user.settings.decimalSeparator.toUi(),
                    thousandsSeparator = user.settings.thousandsSeparator.toUi()
                )
            )
        }
    }

    private fun createTransaction() {
        userId?.let { userId ->
            val transactionFields = _createTransactionState.value.transactionFieldsState
            val transactionTitle = transactionFields.title.text.toString().trim()
            val transactionAmount = AmountFormatter.parseAmountToFloat(
                amount = transactionFields.amount.text,
                amountSettings = _dashboardState.value.amountSettings,
                isExpense = _createTransactionState.value.isExpense
            )

            val note = transactionFields.note.text.toString().trim()
            val expenseCategory =
                if (_createTransactionState.value.isExpense) {
                    _createTransactionState.value.expenseCategory.toDomain()
                } else {
                    TransactionType.INCOME
                }
            val repeatingCategory = _createTransactionState.value.repeatingCategory.toDomain()

            val transaction = Transaction(
                userId = userId,
                title = transactionTitle,
                amount = transactionAmount,
                note = note,
                transactionType = expenseCategory,
                repeatType = repeatingCategory
            )

            viewModelScope.launch {
                transactionRepository.saveTransaction(transaction)
                toggleCreateTransactionSheet()
                resetCreateTransactionState()
            }
        }
    }

    private fun resetCreateTransactionState() {
        val transactionFieldsState = _createTransactionState.value.transactionFieldsState
        transactionFieldsState.title.clearText()
        transactionFieldsState.amount.clearText()
        transactionFieldsState.note?.clearText()

        _createTransactionState.update {
            it.copy(
                transactionFieldsState = transactionFieldsState,
                transactionType = TransactionTypeOptions.EXPENSE,
                expenseCategory = TransactionCategoryTypeUI.OTHER,
                repeatingCategory = RecurringTypeUI.ONE_TIME,
            )
        }
    }

    private fun handleTransactionTitleInput(enteredText: CharSequence) {
        val originalTitleState = _createTransactionState.value.transactionFieldsState.title
        val filteredText =
            enteredText.filter { it.isLetterOrDigit() || it.isWhitespace() }
                .take(MAX_TITLE_LENGTH)


        if (filteredText != originalTitleState) {
            originalTitleState.edit {
                replace(0, originalTitleState.text.length, filteredText)
            }
        }
    }

    private fun handleTransactionAmountInput(enteredText: CharSequence) {
        val originalAmountState = _createTransactionState.value.transactionFieldsState.amount
        val formattedAmount = AmountFormatter.getFormatedAmount(
            amount = enteredText,
            amountSettings = _dashboardState.value.amountSettings,
        )

        originalAmountState.edit {
            replace(0, originalAmountState.text.length, formattedAmount)
        }
    }

    private fun handleTransactionNoteInput(enteredText: CharSequence) {
        val originalNoteState = _createTransactionState.value.transactionFieldsState.note
        val filteredText = enteredText.take(100)
        originalNoteState.edit {
            replace(0, originalNoteState.text.length, filteredText)
        }
    }

    private fun observeTransactionFields() {
        _createTransactionState.value.transactionFieldsState.title.textAsFlow()
            .onEach(::handleTransactionTitleInput)
            .launchIn(viewModelScope)

        _createTransactionState.value.transactionFieldsState.amount.textAsFlow()
            .onEach(::handleTransactionAmountInput)
            .launchIn(viewModelScope)

        _createTransactionState.value.transactionFieldsState.note.textAsFlow()
            .onEach(::handleTransactionNoteInput)
            .launchIn(viewModelScope)
    }

    private fun toggleCreateTransactionSheet() {
        _dashboardState.update {
            it.copy(showCreateTransactionSheet = !it.showCreateTransactionSheet)
        }
    }

    private suspend fun setTransactionAnalytics() {
        userId?.let { userId ->
            transactionRepository.getTransactionByUser(userId).collectLatest { result ->
                when (result) {
                    is Result.Error -> TODO()
                    is Result.Success -> {
                        val transactions = result.data
                        if (transactions.isNotEmpty()) {
                            val latestTransactions = getLatestTransaction(transactions)
                                .groupBy { InstantFormatter.convertInstantToLocalDate(it.transactionDate) }
                            val accountInfoState = getAccountInfoState(
                                transactions = transactions,
                                amountSettings = _dashboardState.value.amountSettings,
                                userId = userId
                            )
                            _dashboardState.update { currentState ->
                                currentState.copy(
                                    accountInfoState = accountInfoState,
                                    latestTransactions = latestTransactions,
                                    showCreateTransactionSheet = false,
                                    isDataLoaded = true
                                )
                            }
                        } else {
                            val initAmount =
                                "${_dashboardState.value.amountSettings.currency.symbol}0"

                            _dashboardState.update {
                                it.copy(
                                    isDataLoaded = true,
                                    accountInfoState = DashboardState.AccountInfoState(
                                        accountBalance = initAmount,
                                        previousWeekExpenseAmount = initAmount
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getLatestTransaction(transactions: List<Transaction>): List<Transaction> {
        return transactions
            .sortedByDescending { transaction ->
                transaction.transactionDate
            }
            .take(20)
    }

    private fun getAndFormatAccountBalance(
        transactions: List<Transaction>,
        amountSettings: AmountSettings
    ): String {
        val balance = transactions.fold(0f) { acc, transaction ->
            acc + transaction.amount
        }

        val formattedBalance = AmountFormatter.formatUserInput(
            amount = balance,
            amountSettings = _dashboardState.value.amountSettings,
            enableTwoDecimal = true
        )

        val currency = amountSettings.currency.symbol
        return if (balance < 0) {
            when (amountSettings.expensesFormat) {
                ExpensesFormatUi.MINUS -> "-$currency$formattedBalance"
                ExpensesFormatUi.BRACKETS -> "($currency$formattedBalance)"
            }
        } else {
            "$currency$formattedBalance"
        }
    }

    private suspend fun getLargestTransactionAmount(
        userId: Long,
        amountSettings: AmountSettings
    ): Triple<String, String, String> {
        val result = transactionRepository.getLargestTransaction(userId)
        
        return when (result) {
            is Result.Error -> {
                _dashboardEvent.trySend(DashboardEvent.ShowSnackbar(UiText.DynamicString("Error fetching largest transaction")))
                Triple("", "0.00", "")
            }

            is Result.Success -> {
                val transaction = result.data

                if (transaction == null) {
                    Triple("", "0.00", "")
                } else {
                    val formattedAmount = AmountFormatter.formatUserInput(
                        amount = kotlin.math.abs(transaction.amount),
                        amountSettings = amountSettings,
                        enableTwoDecimal = true
                    )
                    val currency = amountSettings.currency.symbol
                    val formattedLargestAmount = if (transaction.amount < 0) {
                        when (amountSettings.expensesFormat) {
                            ExpensesFormatUi.MINUS -> "-$currency$formattedAmount"
                            ExpensesFormatUi.BRACKETS -> "($currency$formattedAmount)"
                        }
                    } else {
                        "$currency$formattedAmount"
                    }
                    
                    val title = transaction.title.take(MAX_TITLE_LENGTH)
                    val date = InstantFormatter.formatDateString(transaction.transactionDate)

                    Triple(title, formattedLargestAmount, date)
                }
            }
        }
    }

    private suspend fun getMostPopularCategory(userId: Long): TransactionCategoryTypeUI? {
        val result = transactionRepository.getMostPopularCategory(userId)
        return when (result) {
            is Result.Error -> {
                _dashboardEvent.trySend(DashboardEvent.ShowSnackbar(UiText.DynamicString("Error fetching most popular category")))
                null
            }
            is Result.Success -> {
                result.data?.toUi()
            }
        }
    }

    private suspend fun getPreviousWeekExpenseAmount(userId: Long): String {
        val result = transactionRepository.getPreviousWeekTransactions(userId)
        
        return when (result) {
            is Result.Error -> {
                _dashboardEvent.trySend(DashboardEvent.ShowSnackbar(UiText.DynamicString("Error fetching previous week expense amount")))
                "$0.00"
            }

            is Result.Success -> {
                val transactions = result.data
                val previousWeekExpenseAmount = transactions.fold(0f) { acc, transaction ->
                    acc + transaction.amount
                }
                AmountFormatter.formatUserInput(
                    amount = previousWeekExpenseAmount,
                    amountSettings = _dashboardState.value.amountSettings,
                    enableTwoDecimal = true
                )
            }
        }
    }

    private suspend fun getAccountInfoState(
        transactions: List<Transaction>,
        amountSettings: AmountSettings,
        userId: Long
    ): DashboardState.AccountInfoState = coroutineScope {
        val balance = async { getAndFormatAccountBalance(transactions, amountSettings) }

        val largestTransactionInfo = async { getLargestTransactionAmount(userId, amountSettings) }

        val popularCategory = async { getMostPopularCategory(userId) }
        val previousWeekExpenseAmount = async { getPreviousWeekExpenseAmount(userId) }

        val (title, amount, date) = largestTransactionInfo.await()

        DashboardState.AccountInfoState(
            accountBalance = balance.await(),
            popularCategory = popularCategory.await(),
            largestTransaction = DashboardState.LargestTransaction(
                name = title,
                amount = amount,
                date = date
            ),
            previousWeekExpenseAmount = previousWeekExpenseAmount.await()
        )
    }

    companion object {
        private const val MAX_TITLE_LENGTH = 14
    }

}