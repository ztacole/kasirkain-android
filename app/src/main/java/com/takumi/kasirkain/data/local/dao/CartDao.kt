package com.takumi.kasirkain.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
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

    @Update
    suspend fun updateCartItem(item: CartEntity)

    @Delete
    suspend fun deleteCartItem(item: CartEntity)

    @Query("DELETE FROM cart")
    suspend fun clearCart()

    @Query("SELECT * FROM cart WHERE productId = :productId AND productVariantId = :productVariantId")
    suspend fun getItem(productId: Int, productVariantId: Int): CartEntity?

    suspend fun insertOrUpdate(item: CartEntity) {
        val existingItem = getItem(item.productId, item.productVariantId)
        if (existingItem != null && existingItem.quantity < existingItem.stock) {
            updateCartItem(existingItem.copy(quantity = existingItem.quantity + item.quantity))
        } else if (existingItem == null) {
            insertCartItem(item)
        }
    }
}