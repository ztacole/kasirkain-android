package com.takumi.kasirkain.domain.model


data class CartItem(
    val id: Int,
    val productId: Int,
    val productName: String,
    val productImage: String,
    val productPrice: Int,
    val productVariantId: Int,
    val barcode: String,
    val stock: Int,
    val quantity: Int
)
