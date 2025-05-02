package com.rbs.simplestore.data.repository

import com.rbs.simplestore.data.local.source.ProductLocalSource
import com.rbs.simplestore.data.model.CartData
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.data.remote.source.ProductRemoteSource
import com.rbs.simplestore.domain.mapper.toData
import com.rbs.simplestore.domain.mapper.toDomain
import com.rbs.simplestore.domain.model.CartDomain
import com.rbs.simplestore.domain.model.ProductDomain
import com.rbs.simplestore.domain.repository.IProductRepository

class ProductRepository(
    private val localSource: ProductLocalSource,
    private val remoteSource: ProductRemoteSource
) : IProductRepository {
    override suspend fun getProducts(): ResultState<List<ProductDomain>> =
        when (val result = remoteSource.getProducts()) {
            is ResultState.Success -> ResultState.Success(result.data.map { it.toDomain() })
            is ResultState.Error -> ResultState.Error(result.message)
            else -> ResultState.Loading
        }

    override suspend fun addCart(request: CartDomain): ResultState<Boolean> = when (val result = localSource.addCart(request.toData())) {
        is ResultState.Success -> ResultState.Success(true)
        is ResultState.Error -> ResultState.Error(result.message)
        else -> ResultState.Loading
    }

    override suspend fun getCart(userId: Int): ResultState<List<CartDomain>> = when (val result = localSource.getCart(userId)) {
        is ResultState.Success -> ResultState.Success(result.data.map { it.toDomain() })
        is ResultState.Error -> ResultState.Error(result.message)
        else -> ResultState.Loading
    }

    override suspend fun deleteCart(id: Int): ResultState<Boolean> = when (val result = localSource.deleteCart(id)) {
        is ResultState.Success -> ResultState.Success(true)
        is ResultState.Error -> ResultState.Error(result.message)
        else -> ResultState.Loading
    }
}