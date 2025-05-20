package com.takumi.kasirkain.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.takumi.kasirkain.data.local.entity.CartEntity

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    suspend fun getCartItems(): List<CartEntity>?

    @Insert(onConflict = REPLACE)
    suspend fun insertCartItem(item: CartEntity)

    @Query("UPDATE cart SET stock = :stock WHERE productVariantId = :productVariantId")
    suspend fun updateStockByVariantId(productVariantId: Int, stock: Int)

    @Query("DELETE FROM cart WHERE productVariantId = :productVariantId")
    suspend fun deleteByVariantId(productVariantId: Int)

    @Query("DELETE FROM cart")
    suspend fun clearCart()
}