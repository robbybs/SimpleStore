package com.rbs.simplestore.domain.repository

import com.rbs.simplestore.data.remote.model.LoginRequest
import com.rbs.simplestore.data.remote.network.ResultState

interface ILoginRepository {
    suspend fun login(request: LoginRequest): ResultState<Boolean>
}