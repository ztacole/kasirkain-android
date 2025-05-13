package com.takumi.kasirkain.domain.usecase

import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.domain.model.ProductVariant
import com.takumi.kasirkain.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(category: String?, search: String?): List<Product> {
        return repository.getProduct(category, search)
    }
}