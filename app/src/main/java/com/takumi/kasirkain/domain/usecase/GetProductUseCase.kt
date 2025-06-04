package com.takumi.kasirkain.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.takumi.kasirkain.data.ProductPagingSource
import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.domain.model.ProductVariant
import com.takumi.kasirkain.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(category: String?, search: String?): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 8,
                enablePlaceholders = false,
                initialLoadSize = 8,
                prefetchDistance = 4
            ),
            pagingSourceFactory = {
                ProductPagingSource(repository, category, search)
            }
        ).flow
    }
}