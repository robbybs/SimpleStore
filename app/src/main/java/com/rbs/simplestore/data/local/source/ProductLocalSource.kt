package com.rbs.simplestore.data.local.source

import com.rbs.simplestore.data.local.dao.StoreDao
import com.rbs.simplestore.data.mapper.toData
import com.rbs.simplestore.data.mapper.toEntity
import com.rbs.simplestore.data.model.CartData
import com.rbs.simplestore.data.remote.network.ResultState

class ProductLocalSource(
    private val storeDao: StoreDao
) {
    suspend fun addCart(request: CartData): ResultState<Boolean> = try {
        storeDao.insertCart(request.toEntity())
        ResultState.Success(true)
    } catch (e: Exception) {
        ResultState.Error(e.message.toString())
    }

    suspend fun getCart(userId: Int): ResultState<List<CartData>> = try {
        val result = storeDao.getUserCart(userId)
        ResultState.Success(result.map { it.toData() })
    } catch (e: Exception) {
        ResultState.Error(e.message.toString())
    }

    suspend fun deleteCart(cartID: Int): ResultState<Boolean> = try {
        storeDao.deleteCart(cartID)
        ResultState.Success(true)
    } catch (e: Exception) {
        ResultState.Error(e.message.toString())
    }
}