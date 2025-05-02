package com.rbs.simplestore.di

import com.rbs.simplestore.data.repository.LoginRepository
import com.rbs.simplestore.data.repository.ProductRepository
import com.rbs.simplestore.data.repository.UserRepository
import com.rbs.simplestore.domain.usecase.LoginUseCase
import com.rbs.simplestore.domain.usecase.ProductUseCase
import com.rbs.simplestore.domain.usecase.UserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideLoginUseCase(repository: LoginRepository): LoginUseCase = LoginUseCase(repository)

    @Provides
    @Singleton
    fun provideProductUseCase(repository: ProductRepository): ProductUseCase = ProductUseCase(repository)

    @Provides
    @Singleton
    fun provideUserUseCase(repository: UserRepository): UserUseCase = UserUseCase(repository)
}