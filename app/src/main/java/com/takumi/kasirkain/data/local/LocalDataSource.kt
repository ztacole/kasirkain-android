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
        db.cartDao().insertCartItem(item)
    }

    suspend fun updateStockByVariantId(productVariantId: Int, stock: Int) {
        db.cartDao().updateStockByVariantId(productVariantId, stock)
    }

    suspend fun deleteByVariantId(productVariantId: Int) {
        db.cartDao().deleteByVariantId(productVariantId)
    }

    suspend fun clearCart() {
        db.cartDao().clearCart()
    }
}