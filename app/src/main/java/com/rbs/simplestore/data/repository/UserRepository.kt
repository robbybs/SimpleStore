package com.rbs.simplestore.data.repository

import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.data.local.source.UserLocalSource
import com.rbs.simplestore.data.remote.model.LoginRequest
import com.rbs.simplestore.domain.mapper.toDomain
import com.rbs.simplestore.domain.model.UserDomain
import com.rbs.simplestore.domain.repository.IUserRepository
import com.rbs.simplestore.presentation.model.RegisterRequest

class UserRepository(
    private val localSource: UserLocalSource
) : IUserRepository {
    override suspend fun loginUser(request: LoginRequest): ResultState<UserDomain> = when (val result = localSource.loginUser(request)) {
        ResultState.Loading -> ResultState.Loading
        is ResultState.Success -> ResultState.Success(result.data.toDomain())
        is ResultState.Error -> ResultState.Error(result.message)
    }

    override suspend fun registerUser(request: RegisterRequest): ResultState<Boolean> = when (val result = localSource.registerUser(request)) {
        ResultState.Loading -> ResultState.Loading
        is ResultState.Success -> ResultState.Success(result.data)
        is ResultState.Error -> ResultState.Error(result.message)
    }

    override suspend fun getDetailUser(id: Int): ResultState<UserDomain> = when (val result = localSource.getDetailUser(id)) {
        ResultState.Loading -> ResultState.Loading
        is ResultState.Success -> ResultState.Success(result.data.toDomain())
        is ResultState.Error -> ResultState.Error(result.message)
    }
}