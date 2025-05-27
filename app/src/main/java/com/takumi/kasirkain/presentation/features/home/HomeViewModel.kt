package com.takumi.kasirkain.presentation.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takumi.kasirkain.data.local.mapper.toCartItem
import com.takumi.kasirkain.domain.model.CartItem
import com.takumi.kasirkain.domain.model.Category
import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.domain.model.ProductDetail
import com.takumi.kasirkain.domain.model.ProductVariant
import com.takumi.kasirkain.domain.usecase.AddCartItemUseCase
import com.takumi.kasirkain.domain.usecase.GetCartItemsUseCase
import com.takumi.kasirkain.domain.usecase.GetCategoriesUseCase
import com.takumi.kasirkain.domain.usecase.GetProductUseCase
import com.takumi.kasirkain.domain.usecase.GetProductVariantByBarcodeUseCase
import com.takumi.kasirkain.domain.usecase.GetProductVariantsUseCase
import com.takumi.kasirkain.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductVariantsUseCase: GetProductVariantsUseCase,
    private val getProductVariantByBarcodeUseCase: GetProductVariantByBarcodeUseCase,
    private val addCartItemUseCase: AddCartItemUseCase
): ViewModel() {
    private val _products: MutableStateFlow<UiState<List<Product>>> = MutableStateFlow(UiState.Idle)
    private val _categories: MutableStateFlow<UiState<List<Category>>> = MutableStateFlow(UiState.Idle)
    private val _productVariants: MutableStateFlow<UiState<List<ProductVariant>>> =
        MutableStateFlow(UiState.Idle)
    private val _productVariant: MutableStateFlow<UiState<ProductDetail>> = MutableStateFlow(UiState.Idle)

    val products = _products.asStateFlow()
    val categories = _categories.asStateFlow()
    val productVariants = _productVariants.asStateFlow()
    val productVariant = _productVariant.asStateFlow()

    init {
        getCartItems()
    }

    private fun getCartItems() {
        viewModelScope.launch {

        }
    }

    fun getProduct(category: String? = null, search: String? = null) {
        _products.value = UiState.Loading
        viewModelScope.launch {
            var categoryQuery = if (category == "" || category == "0") null else category
            var searchQuery = if (search == "") null else search

            try {
                val response = getProductUseCase(categoryQuery, searchQuery)
                _products.value = UiState.Success(response)
            } catch (e: Exception) {
                _products.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun getCategories() {
        _categories.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = getCategoriesUseCase()
                val categories = mutableListOf<Category>()
                categories.add(Category(id = 0, name = "Semua"))
                categories.addAll(response)
                _categories.value = UiState.Success(categories)
            } catch (e: Exception) {
                _categories.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun getProductVariants(id: Int) {
        _productVariants.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = getProductVariantsUseCase(id)
                _productVariants.value = UiState.Success(response)
            } catch (e: Exception) {
                _productVariants.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun getProductVariantDetail(barcode: String) {
        viewModelScope.launch {
            _productVariant.value = UiState.Loading
            try {
                if (barcode.isEmpty()) return@launch
                val response = getProductVariantByBarcodeUseCase(barcode)
                _productVariant.value = UiState.Success(response)
            } catch (e: Exception) {
                _productVariant.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun addProductToCart(product: Product, productVariant: ProductVariant) {
        viewModelScope.launch {
            addCartItemUseCase(product.toCartItem(productVariant))
        }
    }
}