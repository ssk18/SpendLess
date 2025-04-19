package com.ssk.dashboard.data

import com.ssk.dashboard.domain.CsvExporter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val csvDataModule = module {
    single<CsvExporter> { CsvExporterImpl(androidContext()) }
}