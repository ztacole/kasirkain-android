package com.takumi.kasirkain.domain.usecase

import com.takumi.kasirkain.domain.model.ProductDetail
import com.takumi.kasirkain.domain.model.ProductVariant
import com.takumi.kasirkain.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductVariantDetailUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(barcode: String): ProductDetail {
        return repository.getProductVariantDetail(barcode)
    }
}