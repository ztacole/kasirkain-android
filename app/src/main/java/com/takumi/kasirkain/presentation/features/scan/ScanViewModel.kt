package com.takumi.kasirkain.presentation.features.scan

import androidx.lifecycle.ViewModel
import com.takumi.kasirkain.domain.usecase.GetProductVariantByBarcodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val getProductVariantByBarcodeUseCase: GetProductVariantByBarcodeUseCase
): ViewModel() {



}