package com.takumi.kasirkain.data.remote.response

import com.takumi.kasirkain.domain.model.ProductDetail

data class ProductDetailResponse(
    val id: Int,
    val name: String,
    val price: Int,
    val image: String,
    val category: CategoryResponse,
    val variants: List<ProductVariantResponse>
) {
    fun toDomain(): ProductDetail {
        return ProductDetail(
            id = id,
            name = name,
            price = price,
            image = image,
            category = category.toDomain(),
            variants = variants.map { it.toDomain() }
        )
    }
}
