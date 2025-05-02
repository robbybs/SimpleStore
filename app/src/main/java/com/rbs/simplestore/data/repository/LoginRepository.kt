package com.rbs.simplestore.data.repository

import com.rbs.simplestore.data.remote.model.LoginRequest
import com.rbs.simplestore.data.remote.model.LoginResponse
import com.rbs.simplestore.data.remote.network.ApiService
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.data.remote.source.LoginRemoteSource
import com.rbs.simplestore.domain.repository.ILoginRepository
import javax.inject.Inject

class LoginRepository (
    private val remoteSource: LoginRemoteSource
) : ILoginRepository {
    override suspend fun login(request: LoginRequest): ResultState<Boolean> = when(val result = remoteSource.login(request)) {
        is ResultState.Success -> ResultState.Success(true)
        is ResultState.Error -> ResultState.Error(result.message)
        else -> ResultState.Loading
    }
}