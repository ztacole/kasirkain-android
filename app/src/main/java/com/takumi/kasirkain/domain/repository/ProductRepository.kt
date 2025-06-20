package com.takumi.kasirkain.domain.repository

import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.domain.model.ProductDetail
import com.takumi.kasirkain.domain.model.ProductVariant

interface ProductRepository {
    suspend fun getProduct(page: Int?, perPage: Int?, category: String?, search: String?): List<Product>
    suspend fun getProductVariantDetail(barcode: String) : ProductDetail
    suspend fun getProductVariants(id: Int): List<ProductVariant>
}