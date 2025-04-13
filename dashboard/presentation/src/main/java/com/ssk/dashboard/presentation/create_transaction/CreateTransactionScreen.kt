package com.ssk.dashboard.presentation.create_transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.domain.model.ExpensesFormat
import com.ssk.core.presentation.designsystem.components.SpendLessActionButton
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.dashboard.presentation.create_transaction.CreateTransactionState.TransactionFieldsState
import com.ssk.dashboard.presentation.create_transaction.components.AmountTextField
import com.ssk.dashboard.presentation.create_transaction.components.CreateTransactionHeader
import com.ssk.dashboard.presentation.create_transaction.components.NoteTextField
import com.ssk.dashboard.presentation.create_transaction.components.ReceiverTextField
import com.ssk.dashboard.presentation.create_transaction.components.TransactionDropDowns
import com.ssk.dashboard.presentation.create_transaction.components.TransactionTypeSelector

@Composable
fun CreateTransactionScreenRoot() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTransactionScreen(
    modifier: Modifier = Modifier,
    state: CreateTransactionState,
    onAction: (CreateTransactionAction) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val sheetHeight = screenHeight * 0.07f

    ModalBottomSheet(
        onDismissRequest = {
            onAction(CreateTransactionAction.OnCreateTransactionSheetToggled)
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier.padding(top = sheetHeight),
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            CreateTransactionHeader(
                onCloseClick = {
                    onAction(CreateTransactionAction.OnCreateTransactionSheetToggled)
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            TransactionTypeSelector(
                selectedOption = state.transactionType,
                onOptionSelected = {
                    onAction(CreateTransactionAction.OnTransactionTypeSelected(it))
                }
            )

            Spacer(modifier = Modifier.height(34.dp))

            TransactionFields(
                transactionFieldsState = state.transactionFieldsState,
                isExpense = state.isExpense,
                expensesFormat = state.expensesFormat
            )

            Spacer(modifier = Modifier.height(32.dp))

            TransactionDropDowns(
                isExpense = state.isExpense,
                selectedExpenseCategory = state.expenseCategory,
                selectedRepeatType = state.repeatingCategory,
                onExpenseCategorySelected = {
                    onAction(CreateTransactionAction.OnExpenseCategorySelected(it))
                },
                onRepeatTypeSelected = {
                    onAction(CreateTransactionAction.OnRepeatingCategorySelected(it))
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SpendLessActionButton(
                text = "Create",
                onClick = {
                    onAction(CreateTransactionAction.OnCreateTransactionClicked)
                },
                enabled = state.isCreateButtonEnabled
            )
        }
    }
}

@Composable
fun TransactionFields(
    modifier: Modifier = Modifier,
    transactionFieldsState: TransactionFieldsState,
    isExpense: Boolean,
    expensesFormat: ExpensesFormat
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val focusManager = LocalFocusManager.current
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        ReceiverTextField(
            state = transactionFieldsState.title,
            isExpense = isExpense,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words
            ),
            onKeyboardAction = {
                focusManager.moveFocus(FocusDirection.Down)
            },
            modifier = Modifier.focusRequester(focusRequester)
        )
        AmountTextField(
            state = transactionFieldsState.amount,
            expensesFormat = expensesFormat,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            onKeyboardAction = {
                focusManager.moveFocus(FocusDirection.Down)
            },
            isExpense = isExpense
        )
        NoteTextField(
            state = transactionFieldsState.note,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            onKeyboardAction = {
                focusManager.clearFocus()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTransactionScreenPreview() {
    SpendLessAppTheme {
        CreateTransactionScreen(
            state = CreateTransactionState(),
            onAction = {}
        )
    }
}