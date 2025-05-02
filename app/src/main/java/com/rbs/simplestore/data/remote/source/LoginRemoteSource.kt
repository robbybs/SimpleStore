package com.rbs.simplestore.data.remote.source

import com.rbs.simplestore.data.remote.model.LoginRequest
import com.rbs.simplestore.data.remote.network.ApiService
import com.rbs.simplestore.data.remote.network.ResultState

class LoginRemoteSource(
    private val apiService: ApiService
) {
    suspend fun login(request: LoginRequest): ResultState<Boolean> = try {
        apiService.login(request)
        ResultState.Success(true)
    } catch (e: Exception) {
        ResultState.Error(e.toString())
    }
}