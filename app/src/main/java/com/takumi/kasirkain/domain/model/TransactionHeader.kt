package com.takumi.kasirkain.domain.model

data class TransactionHeader(
    val id: Int,
    val user: User,
    val paymentType: String,
    val productCount: Int,
    val total: Int,
    val time: String
)
