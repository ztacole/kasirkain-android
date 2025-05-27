package com.takumi.kasirkain.domain.model

data class Transaction(
    val id: Int,
    val user: User,
    val paymentType: String,
    val cashReceived: Int,
    val changeReturned: Int,
    val productCount: Int,
    val total: Int,
    val time: String
)
