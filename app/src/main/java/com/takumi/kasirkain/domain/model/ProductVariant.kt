package com.takumi.kasirkain.domain.model

data class ProductVariant(
    val id: Int,
    val size: String,
    val color: String,
    val barcode: String,
    val stock: Int,
)
