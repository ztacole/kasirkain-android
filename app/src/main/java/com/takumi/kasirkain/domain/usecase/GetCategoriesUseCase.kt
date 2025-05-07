package com.takumi.kasirkain.domain.usecase

import com.takumi.kasirkain.domain.model.Category
import com.takumi.kasirkain.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): List<Category> {
        return repository.getCategories()
    }
}