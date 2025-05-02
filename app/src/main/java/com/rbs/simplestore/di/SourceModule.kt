package com.rbs.simplestore.di

import com.rbs.simplestore.data.remote.network.ApiService
import com.rbs.simplestore.data.remote.source.LoginRemoteSource
import com.rbs.simplestore.data.remote.source.ProductRemoteSource
import com.rbs.simplestore.data.local.source.UserLocalSource
import com.rbs.simplestore.data.local.dao.StoreDao
import com.rbs.simplestore.data.local.source.ProductLocalSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SourceModule {
    @Provides
    @Singleton
    fun provideLoginRemoteSource(apiService: ApiService): LoginRemoteSource = LoginRemoteSource(apiService)

    @Provides
    @Singleton
    fun provideProductRemoteSource(apiService: ApiService): ProductRemoteSource = ProductRemoteSource(apiService)

    @Provides
    @Singleton
    fun provideProductLocalSource(storeDao: StoreDao): ProductLocalSource = ProductLocalSource(storeDao)

    @Provides
    @Singleton
    fun provideUserRemoteSource(storeDao: StoreDao): UserLocalSource = UserLocalSource(storeDao)
}