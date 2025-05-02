package com.rbs.simplestore.domain.model

data class CartDomain(
    val id: Int,
    val userId: Int,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val image: String
)
