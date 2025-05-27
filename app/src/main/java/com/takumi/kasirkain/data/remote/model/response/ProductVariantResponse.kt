package com.takumi.kasirkain.data.remote.model.response

import com.takumi.kasirkain.domain.model.ProductVariant

data class ProductVariantResponse(
    val id: Int,
    val size: String,
    val color: String,
    val barcode: String,
    val stock: Int,
) {
    fun toDomain() : ProductVariant {
        return ProductVariant(
            id = id,
            size = size,
            color = color,
            barcode = barcode,
            stock = stock
        )
    }
}
