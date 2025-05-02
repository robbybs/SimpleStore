package com.rbs.simplestore.data.mapper

import com.rbs.simplestore.data.local.entity.CartEntity
import com.rbs.simplestore.data.local.entity.UserEntity
import com.rbs.simplestore.data.model.CartData
import com.rbs.simplestore.data.model.ProductData
import com.rbs.simplestore.data.model.UserData
import com.rbs.simplestore.data.remote.model.ProductResponse
import com.rbs.simplestore.presentation.model.RegisterRequest

fun ProductResponse.toData() = ProductData(
    id = id ?: 0,
    title = title.orEmpty(),
    description = description.orEmpty(),
    price = price ?: 0.0,
    category = category.orEmpty(),
    image = image.orEmpty()
)

fun RegisterRequest.toEntity() = UserEntity(
    name = name,
    username = username,
    email = email,
    password = password
)

fun UserEntity.toData() = UserData(
    id = id,
    username = username,
    name = name,
    email = email
)

fun CartEntity.toData() = CartData(
    id = id,
    userId = userId,
    title = title,
    description = description,
    price = price,
    image = image,
    category = category
)

fun CartData.toEntity() = CartEntity(
    id = id,
    userId = userId,
    title = title,
    description = description,
    price = price,
    image = image,
    category = category
)