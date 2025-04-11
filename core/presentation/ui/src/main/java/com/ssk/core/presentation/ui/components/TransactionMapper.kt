package com.ssk.core.presentation.ui.components

import com.ssk.core.domain.model.RepeatType
import com.ssk.core.domain.model.TransactionType
import com.ssk.core.presentation.designsystem.model.RecurringTypeUI
import com.ssk.core.presentation.designsystem.model.TransactionCategoryTypeUI

fun TransactionType.toUi(): TransactionCategoryTypeUI {
    return when (this) {
        TransactionType.HOME -> TransactionCategoryTypeUI.HOME
        TransactionType.FOOD -> TransactionCategoryTypeUI.FOOD
        TransactionType.ENTERTAINMENT -> TransactionCategoryTypeUI.ENTERTAINMENT
        TransactionType.CLOTHING -> TransactionCategoryTypeUI.CLOTHING
        TransactionType.HEALTH -> TransactionCategoryTypeUI.HEALTH
        TransactionType.PERSONAL_CARE -> TransactionCategoryTypeUI.PERSONAL_CARE
        TransactionType.TRANSPORTATION -> TransactionCategoryTypeUI.TRANSPORTATION
        TransactionType.EDUCATION -> TransactionCategoryTypeUI.EDUCATION
        TransactionType.SAVINGS -> TransactionCategoryTypeUI.SAVINGS
        TransactionType.OTHER -> TransactionCategoryTypeUI.OTHER
        TransactionType.INCOME -> TransactionCategoryTypeUI.INCOME
    }
}

fun TransactionCategoryTypeUI.toDomain(): TransactionType {
    return when (this) {
        TransactionCategoryTypeUI.HOME -> TransactionType.HOME
        TransactionCategoryTypeUI.FOOD -> TransactionType.FOOD
        TransactionCategoryTypeUI.ENTERTAINMENT -> TransactionType.ENTERTAINMENT
        TransactionCategoryTypeUI.CLOTHING -> TransactionType.CLOTHING
        TransactionCategoryTypeUI.HEALTH -> TransactionType.HEALTH
        TransactionCategoryTypeUI.PERSONAL_CARE -> TransactionType.PERSONAL_CARE
        TransactionCategoryTypeUI.TRANSPORTATION -> TransactionType.TRANSPORTATION
        TransactionCategoryTypeUI.EDUCATION -> TransactionType.EDUCATION
        TransactionCategoryTypeUI.SAVINGS -> TransactionType.SAVINGS
        TransactionCategoryTypeUI.OTHER -> TransactionType.OTHER
        TransactionCategoryTypeUI.INCOME -> TransactionType.INCOME
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