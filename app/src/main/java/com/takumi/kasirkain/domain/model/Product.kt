package com.takumi.kasirkain.domain.model

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val image: String,
    val category: Category,
    val variantCount: Int,
    val activeEvents: List<Event>,
    val discount: Int,
    val finalPrice: Int
)
