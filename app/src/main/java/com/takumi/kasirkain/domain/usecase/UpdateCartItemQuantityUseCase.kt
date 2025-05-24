package com.takumi.kasirkain.domain.usecase

import com.takumi.kasirkain.data.local.LocalDataSource
import com.takumi.kasirkain.data.local.mapper.toData
import com.takumi.kasirkain.domain.model.CartItem
import com.takumi.kasirkain.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartItemQuantityUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(cartItem: CartItem) {
        repository.updateCartItem(cartItem)
    }
}