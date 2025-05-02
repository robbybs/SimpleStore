package com.rbs.simplestore.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductDomain(
    val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val category: String,
    val image: String
) : Parcelable