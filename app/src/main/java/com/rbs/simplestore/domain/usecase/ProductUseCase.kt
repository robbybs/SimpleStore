package com.rbs.simplestore.domain.usecase

import com.rbs.simplestore.data.model.CartData
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.data.repository.ProductRepository
import com.rbs.simplestore.domain.model.CartDomain
import com.rbs.simplestore.domain.model.ProductDomain

class ProductUseCase(
    private val repository: ProductRepository
) {
    suspend fun getProducts(): ResultState<List<ProductDomain>> = repository.getProducts()
    suspend fun addCart(request: CartDomain): ResultState<Boolean> = repository.addCart(request)
    suspend fun getCart(userId: Int): ResultState<List<CartDomain>> = repository.getCart(userId)
    suspend fun deleteCart(id: Int): ResultState<Boolean> = repository.deleteCart(id)
}