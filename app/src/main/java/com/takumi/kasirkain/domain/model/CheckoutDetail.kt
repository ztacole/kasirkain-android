package com.takumi.kasirkain.domain.model

import com.takumi.kasirkain.data.remote.request.CheckoutDetailRequest

data class CheckoutDetail(
    val productVariantId: Int,
    val quantity: Int
) {
    fun toData(): CheckoutDetailRequest {
        return CheckoutDetailRequest(
            product_variant_id = productVariantId,
            quantity = quantity
        )
    }
}
