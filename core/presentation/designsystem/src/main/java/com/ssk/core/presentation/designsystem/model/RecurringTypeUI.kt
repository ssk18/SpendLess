package com.ssk.core.presentation.designsystem.model

import com.ssk.core.domain.utils.InstantFormatter

enum class RecurringTypeUI(val symbol: String, val title: String) {
    ONE_TIME("\uD83D\uDD04", "Does not repeat"),
    DAILY("\uD83D\uDD04", "Daily"),
    WEEKLY("\uD83D\uDD04", "Weekly on ${InstantFormatter.getCurrentDayOfWeek()}"),
    MONTHLY("\uD83D\uDD04", "Monthly on the ${InstantFormatter.getCurrentDayOfMonth()}th"),
    YEARLY("\uD83D\uDD04", "Yearly on ${InstantFormatter.getCurrentMonthAndDay()}th")
}