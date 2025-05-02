package com.rbs.simplestore.domain.usecase

import com.rbs.simplestore.data.remote.model.LoginRequest
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.data.repository.LoginRepository

class LoginUseCase(
    private val repository: LoginRepository
) {
    suspend fun loginUser(request: LoginRequest): ResultState<Boolean> = repository.login(request)
}