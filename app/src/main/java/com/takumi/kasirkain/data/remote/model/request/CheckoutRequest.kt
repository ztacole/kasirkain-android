package com.takumi.kasirkain.data.remote.model.request

data class CheckoutRequest(
    val user_id: Int,
    val payment_type: String,
    val cash_received: Long,
    val change_returned: Long,
    val details: List<CheckoutDetailRequest>
)
