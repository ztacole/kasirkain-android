package com.takumi.kasirkain.domain.model

data class TransactionDetail(
    val id: Int,
    val product: ProductDetail,
    val quantity: Int
)
