package com.takumi.kasirkain.presentation.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takumi.kasirkain.data.local.mapper.toCartItem
import com.takumi.kasirkain.domain.model.Category
import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.domain.model.ProductDetail
import com.takumi.kasirkain.domain.model.ProductVariant
import com.takumi.kasirkain.domain.model.User
import com.takumi.kasirkain.domain.usecase.AddCartItemUseCase
import com.takumi.kasirkain.domain.usecase.GetCategoriesUseCase
import com.takumi.kasirkain.domain.usecase.GetProductUseCase
import com.takumi.kasirkain.domain.usecase.GetProductVariantByBarcodeUseCase
import com.takumi.kasirkain.domain.usecase.GetProductVariantsUseCase
import com.takumi.kasirkain.domain.usecase.GetUserProfileUseCase
import com.takumi.kasirkain.domain.usecase.RemoveTokenUseCase
import com.takumi.kasirkain.presentation.common.state.UiEvent
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.common.state.toErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductVariantsUseCase: GetProductVariantsUseCase,
    private val getProductVariantByBarcodeUseCase: GetProductVariantByBarcodeUseCase,
    private val addCartItemUseCase: AddCartItemUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val removeTokenUseCase: RemoveTokenUseCase
) : ViewModel() {

    // UI States
    private val _products = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val products: StateFlow<UiState<List<Product>>> = _products.asStateFlow()

    private val _categories = MutableStateFlow<UiState<List<Category>>>(UiState.Idle)
    val categories: StateFlow<UiState<List<Category>>> = _categories.asStateFlow()

    private val _productVariants = MutableStateFlow<UiState<List<ProductVariant>>>(UiState.Idle)
    val productVariants: StateFlow<UiState<List<ProductVariant>>> = _productVariants.asStateFlow()

    private val _productVariant = MutableStateFlow<UiState<ProductDetail>>(UiState.Idle)
    val productVariant: StateFlow<UiState<ProductDetail>> = _productVariant.asStateFlow()

    private val _userProfile = MutableStateFlow<UiState<User>>(UiState.Idle)
    val userProfile: StateFlow<UiState<User>> = _userProfile.asStateFlow()

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    // Initial loading
    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            launch { getCategories() }
            launch { getUserProfile() }
            launch { getProduct() }
        }
    }

    fun getProduct(category: Int? = null, search: String? = null) {
        _products.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = getProductUseCase(
                    category?.toString(),
                    search?.takeUnless { it.isEmpty() }
                )
                _products.value = UiState.Success(response)
            } catch (e: Exception) {
                _products.value = UiState.Error(e.toErrorMessage())
                _uiEvents.send(UiEvent.ShowToast(e.toErrorMessage()))
            }
        }
    }

    fun getCategories() {
        _categories.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = getCategoriesUseCase()
                val allCategories = listOf(Category(id = 0, name = "Semua")) + response
                _categories.value = UiState.Success(allCategories)
            } catch (e: Exception) {
                _categories.value = UiState.Error(e.toErrorMessage())
                _uiEvents.send(UiEvent.ShowToast(e.toErrorMessage()))
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
                _productVariants.value = UiState.Error(e.toErrorMessage())
                _uiEvents.send(UiEvent.ShowToast(e.toErrorMessage()))
            }
        }
    }

    fun getProductVariantDetail(barcode: String) {
        _productVariant.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = getProductVariantByBarcodeUseCase(barcode)
                _productVariant.value = UiState.Success(response)
            } catch (e: Exception) {
                _productVariant.value = UiState.Error(e.toErrorMessage())
                _uiEvents.send(UiEvent.ShowToast(e.toErrorMessage()))
            }
        }
    }

    fun resetProductVariantState() {
        _productVariant.value = UiState.Idle
    }

    fun addProductToCart(product: Product, productVariant: ProductVariant) {
        viewModelScope.launch {
            try {
                addCartItemUseCase(product.toCartItem(productVariant))
                _uiEvents.send(UiEvent.ShowToast("Produk ditambahkan ke keranjang"))
            } catch (e: Exception) {
                _uiEvents.send(UiEvent.ShowToast("Gagal menambahkan ke keranjang: ${e.toErrorMessage()}"))
            }
        }
    }

    private fun getUserProfile() {
        _userProfile.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = getUserProfileUseCase()
                _userProfile.value = UiState.Success(response)
            } catch (e: Exception) {
                _userProfile.value = UiState.Error(e.toErrorMessage())
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            removeTokenUseCase()
        }
    }
}