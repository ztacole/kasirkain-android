package com.takumi.kasirkain.domain.usecase

import com.takumi.kasirkain.domain.model.TransactionHeader
import com.takumi.kasirkain.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionByIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(id: Int) : TransactionHeader {
        return transactionRepository.getTransactionById(id)
    }
}