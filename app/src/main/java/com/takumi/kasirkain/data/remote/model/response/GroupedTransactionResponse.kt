package com.takumi.kasirkain.data.remote.model.response

import com.takumi.kasirkain.domain.model.GroupedTransaction

data class GroupedTransactionResponse(
    val date: String,
    val transactions: List<TransactionResponse>
) {
    fun toDomain(): GroupedTransaction {
        return GroupedTransaction(
            date = date,
            transactions = transactions.map { it.toDomain() }
        )
    }
}
