package com.rbs.simplestore.domain.mapper

import com.rbs.simplestore.data.model.CartData
import com.rbs.simplestore.data.model.ProductData
import com.rbs.simplestore.data.model.UserData
import com.rbs.simplestore.domain.model.CartDomain
import com.rbs.simplestore.domain.model.ProductDomain
import com.rbs.simplestore.domain.model.UserDomain

fun ProductData.toDomain() = ProductDomain(
    id = id,
    title = title,
    description = description,
    price = price.asDollarString(),
    category = category,
    image = image
)

fun Double.asDollarString(): String = "$$this"

fun UserData.toDomain() = UserDomain(
    id = id,
    email = email,
    username = username,
    name = name
)

fun CartData.toDomain() = CartDomain(
    id = id,
    userId = userId,
    title = title,
    description = description,
    price = price,
    image = image,
    category = category
)

fun CartDomain.toData() = CartData(
    id = id,
    userId = userId,
    title = title,
    description = description,
    price = price,
    image = image,
    category = category
)

fun ProductDomain.toCart(userID: Int) = CartDomain(
        id = id,
        userId = userID,
        title = title,
        description = description,
        price = price.removeDollarSign(),
        image = image,
        category = category
    )

fun String.removeDollarSign(): Double = this
    .replace("$", "")
    .trim()
    .toDouble()