package com.takumi.kasirkain.presentation.features.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takumi.kasirkain.domain.model.ProductDetail
import com.takumi.kasirkain.domain.model.ProductVariant
import com.takumi.kasirkain.domain.usecase.GetProductVariantDetailUseCase
import com.takumi.kasirkain.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val getProductVariantDetailUseCase: GetProductVariantDetailUseCase
): ViewModel() {
    private val _productVariant: MutableStateFlow<UiState<ProductDetail>> = MutableStateFlow(UiState.Idle)

    val productVariant = _productVariant.asStateFlow()

    fun getProductVariantDetail(barcode: String) {
        viewModelScope.launch {
            _productVariant.value = UiState.Loading
            try {
                if (barcode.isEmpty()) return@launch
                val response = getProductVariantDetailUseCase(barcode)
                _productVariant.value = UiState.Success(response)
            } catch (e: Exception) {
                _productVariant.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}