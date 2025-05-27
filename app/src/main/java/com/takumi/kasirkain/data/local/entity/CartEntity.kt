package com.takumi.kasirkain.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val productName: String,
    val productImage: String,
    val productPrice: Int,
    val productVariantId: Int,
    val productSize: String,
    val productColor: String,
    val barcode: String,
    val stock: Int,
    val quantity: Int
)