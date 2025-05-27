package com.takumi.kasirkain.data.remote.model.request

data class CheckoutDetailRequest(
    val product_variant_id: Int,
    val quantity: Int
)
