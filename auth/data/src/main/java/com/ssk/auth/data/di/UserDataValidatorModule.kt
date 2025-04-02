package com.ssk.auth.data.di

import com.ssk.auth.data.userDataValidator.UserDataValidator
import com.ssk.auth.domain.IUserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val userDataValidatorModule = module {
    singleOf(::UserDataValidator).bind<IUserDataValidator>()
}