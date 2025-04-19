package com.ssk.dashboard.presentation

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.ssk.dashboard.domain.CsvExporter
import com.ssk.dashboard.presentation.all_transactions.AllTransactionsAction
import com.ssk.dashboard.presentation.all_transactions.AllTransactionsUiState
import com.ssk.dashboard.presentation.create_transaction.CreateTransactionAction
import com.ssk.dashboard.presentation.create_transaction.CreateTransactionEvent
import com.ssk.dashboard.presentation.create_transaction.CreateTransactionState
import com.ssk.dashboard.presentation.create_transaction.components.TransactionTypeOptions
import com.ssk.dashboard.presentation.dashboard.DashboardAction
import com.ssk.dashboard.presentation.dashboard.DashboardEvent
import com.ssk.dashboard.presentation.dashboard.DashboardState
import com.ssk.dashboard.presentation.dashboard.DashboardState.AmountSettings
import com.ssk.dashboard.presentation.dashboard.utils.AmountFormatter
import com.ssk.dashboard.presentation.export.ExportRange
import com.ssk.dashboard.presentation.export.ExportUiAction
import com.ssk.dashboard.presentation.export.ExportUiState
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
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TransactionSharedViewModel(
    private val userRepository: IUserRepository,
    private val transactionRepository: ITransactionsRepository,
    private val sessionRepository: ISessionRepository,
    private val csvExporter: CsvExporter
) : ViewModel() {

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState = _dashboardState.asStateFlow()

    private val _createTransactionState = MutableStateFlow(CreateTransactionState())
    val createTransactionState = _createTransactionState.asStateFlow()

    private val _exportState = MutableStateFlow(ExportUiState())
    val exportState = _exportState.asStateFlow()

    var allTransactionsUiState by mutableStateOf(AllTransactionsUiState())
        private set

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
            is DashboardAction.NavigateToCreateTransaction -> {
                _dashboardState.update {
                    it.copy(showCreateTransactionSheet = true)
                }
            }

            DashboardAction.NavigateToPinPrompt -> TODO()
            DashboardAction.NavigateToSettings -> navigateToSettings()
            DashboardAction.OnShowAllTransactionsClicked -> {
                _dashboardEvent.trySend(DashboardEvent.NavigateToAllTransactions)
            }

            is DashboardAction.UpdateExportBottomSheet -> {
                _exportState.update { currentState ->
                    currentState.copy(
                        isExportSheetOpen = action.showSheet
                    )
                }
            }

            DashboardAction.NavigateToExport -> {
                _exportState.update {
                    it.copy(isExportSheetOpen = true)
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

    fun onAction(exportUiAction: ExportUiAction) {
        when (exportUiAction) {
            ExportUiAction.OnExportClicked -> exportCsvData()
            is ExportUiAction.OnExportRangeClicked -> {
                _exportState.update {
                    it.copy(
                        exportRange = exportUiAction.exportRange
                    )
                }
            }

            ExportUiAction.OnExportSheetToggled -> {
                _exportState.update {
                    it.copy(isExportSheetOpen = !it.isExportSheetOpen)
                }
            }
        }
    }

    fun onAction(allTransactionsAction: AllTransactionsAction) {
        when (allTransactionsAction) {
            AllTransactionsAction.OnExportClicked -> {
                _exportState.update {
                    it.copy(isExportSheetOpen = !it.isExportSheetOpen)
                }
            }
        }
    }

    private fun navigateToSettings() {
        _dashboardEvent.trySend(DashboardEvent.NavigateToSettings)
    }

    private fun exportCsvData() {
        val transactionInRange = filterTransactionsByRange(
            transactions = allTransactionsUiState.transactions.values.flatten(),
            exportRange = _exportState.value.exportRange
        )
        csvExporter.exportToCsv(fileName = generateCsvName(), transactions = transactionInRange)
        _exportState.update {
            it.copy(
                isExportSheetOpen = false,
            )
        }
    }

    private fun generateCsvName(): String {
        val currentDateTime = LocalDateTime.now()
        val formattedDate = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))
        val title = when (_exportState.value.exportRange) {
            ExportRange.THREE_MONTH -> "transactions_last_3_months_$formattedDate"
            ExportRange.LAST_MONTH -> "transactions_last_month_$formattedDate"
            ExportRange.CURRENT_MONTH -> "transactions_current_month_$formattedDate"
            ExportRange.ALL -> "all_transactions_$formattedDate"
        }
        return "$title.csv"
    }

    private fun filterTransactionsByRange(
        transactions: List<Transaction>,
        exportRange: ExportRange
    ): List<Transaction> {
        val zoneId = ZoneId.systemDefault()
        val now = LocalDate.now(zoneId)

        val startDate: Instant

        when (exportRange) {
            ExportRange.THREE_MONTH -> {
                val lastThreeMonths = now.minusMonths(3)
                startDate = lastThreeMonths.withDayOfMonth(1).atStartOfDay(zoneId).toInstant()
            }

            ExportRange.LAST_MONTH -> {
                val lastMonth = now.minusMonths(1)
                startDate = lastMonth.withDayOfMonth(1).atStartOfDay(zoneId).toInstant()
            }

            ExportRange.CURRENT_MONTH -> {
                startDate = now.withDayOfMonth(1).atStartOfDay(zoneId).toInstant()
            }

            ExportRange.ALL -> {
                startDate = Instant.MIN
            }
        }

        return transactions.filter { transaction ->
            val transactionInstant = Instant.ofEpochMilli(transaction.transactionDate)
            transactionInstant in startDate..Instant.now()
        }.reversed()
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

            updateDashboardData(transaction)
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

    private fun updateDashboardData(transaction: Transaction) {
        val currentState = _dashboardState.value

        // Update Transactions
        val updatedTransactions = currentState.latestTransactions.toMutableMap()
        val transactionDate =
            InstantFormatter.convertInstantToLocalDate(transaction.transactionDate)
        val transactionsForDate =
            updatedTransactions[transactionDate]?.toMutableList() ?: mutableListOf()

        val updatedTransactionsForDate = listOf(transaction) + transactionsForDate
        updatedTransactions[transactionDate] = updatedTransactionsForDate

        // Update Account Balance
        val allTransactions = currentState.latestTransactions
            .flatMap { it.value }
            .toMutableList()
            .also { it.add(transaction) }
        val updatedBalance =
            getAndFormatAccountBalance(allTransactions, currentState.amountSettings)

        _dashboardState.update {
            it.copy(
                accountInfoState = it.accountInfoState.copy(accountBalance = updatedBalance),
                latestTransactions = updatedTransactions,
                isDataLoaded = true
            )
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

                            allTransactionsUiState = allTransactionsUiState.copy(
                                transactions = transactions
                                    .reversed()
                                    .groupBy { InstantFormatter.convertInstantToLocalDate(it.transactionDate) },
                                amountSettings = _dashboardState.value.amountSettings
                            )
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