package com.takumi.kasirkain.domain.model

data class GroupedTransaction(
    val date: String,
    val transactions: List<Transaction>
)
