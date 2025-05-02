package com.rbs.simplestore.domain.repository

import com.rbs.simplestore.data.remote.model.LoginRequest
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.domain.model.UserDomain
import com.rbs.simplestore.presentation.model.RegisterRequest

interface IUserRepository {
    suspend fun registerUser(request: RegisterRequest): ResultState<Boolean>
    suspend fun loginUser(request: LoginRequest): ResultState<UserDomain>
    suspend fun getDetailUser(id: Int): ResultState<UserDomain>
}