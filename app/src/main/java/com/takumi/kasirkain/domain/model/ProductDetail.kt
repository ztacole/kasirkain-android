package com.takumi.kasirkain.domain.model

data class ProductDetail(
    val id: Int,
    val name: String,
    val price: Int,
    val image: String,
    val category: Category,
    val variants: List<ProductVariant>,
    val activeEvents: List<Event>,
    val discount: Int,
    val finalPrice: Int,
) {
    fun toProduct(): Product {
        return Product(
            id = id,
            name = name,
            price = price,
            image = image,
            category = category,
            variantCount = variants.size,
            activeEvents = activeEvents,
            discount = discount,
            finalPrice = finalPrice
        )
    }
}
