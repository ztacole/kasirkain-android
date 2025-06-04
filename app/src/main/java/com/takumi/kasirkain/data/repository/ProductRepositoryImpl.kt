package com.takumi.kasirkain.data.repository

import android.util.Log
import com.takumi.kasirkain.data.local.LocalDataSource
import com.takumi.kasirkain.data.remote.RemoteDataSource
import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.domain.model.ProductDetail
import com.takumi.kasirkain.domain.model.ProductVariant
import com.takumi.kasirkain.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
): ProductRepository {
    override suspend fun getProduct(
        page: Int?,
        perPage: Int?,
        category: String?,
        search: String?
    ): List<Product> {
        val response = remoteDataSource.getProduct(page, perPage, category, search)
        return response.map { it.toDomain() }
    }

    override suspend fun getProductVariantDetail(barcode: String): ProductDetail {
        val response = remoteDataSource.getProductVariantDetail(barcode)
        return response.toDomain()
    }

    override suspend fun getProductVariants(id: Int): List<ProductVariant> {
        val response = remoteDataSource.getProductVariants(id)
        return response.map { it.toDomain() }
    }
}