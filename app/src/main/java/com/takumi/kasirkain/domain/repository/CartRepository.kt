package com.takumi.kasirkain.domain.repository

import com.takumi.kasirkain.domain.model.CartItem

interface CartRepository {
    suspend fun getCartItems(): List<CartItem>
    suspend fun insertCartItem(item: CartItem)
    suspend fun updateQuantity(cartItem: CartItem)
    suspend fun updateExpiredEventItem(cartItem: CartItem)
    suspend fun deleteCartItem(cartItem: CartItem)
    suspend fun clearCart()
}