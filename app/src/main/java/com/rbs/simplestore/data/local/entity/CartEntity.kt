package com.rbs.simplestore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userId: Int,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val image: String
)
