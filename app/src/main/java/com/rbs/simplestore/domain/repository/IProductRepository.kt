package com.rbs.simplestore.domain.repository

import com.rbs.simplestore.data.model.CartData
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.domain.model.CartDomain
import com.rbs.simplestore.domain.model.ProductDomain

interface IProductRepository {
    suspend fun getProducts(): ResultState<List<ProductDomain>>
    suspend fun addCart(request: CartDomain): ResultState<Boolean>
    suspend fun getCart(userId: Int): ResultState<List<CartDomain>>
    suspend fun deleteCart(id: Int): ResultState<Boolean>
}