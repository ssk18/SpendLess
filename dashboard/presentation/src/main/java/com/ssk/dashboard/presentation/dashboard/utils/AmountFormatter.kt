package com.ssk.dashboard.presentation.dashboard.utils

import com.ssk.core.presentation.ui.components.DecimalSeparatorUi
import com.ssk.core.presentation.ui.components.ThousandsSeparatorUi
import com.ssk.dashboard.presentation.dashboard.DashboardState.AmountSettings

object AmountFormatter {

    private const val MAX_AMOUNT_INTEGER_LENGTH = 9
    private const val MAX_NUMBER_OF_SEPARATORS = 2
    private const val MAX_DECIMAL_LENGTH = 2

    fun getFormatedAmount(
        amount: CharSequence,
        amountSettings: AmountSettings,
        enableTwoDecimal: Boolean = false
    ): String {
        val decimalSeparator = amountSettings.decimalSeparator.separator
        val thousandSeparator = amountSettings.thousandsSeparator.separator

        val filteredAmount = amount.filter { it.isDigit() || it == '.' || it == ',' }
        val replacedAmount = replaceDecimalSeparator(filteredAmount, decimalSeparator)
        val parts = replacedAmount.split(decimalSeparator)
        val integerPart = getIntegerPart(parts[0], thousandSeparator)
        val formattedIntegerPart = formatThousands(integerPart, thousandSeparator)

        var resultInt = formattedIntegerPart
        if (parts.size > 1) {
            val fractionalPart = when {
                enableTwoDecimal && parts[1].length == 1 -> parts[1] + "0"
                enableTwoDecimal && parts[1].isEmpty() -> "00"
                else -> parts[1].take(MAX_DECIMAL_LENGTH)
            }
            resultInt += decimalSeparator + fractionalPart
        }
        return resultInt
    }

    fun parseAmountToFloat(
        amount: CharSequence,
        amountSettings: AmountSettings,
        isExpense: Boolean
    ): Float {
        val decimalSeparator = amountSettings.decimalSeparator.separator
        val cleanedAmount = amount.toString()
            .replace(amountSettings.thousandsSeparator.separator, "")
            .replace(decimalSeparator, ".")
            .toFloatOrNull()
            ?: throw NumberFormatException("Invalid amount format: $amount")

        return if (isExpense) -cleanedAmount else cleanedAmount
    }

    fun formatUserInput(
        amount: Float,
        amountSettings: AmountSettings,
        enableTwoDecimal: Boolean = false
    ): String {
        return getFormatedAmount(
            amount = amount.toString()
                .replace(".", amountSettings.decimalSeparator.separator),
            amountSettings = amountSettings,
            enableTwoDecimal = enableTwoDecimal
        )
    }

    private fun replaceDecimalSeparator(
        amount: CharSequence,
        decimalSeparator: String
    ): CharSequence {
        val lastIndex = amount.lastIndexOfAny(
            listOf(
                DecimalSeparatorUi.POINT.separator,
                DecimalSeparatorUi.COMMA.separator
            )
        )

        if (lastIndex == -1 || lastIndex + 2 < amount.length) return amount

        val integerPart = amount.substring(0, lastIndex)
        val decimalPart = amount.substring(lastIndex + 1)
        return integerPart + decimalSeparator + decimalPart
    }

    private fun formatThousands(integerPart: String, thousandSeparator: String): String {
        val cleanIntegerPart = integerPart.replace(thousandSeparator, "")
        val reversed = cleanIntegerPart.reversed()
        val formatted = StringBuilder()

        for (i in reversed.indices) {
            if (i > 0 && i % 3 == 0) {
                formatted.append(thousandSeparator)
            }
            formatted.append(reversed[i])
        }
        return formatted.reverse().toString()
    }

    private fun getIntegerPart(integerPart: String, thousandSeparator: String): String {
        val maxIntegerDigits = when (thousandSeparator) {
            ThousandsSeparatorUi.SPACE.separator -> MAX_AMOUNT_INTEGER_LENGTH
            else -> MAX_AMOUNT_INTEGER_LENGTH + MAX_NUMBER_OF_SEPARATORS
        }
        return integerPart.take(maxIntegerDigits)
    }
}