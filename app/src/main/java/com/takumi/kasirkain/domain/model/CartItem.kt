package com.takumi.kasirkain.domain.model


data class CartItem(
    val id: Int,
    val productId: Int,
    val productName: String,
    val productImage: String,
    val productPrice: Int,
    val productDiscount: Int,
    val productFinalPrice: Int,
    val productEventName: String?,
    val productEventStartDate: String?,
    val productEventEndDate: String?,
    val productVariantId: Int,
    val productSize: String,
    val productColor: String,
    val barcode: String,
    val stock: Int,
    val quantity: Int
)
