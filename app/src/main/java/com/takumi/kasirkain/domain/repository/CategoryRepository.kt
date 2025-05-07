package com.takumi.kasirkain.domain.repository

import com.takumi.kasirkain.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
}