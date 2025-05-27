package com.takumi.kasirkain.domain.model

data class TransactionHeader(
    val id: Int,
    val user: User,
    val paymentType: String,
    val cashReceived: Int,
    val changeReturned: Int,
    val total: Int,
    val details: List<TransactionDetail>,
    val createdAt: String
)
