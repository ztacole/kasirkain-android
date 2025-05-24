package com.takumi.kasirkain.domain.usecase

import com.takumi.kasirkain.data.local.LocalDataSource
import com.takumi.kasirkain.data.local.mapper.toData
import com.takumi.kasirkain.domain.model.CartItem
import com.takumi.kasirkain.domain.repository.CartRepository
import javax.inject.Inject

class DeleteCartItemUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(item: CartItem) {
        repository.deleteCartItem(item)
    }
}