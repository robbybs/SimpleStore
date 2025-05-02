package com.rbs.simplestore.di

import com.rbs.simplestore.data.local.DataStoreManager
import com.rbs.simplestore.data.local.source.ProductLocalSource
import com.rbs.simplestore.data.remote.source.LoginRemoteSource
import com.rbs.simplestore.data.remote.source.ProductRemoteSource
import com.rbs.simplestore.data.local.source.UserLocalSource
import com.rbs.simplestore.data.repository.LoginRepository
import com.rbs.simplestore.data.repository.ProductRepository
import com.rbs.simplestore.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideLoginRepository(remoteSource: LoginRemoteSource): LoginRepository = LoginRepository(remoteSource)

    @Provides
    @Singleton
    fun provideProductRepository(
        localSource: ProductLocalSource,
        remoteSource: ProductRemoteSource
    ): ProductRepository = ProductRepository(localSource,remoteSource)

    @Provides
    @Singleton
    fun provideUserRepository(localSource: UserLocalSource): UserRepository = UserRepository(localSource)
}