package com.rbs.simplestore.data.remote.source

import com.rbs.simplestore.data.mapper.toData
import com.rbs.simplestore.data.model.ProductData
import com.rbs.simplestore.data.remote.network.ApiService
import com.rbs.simplestore.data.remote.network.ResultState

class ProductRemoteSource(
    private val apiService: ApiService
) {
    suspend fun getProducts(): ResultState<List<ProductData>> = try {
        val result = apiService.getProducts()
        ResultState.Success(result.map { it.toData() })
    } catch (e: Exception) {
        ResultState.Error(e.message.toString())
    }
}