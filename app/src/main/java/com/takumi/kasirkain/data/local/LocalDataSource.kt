package com.takumi.kasirkain.data.local

import com.takumi.kasirkain.data.local.entity.CartEntity
import com.takumi.kasirkain.domain.model.CartItem
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val db: AppDatabase,
    private val tokenManager: TokenManager
) {
    suspend fun saveToken(token: String) {
        tokenManager.saveToken(token)
    }

    suspend fun getToken(): String? = tokenManager.getToken()

    suspend fun clearToken() {
        tokenManager.clearToken()
    }

    suspend fun getCartItems(): List<CartEntity> {
        return db.cartDao().getCartItems() ?: emptyList()
    }

    suspend fun insertCartItem(item: CartEntity) {
        db.cartDao().insertOrUpdate(item)
    }

    suspend fun updateQuantity(productVariantId: Int, quantity: Int) {
        db.cartDao().updateQuantity(productVariantId, quantity)
    }

    suspend fun updateExpiredEventItem(cartItem: CartEntity) {
        db.cartDao().updateExpiredEventItem(cartItem)
    }

    suspend fun deleteCartItem(item: CartEntity) {
        db.cartDao().deleteCartItem(item.productVariantId)
    }

    suspend fun clearCart() {
        db.cartDao().clearCart()
    }
}