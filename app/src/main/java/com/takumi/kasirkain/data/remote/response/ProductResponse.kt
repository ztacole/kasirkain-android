package com.takumi.kasirkain.data.remote.response

import com.takumi.kasirkain.domain.model.Product

data class ProductResponse(
    val id: Int,
    val name: String,
    val price: Int,
    val image: String,
    val category: CategoryResponse,
    val varian_count: Int
) {
    fun toDomain(): Product {
        return Product(
            id = id,
            name = name,
            price = price,
            image = image,
            category = category.toDomain(),
            variantCount = varian_count
        )
    }
}
