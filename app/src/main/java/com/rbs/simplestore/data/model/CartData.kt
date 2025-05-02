package com.rbs.simplestore.data.model

data class CartData(
    val id: Int,
    val userId: Int,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val image: String
)
