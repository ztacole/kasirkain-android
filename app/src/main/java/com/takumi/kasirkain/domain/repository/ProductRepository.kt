package com.takumi.kasirkain.domain.repository

import com.takumi.kasirkain.domain.model.Product

interface ProductRepository {
    suspend fun getProduct(category: String?, search: String?): List<Product>
}