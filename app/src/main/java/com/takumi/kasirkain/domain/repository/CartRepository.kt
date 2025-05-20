package com.takumi.kasirkain.domain.repository

import com.takumi.kasirkain.domain.model.CartItem

interface CartRepository {
    suspend fun getCartItems(): List<CartItem>
    suspend fun insertCartItem(item: CartItem)
    suspend fun updateStockByVariantId(productVariantId: Int, stock: Int)
    suspend fun deleteByVariantId(productVariantId: Int)
    suspend fun clearCart()
}