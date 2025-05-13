package com.takumi.kasirkain.data.repository

import com.takumi.kasirkain.data.remote.RemoteDataSource
import com.takumi.kasirkain.domain.model.Transaction
import com.takumi.kasirkain.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
): TransactionRepository {
    override suspend fun getTransactions(): List<Transaction> {
        val response = remote.getTransactions()
        return response.map { it.toDomain() }
    }
}