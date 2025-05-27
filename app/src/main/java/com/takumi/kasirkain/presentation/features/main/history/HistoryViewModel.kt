package com.takumi.kasirkain.presentation.features.main.history

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takumi.kasirkain.domain.model.GroupedTransaction
import com.takumi.kasirkain.domain.model.TransactionHeader
import com.takumi.kasirkain.domain.usecase.GetTransactionByIdUseCase
import com.takumi.kasirkain.domain.usecase.GetTransactionsUseCase
import com.takumi.kasirkain.domain.usecase.PrintReceiptUseCase
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.util.PrinterManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val printReceiptUseCase: PrintReceiptUseCase,
    private val printerManager: PrinterManager
): ViewModel() {
    private val _transactions: MutableStateFlow<UiState<List<GroupedTransaction>>> = MutableStateFlow(UiState.Idle)
    private val _transactionDetail: MutableStateFlow<UiState<TransactionHeader>> = MutableStateFlow(UiState.Idle)
    private val _printState: MutableStateFlow<UiState<Boolean>> = MutableStateFlow(UiState.Idle)
    private val _printers: MutableStateFlow<List<PrinterManager.BluetoothPrinter>> =
        MutableStateFlow(emptyList())

    val transaction = _transactions.asStateFlow()
    val transactionDetail = _transactionDetail.asStateFlow()
    val printState = _printState.asStateFlow()
    val printers = _printers.asStateFlow()

    init {
        getTransactions()
    }

    private fun getTransactions() {
        _transactions.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = getTransactionsUseCase()
                _transactions.value = UiState.Success(result)
            } catch (e: Exception) {
                _transactions.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun getTransactionById(id: Int) {
        if (id == -1) return
        _transactionDetail.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = getTransactionByIdUseCase(id)
                _transactionDetail.value = UiState.Success(result)
            } catch (e: Exception) {
                _transactionDetail.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun resetPrintState() {
        _printState.value = UiState.Idle
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
        context: Context,
        transactionId: Int
    ) {
        viewModelScope.launch {
            _printState.value = UiState.Loading
            try {
                printReceiptUseCase(printer, paymentType, cashReceived, context, transactionId)
                _printState.value = UiState.Success(true)
            } catch (e: Exception) {
                _printState.value = UiState.Error(e.message ?: "Gagal mencetak")
            }
        }
    }
}