package com.takumi.kasirkain.domain.repository

import com.takumi.kasirkain.domain.model.Checkout
import com.takumi.kasirkain.domain.model.GroupedTransaction
import com.takumi.kasirkain.domain.model.TransactionHeader

interface TransactionRepository {
    suspend fun getTransactions(): List<GroupedTransaction>
    suspend fun getTransactionById(id: Int): TransactionHeader
    suspend fun checkout(checkoutTransaction: Checkout): TransactionHeader
}