package com.takumi.kasirkain.data.repository

import com.takumi.kasirkain.data.local.LocalDataSource
import com.takumi.kasirkain.data.local.mapper.toData
import com.takumi.kasirkain.data.local.mapper.toDomain
import com.takumi.kasirkain.domain.model.CartItem
import com.takumi.kasirkain.domain.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
): CartRepository {
    override suspend fun getCartItems(): List<CartItem> {
        return localDataSource.getCartItems().map { it.toDomain() }
    }

    override suspend fun insertCartItem(item: CartItem) {
        localDataSource.insertCartItem(item.toData())
    }

    override suspend fun updateCartItem(cartItem: CartItem) {
        localDataSource.updateCartItem(cartItem.toData())
    }

    override suspend fun deleteCartItem(cartItem: CartItem) {
        localDataSource.deleteCartItem(cartItem.toData())
    }

    override suspend fun clearCart() {
        localDataSource.clearCart()
    }
}