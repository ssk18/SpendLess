package com.ssk.dashboard.presentation.create_transaction

sealed interface CreateTransactionEvent {
    data object CloseBottomSheet : CreateTransactionEvent
    data object NavigateToDashboard : CreateTransactionEvent
}