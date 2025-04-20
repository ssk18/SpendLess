package com.ssk.dashboard.data.di

import com.ssk.dashboard.data.CsvExporterImpl
import com.ssk.dashboard.domain.CsvExporter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val csvDataModule = module {
    single<CsvExporter> { CsvExporterImpl(androidContext()) }
}