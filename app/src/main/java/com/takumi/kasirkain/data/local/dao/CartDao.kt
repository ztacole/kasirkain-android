package com.takumi.kasirkain.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import androidx.room.Update
import com.takumi.kasirkain.data.local.entity.CartEntity

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    suspend fun getCartItems(): List<CartEntity>?

    @Insert(onConflict = IGNORE)
    suspend fun insertCartItem(item: CartEntity)

    @Query("UPDATE cart SET quantity = :quantity WHERE productVariantId = :productVariantId")
    suspend fun updateQuantity(productVariantId: Int, quantity: Int)

    @Query("DELETE FROM cart WHERE productVariantId = :productVariantId")
    suspend fun deleteCartItem(productVariantId: Int)

    @Query("DELETE FROM cart")
    suspend fun clearCart()

    @Query("SELECT * FROM cart WHERE productId = :productId AND productVariantId = :productVariantId")
    suspend fun getItem(productId: Int, productVariantId: Int): CartEntity?

    suspend fun insertOrUpdate(item: CartEntity) {
        val existingItem = getItem(item.productId, item.productVariantId)
        if (existingItem != null && existingItem.quantity < existingItem.stock) {
            updateQuantity(item.productVariantId, existingItem.quantity + item.quantity)
        } else if (existingItem == null) {
            insertCartItem(item)
        }
    }
}