package com.takumi.kasirkain.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.domain.repository.ProductRepository

class ProductPagingSource(
    private val repository: ProductRepository,
    private val category: String?,
    private val search: String?
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val page = params.key ?: 1
            val response = repository.getProduct(
                page = page,
                perPage = params.loadSize,
                category = category,
                search = search
            ).distinctBy { it.id }

            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}