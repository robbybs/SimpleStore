package com.rbs.simplestore.data.model

data class ProductData(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val image: String
)
