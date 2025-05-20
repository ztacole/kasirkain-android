package com.takumi.kasirkain.presentation.features.cart

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.domain.usecase.PrintReceiptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val printReceiptUseCase: PrintReceiptUseCase
): ViewModel() {
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun printReceipt(items: List<Product>, printerName: String) {
        printReceiptUseCase(items, printerName)
    }
}