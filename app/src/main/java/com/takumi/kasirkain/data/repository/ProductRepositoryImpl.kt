package com.takumi.kasirkain.data.repository

import com.takumi.kasirkain.data.local.LocalDataSource
import com.takumi.kasirkain.data.remote.RemoteDataSource
import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
): ProductRepository {
    override suspend fun getProduct(
        category: String?,
        search: String?
    ): List<Product> {
        val response = remoteDataSource.getProduct(category, search)
        return response.map { it.toDomain() }
    }
}