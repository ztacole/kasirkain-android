package com.takumi.kasirkain.domain.model

data class Transaction(
    val date: String,
    val transactions: List<TransactionHeader>
)
