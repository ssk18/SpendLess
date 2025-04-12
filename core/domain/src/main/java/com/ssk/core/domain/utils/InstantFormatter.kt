package com.ssk.core.domain.utils

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

object InstantFormatter {
    private val zoneId: ZoneId = ZoneId.systemDefault()
    private val englishLocal: Locale = Locale.forLanguageTag("en-US")

    fun formatToRelativeDay(instant: Instant): String {
        val today = LocalDate.now(zoneId)
        val yesterday = today.minusDays(1)

        return when (val date = instant.atZone(zoneId).toLocalDate()) {
            today -> "Today"
            yesterday -> "Yesterday"
            else ->
                date.format(
                    DateTimeFormatter.ofPattern(
                        "MMM d, EEEE",
                        englishLocal
                    )
                )
        }.toString()
    }

    fun getPreviousWeekRange(): Pair<Instant, Instant> {
        val currentDateTime = LocalDate.now(zoneId)

        val lastMonday = currentDateTime.minusWeeks(1)
            .with(DayOfWeek.MONDAY)
            .atStartOfDay(zoneId)
            .toInstant()

        val lastSunday = lastMonday.plus(6, ChronoUnit.DAYS)

        return Pair(lastMonday, lastSunday)
    }

}