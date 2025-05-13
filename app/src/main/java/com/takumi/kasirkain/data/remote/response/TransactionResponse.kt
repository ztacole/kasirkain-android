package com.takumi.kasirkain.data.remote.response

import com.takumi.kasirkain.domain.model.Transaction

data class TransactionResponse(
    val date: String,
    val transactions: List<TransactionHeaderResponse>
) {
    fun toDomain(): Transaction {
        return Transaction(
            date = date,
            transactions = transactions.map { it.toDomain() }
        )
    }
}
