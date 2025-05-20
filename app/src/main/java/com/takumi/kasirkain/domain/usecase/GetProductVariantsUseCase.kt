package com.takumi.kasirkain.domain.usecase

import com.takumi.kasirkain.domain.model.ProductVariant
import com.takumi.kasirkain.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductVariantsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Int): List<ProductVariant> {
        return repository.getProductVariants(id)
    }
}