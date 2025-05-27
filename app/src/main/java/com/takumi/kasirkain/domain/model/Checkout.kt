package com.takumi.kasirkain.domain.model

import com.takumi.kasirkain.data.remote.request.CheckoutRequest

data class Checkout(
    val userId: Int,
    val paymentType: String,
    val cashReceived: Long,
    val changeReturned: Long,
    val details: List<CheckoutDetail>
) {
    fun toData(): CheckoutRequest {
        return CheckoutRequest(
            user_id = userId,
            payment_type = paymentType,
            cash_received = cashReceived,
            change_returned = changeReturned,
            details = details.map { it.toData() }
        )
    }
}
