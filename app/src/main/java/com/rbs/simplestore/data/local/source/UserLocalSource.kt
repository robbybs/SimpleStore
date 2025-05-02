package com.rbs.simplestore.data.local.source

import com.rbs.simplestore.data.local.dao.StoreDao
import com.rbs.simplestore.data.mapper.toData
import com.rbs.simplestore.data.mapper.toEntity
import com.rbs.simplestore.data.model.UserData
import com.rbs.simplestore.data.remote.model.LoginRequest
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.presentation.model.RegisterRequest

class UserLocalSource(
    private val storeDao: StoreDao
) {
    suspend fun registerUser(inquiry: RegisterRequest): ResultState<Boolean> = try {
        val request = inquiry.toEntity()
        storeDao.registerUser(request)
        ResultState.Success(true)
    } catch (e: Exception) {
        ResultState.Error(e.message.toString())
    }

    suspend fun loginUser(request: LoginRequest): ResultState<UserData> = try {
        val result = storeDao.loginUser(request.username, request.password)
        ResultState.Success(result.toData())
    } catch (e: Exception) {
        ResultState.Error(e.message.toString())
    }

    suspend fun getDetailUser(id: Int): ResultState<UserData> = try {
        val result = storeDao.getDetailUser(id)
        ResultState.Success(result.toData())
    } catch (e: Exception) {
        ResultState.Error(e.message.toString())
    }
}