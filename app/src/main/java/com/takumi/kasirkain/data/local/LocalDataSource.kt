package com.takumi.kasirkain.data.local

import com.takumi.kasirkain.data.local.entity.CartEntity
import com.takumi.kasirkain.data.local.entity.TokenEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val db: AppDatabase
) {
    suspend fun saveToken(token: String) {
        db.tokenDao().saveToken(TokenEntity(token = token))
    }

    suspend fun getToken(): String? = db.tokenDao().getToken()?.token

    suspend fun clearToken() {
        db.tokenDao().clearToken()
    }

    suspend fun getCartItems(): List<CartEntity> {
        return db.cartDao().getCartItems() ?: emptyList()
    }

    suspend fun insertCartItem(item: CartEntity) {
        db.cartDao().insertOrUpdate(item)
    }

    suspend fun updateCartItem(item: CartEntity) {
        db.cartDao().updateCartItem(item)
    }

    suspend fun deleteCartItem(item: CartEntity) {
        db.cartDao().deleteCartItem(item)
    }

    suspend fun clearCart() {
        db.cartDao().clearCart()
    }
}