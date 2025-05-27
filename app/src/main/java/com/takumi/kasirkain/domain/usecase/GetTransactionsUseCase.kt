package com.takumi.kasirkain.domain.usecase

import com.takumi.kasirkain.domain.model.GroupedTransaction
import com.takumi.kasirkain.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(): List<GroupedTransaction> {
        return repository.getTransactions()
    }
}