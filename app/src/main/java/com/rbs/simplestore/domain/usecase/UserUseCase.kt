package com.rbs.simplestore.domain.usecase

import com.rbs.simplestore.data.remote.model.LoginRequest
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.data.repository.UserRepository
import com.rbs.simplestore.domain.model.UserDomain
import com.rbs.simplestore.presentation.model.RegisterRequest

class UserUseCase(
    private val repository: UserRepository
){
    suspend fun registerUser(request: RegisterRequest): ResultState<Boolean> = repository.registerUser(request)
    suspend fun loginUser(request: LoginRequest): ResultState<UserDomain> = repository.loginUser(request)
    suspend fun getDetailUser(id: Int): ResultState<UserDomain> = repository.getDetailUser(id)
}