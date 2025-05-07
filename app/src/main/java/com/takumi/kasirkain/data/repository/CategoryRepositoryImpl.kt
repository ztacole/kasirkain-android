package com.takumi.kasirkain.data.repository

import com.takumi.kasirkain.data.remote.RemoteDataSource
import com.takumi.kasirkain.domain.model.Category
import com.takumi.kasirkain.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
): CategoryRepository {
    override suspend fun getCategories(): List<Category> {
        val response = remote.getCategories()
        return response.map { it.toDomain() }
    }

}