package com.rbs.simplestore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rbs.simplestore.data.local.entity.CartEntity
import com.rbs.simplestore.data.local.entity.UserEntity

@Dao
interface StoreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registerUser(userEntity: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE username = :username AND password = :password")
    suspend fun loginUser(username: String, password: String): UserEntity

    @Query("SELECT * FROM UserEntity WHERE id = :id")
    suspend fun getDetailUser(id: Int): UserEntity

    @Insert
    suspend fun insertCart(cartEntity: CartEntity)

    @Query("SELECT * FROM CartEntity WHERE userId = :id")
    suspend fun getUserCart(id: Int): List<CartEntity>

    @Query("DELETE FROM CartEntity WHERE id = :id")
    suspend fun deleteCart(id: Int)
}