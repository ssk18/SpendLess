package com.ssk.dashboard.data

import com.ssk.core.domain.model.RepeatType
import com.ssk.core.domain.model.Transaction
import com.ssk.core.domain.model.TransactionType
import com.ssk.core.domain.utils.InstantFormatter
import com.ssk.dashboard.domain.CsvExporter

class CsvExporterImpl : CsvExporter {

    override fun exportToCsv(
        fileName: String,
        transactions: List<Transaction>
    ) {
        TODO("Not yet implemented")
    }

    private fun convertToCsv(transactions: List<Transaction>): String {
        return buildString {
            append("ID,Title,Amount,Type,Category,Repeat Type,Note,Transaction Date\n")
            transactions.forEach { transaction ->
                append(
                    "${transaction.id}," +
                            "\"${transaction.title}\"," +
                            "${transaction.amount}," +
                            "${getTransactionType(transaction.transactionType)}," +
                            "${getTransactionCategory(transaction.transactionType)}," +
                            "${getRepeatType(transaction.repeatType)}," +
                            "\"${transaction.note}\"," +
                            "${InstantFormatter.formatDateString(transaction.transactionDate)}\n"
                )
            }

        }
    }

    private fun getTransactionType(transactionType: TransactionType): String {
        return if (transactionType != TransactionType.INCOME) {
            "Expense"
        } else {
            "Income"
        }
    }

    private fun getTransactionCategory(type: TransactionType): String {
        return if (type != TransactionType.INCOME) {
            type.type
                .lowercase()
                .replaceFirstChar { it.uppercase() }
        } else "Income"
    }

    private fun getRepeatType(repeatType: RepeatType): String {
        return repeatType.title
            .lowercase()
            .replaceFirstChar { it.uppercase() }
    }

}