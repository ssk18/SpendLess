package com.ssk.core.presentation.ui.components

import com.ssk.core.domain.model.Expense
import com.ssk.core.domain.model.RepeatType
import com.ssk.core.presentation.designsystem.model.RecurringTypeUI
import com.ssk.core.presentation.designsystem.model.TransactionCategoryTypeUI

fun Expense.toUi(): TransactionCategoryTypeUI {
    return when (this) {
        Expense.HOME -> TransactionCategoryTypeUI.HOME
        Expense.FOOD -> TransactionCategoryTypeUI.FOOD
        Expense.ENTERTAINMENT -> TransactionCategoryTypeUI.ENTERTAINMENT
        Expense.CLOTHING -> TransactionCategoryTypeUI.CLOTHING
        Expense.HEALTH -> TransactionCategoryTypeUI.HEALTH
        Expense.PERSONAL_CARE -> TransactionCategoryTypeUI.PERSONAL_CARE
        Expense.TRANSPORTATION -> TransactionCategoryTypeUI.TRANSPORTATION
        Expense.EDUCATION -> TransactionCategoryTypeUI.EDUCATION
        Expense.SAVING -> TransactionCategoryTypeUI.SAVINGS
        Expense.OTHER -> TransactionCategoryTypeUI.OTHER
    }
}

fun TransactionCategoryTypeUI.toDomain(): Expense {
    return when (this) {
        TransactionCategoryTypeUI.HOME -> Expense.HOME
        TransactionCategoryTypeUI.FOOD -> Expense.FOOD
        TransactionCategoryTypeUI.ENTERTAINMENT -> Expense.ENTERTAINMENT
        TransactionCategoryTypeUI.CLOTHING -> Expense.CLOTHING
        TransactionCategoryTypeUI.HEALTH -> Expense.HEALTH
        TransactionCategoryTypeUI.PERSONAL_CARE -> Expense.PERSONAL_CARE
        TransactionCategoryTypeUI.TRANSPORTATION -> Expense.TRANSPORTATION
        TransactionCategoryTypeUI.EDUCATION -> Expense.EDUCATION
        TransactionCategoryTypeUI.SAVINGS -> Expense.SAVING
        TransactionCategoryTypeUI.OTHER -> Expense.OTHER
        TransactionCategoryTypeUI.INCOME -> TODO()
    }
}

fun RepeatType.toUi(): RecurringTypeUI {
    return when (this) {
        RepeatType.NOT_REPEAT -> RecurringTypeUI.ONE_TIME
        RepeatType.DAILY -> RecurringTypeUI.DAILY
        RepeatType.WEEKLY -> RecurringTypeUI.WEEKLY
        RepeatType.MONTHLY -> RecurringTypeUI.MONTHLY
        RepeatType.YEARLY -> RecurringTypeUI.YEARLY
    }
}

fun RecurringTypeUI.toDomain(): RepeatType {
    return when (this) {
        RecurringTypeUI.ONE_TIME -> RepeatType.NOT_REPEAT
        RecurringTypeUI.DAILY -> RepeatType.DAILY
        RecurringTypeUI.WEEKLY -> RepeatType.WEEKLY
        RecurringTypeUI.MONTHLY -> RepeatType.MONTHLY
        RecurringTypeUI.YEARLY -> RepeatType.YEARLY
    }
}