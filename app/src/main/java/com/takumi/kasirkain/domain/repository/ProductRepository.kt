package com.takumi.kasirkain.domain.repository

import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.domain.model.ProductDetail
import com.takumi.kasirkain.domain.model.ProductVariant

interface ProductRepository {
    suspend fun getProduct(category: String?, search: String?): List<Product>
    suspend fun getProductVariantDetail(barcode: String) : ProductDetail
}