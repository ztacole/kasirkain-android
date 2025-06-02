package com.takumi.kasirkain.presentation.features.checkout

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takumi.kasirkain.domain.model.CartItem
import com.takumi.kasirkain.domain.usecase.CheckoutUseCase
import com.takumi.kasirkain.domain.usecase.ClearCartUseCase
import com.takumi.kasirkain.domain.usecase.GetCartItemsUseCase
import com.takumi.kasirkain.domain.usecase.PrintReceiptUseCase
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.common.state.toErrorMessage
import com.takumi.kasirkain.presentation.util.PrinterManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val checkoutUseCase: CheckoutUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val printReceiptUseCase: PrintReceiptUseCase,
    private val printerManager: PrinterManager
): ViewModel() {
    private val _cartItems : MutableStateFlow<List<CartItem>> = MutableStateFlow(emptyList())
    private val _totalPayment : MutableStateFlow<Int> = MutableStateFlow(0)
    private val _checkoutState : MutableStateFlow<UiState<Int>> = MutableStateFlow(UiState.Idle)
    private val _printState: MutableStateFlow<UiState<Boolean>> = MutableStateFlow(UiState.Idle)
    private val _printers: MutableStateFlow<List<PrinterManager.BluetoothPrinter>> =
        MutableStateFlow(emptyList())

    val totalPayment = _totalPayment.asStateFlow()
    val checkoutState = _checkoutState.asStateFlow()
    val printState = _printState.asStateFlow()
    val printers = _printers.asStateFlow()

    init {
        getTotalPayment()
    }

    private fun getTotalPayment() {
        viewModelScope.launch {
            _cartItems.value = getCartItemsUseCase()
            _totalPayment.value = _cartItems.value.sumOf { it.quantity * it.productFinalPrice }
        }
    }

    fun checkoutTransaction(
        paymentType: String,
        cashReceived: Long,
        changeReturned: Long
    ) {
        _checkoutState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = checkoutUseCase(paymentType, cashReceived, changeReturned)

                clearCartUseCase()
                _checkoutState.value = UiState.Success(result.id)
            } catch (e: Exception) {
                _checkoutState.value = UiState.Error(e.toErrorMessage())
            }
        }
    }

    fun resetPrintState() {
        _printState.value = UiState.Idle
    }

    fun resetCheckoutState() {
        _checkoutState.value = UiState.Idle
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun loadPrinters() {
        _printers.value = printerManager.getAvailablePrinters()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun printReceipt(
        printer: PrinterManager.BluetoothPrinter,
        paymentType: String,
        cashReceived: Long,
        context: Context
    ) {
        viewModelScope.launch {
            _printState.value = UiState.Loading
            try {
                printReceiptUseCase(printer, paymentType, cashReceived, context)
                _printState.value = UiState.Success(true)
            } catch (e: Exception) {
                _printState.value = UiState.Error(e.message ?: "Gagal mencetak")
            }
        }
    }
}