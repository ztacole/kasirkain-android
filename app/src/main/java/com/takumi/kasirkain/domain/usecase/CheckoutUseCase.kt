package com.takumi.kasirkain.domain.usecase

import com.takumi.kasirkain.domain.model.Checkout
import com.takumi.kasirkain.domain.model.CheckoutDetail
import com.takumi.kasirkain.domain.model.TransactionHeader
import com.takumi.kasirkain.domain.repository.AuthRepository
import com.takumi.kasirkain.domain.repository.CartRepository
import com.takumi.kasirkain.domain.repository.TransactionRepository
import javax.inject.Inject

class CheckoutUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val authRepository: AuthRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        paymentType: String,
        cashReceived: Long,
        changeReturned: Long
    ): TransactionHeader {
        val cartItems = cartRepository.getCartItems()
        val user = authRepository.profile()

        val details = mutableListOf<CheckoutDetail>()
        cartItems.forEach { item ->
            val transactionItem = CheckoutDetail(
                productVariantId = item.productVariantId,
                quantity = item.quantity
            )
            details.add(transactionItem)
        }

        val checkoutTransaction = Checkout(
            userId = user.id,
            paymentType = paymentType,
            cashReceived = cashReceived,
            changeReturned = changeReturned,
            details
        )

        return transactionRepository.checkout(checkoutTransaction)
    }
}