package com.rbs.simplestore.data.remote.network

import com.rbs.simplestore.data.remote.model.LoginRequest
import com.rbs.simplestore.data.remote.model.LoginResponse
import com.rbs.simplestore.data.remote.model.ProductResponse
import com.rbs.simplestore.data.remote.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("products")
    suspend fun getProducts(): List<ProductResponse>

    @GET("users")
    suspend fun getUsers(): List<UserResponse>
}