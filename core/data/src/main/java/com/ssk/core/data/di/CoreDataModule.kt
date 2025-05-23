package com.ssk.core.data.di

import com.ssk.core.data.repository.SessionRepository
import com.ssk.core.data.repository.TransactionsRepository
import com.ssk.core.data.repository.UserRepository
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.domain.repository.ITransactionsRepository
import com.ssk.core.domain.repository.IUserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    singleOf(::UserRepository).bind<IUserRepository>()
    singleOf(::SessionRepository).bind<ISessionRepository>()
    singleOf(::TransactionsRepository).bind<ITransactionsRepository>()
}