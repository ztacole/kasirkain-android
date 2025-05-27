package com.takumi.kasirkain.data.remote.response

import com.takumi.kasirkain.domain.model.TransactionDetail

data class TransactionDetailResponse(
    val id: Int,
    val product: ProductDetailResponse,
    val quantity: Int
) {
    fun toDomain() : TransactionDetail {
        return TransactionDetail(
            id = id,
            product = product.toDomain(),
            quantity = quantity
        )
    }
}
