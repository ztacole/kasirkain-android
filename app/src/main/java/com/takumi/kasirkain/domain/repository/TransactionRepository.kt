package com.takumi.kasirkain.domain.repository

import com.takumi.kasirkain.domain.model.Transaction

interface TransactionRepository {
    suspend fun getTransactions(): List<Transaction>
}