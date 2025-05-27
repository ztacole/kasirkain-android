package com.takumi.kasirkain.data.repository

import com.takumi.kasirkain.data.remote.RemoteDataSource
import com.takumi.kasirkain.domain.model.Checkout
import com.takumi.kasirkain.domain.model.GroupedTransaction
import com.takumi.kasirkain.domain.model.TransactionHeader
import com.takumi.kasirkain.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
): TransactionRepository {
    override suspend fun getTransactions(): List<GroupedTransaction> {
        val response = remote.getTransactions()
        return response.map { it.toDomain() }
    }

    override suspend fun getTransactionById(id: Int): TransactionHeader {
        val response = remote.getTransactionById(id)
        return response.toDomain()
    }

    override suspend fun checkout(checkoutTransaction: Checkout): TransactionHeader {
        val response = remote.checkout(checkoutTransaction.toData())
        return response.toDomain()
    }
}