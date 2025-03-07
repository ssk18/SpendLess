package com.ssk.spendless.auth.data.di

import com.ssk.spendless.auth.data.repository.UserRepository
import com.ssk.spendless.auth.domain.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepoModule {

    @Binds
    @Singleton
    abstract fun provideUserRepository(
        userRepository: UserRepository
    ): IUserRepository

}