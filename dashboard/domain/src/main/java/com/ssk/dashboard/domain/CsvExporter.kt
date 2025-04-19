package com.ssk.dashboard.domain

import com.ssk.core.domain.model.Transaction

interface CsvExporter {
    fun exportToCsv(fileName: String, transactions: List<Transaction>)
}