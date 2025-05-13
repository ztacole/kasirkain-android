package com.takumi.kasirkain.domain.model

data class ProductDetail(
    val id: Int,
    val name: String,
    val price: Int,
    val image: String,
    val category: Category,
    val variants: List<ProductVariant>
)
