package com.ssk.core.presentation.ui

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object InstantFormatter {
    private val zoneId: ZoneId = ZoneId.systemDefault()
    private val englishLocal: Locale = Locale.forLanguageTag("en-US")

    fun formatToRelativeDay(instant: Instant): UiText {
        val today = LocalDate.now(zoneId)
        val yesterday = today.minusDays(1)

        return when (val date = instant.atZone(zoneId).toLocalDate()) {
            today -> UiText.StringResource(R.string.today)
            yesterday -> UiText.StringResource(R.string.yesterday)
            else -> UiText.DynamicString(
                date.format(
                    DateTimeFormatter.ofPattern(
                        "MMM d, EEEE",
                        englishLocal
                    )
                )
            )
        }
    }
}