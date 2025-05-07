package com.takumi.kasirkain.presentation.features.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takumi.kasirkain.domain.model.Category
import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.domain.usecase.GetCategoriesUseCase
import com.takumi.kasirkain.domain.usecase.GetProductUseCase
import com.takumi.kasirkain.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
): ViewModel() {
    private val _products: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    private val _categories: MutableStateFlow<List<Category>> = MutableStateFlow(emptyList())

    val products = _products.asStateFlow()
    val categories = _categories.asStateFlow()

    init {
        getCategories()
        getProduct()
    }

    fun getProduct(category: String? = null, search: String? = null) {
        viewModelScope.launch {
            var categoryQuery = if (category == "" || category == "0") null else category
            var searchQuery = if (search == "") null else search

            try {
                val response = getProductUseCase(categoryQuery, searchQuery)
                _products.value = response
            } catch (e: Exception) {
                _products.value = emptyList()
            }
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            try {
                val response = getCategoriesUseCase()
                Log.d("TAG", "getCategories: $response")
                val categories = mutableListOf<Category>()
                categories.add(Category(id = 0, name = "Semua"))
                categories.addAll(response)
                _categories.value = categories
            } catch (e: Exception) {
                Log.d("TAG", "getCategories: ${e.message}")
                _categories.value = emptyList()
            }
        }
    }
}