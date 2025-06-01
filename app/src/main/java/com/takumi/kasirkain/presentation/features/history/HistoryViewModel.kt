package com.takumi.kasirkain.presentation.features.history

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
import com.takumi.kasirkain.presentation.common.state.UiEvent
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.common.state.toErrorMessage
import com.takumi.kasirkain.presentation.util.PrinterManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val printReceiptUseCase: PrintReceiptUseCase,
    private val printerManager: PrinterManager
) : ViewModel() {

    // UI States
    private val _transactions = MutableStateFlow<UiState<List<GroupedTransaction>>>(UiState.Idle)
    val transactions = _transactions.asStateFlow()

    private val _transactionDetail = MutableStateFlow<UiState<TransactionHeader>>(UiState.Idle)
    val transactionDetail = _transactionDetail.asStateFlow()

    private val _printState = MutableStateFlow<UiState<Boolean>>(UiState.Idle)
    val printState = _printState.asStateFlow()

    private val _printers = MutableStateFlow<List<PrinterManager.BluetoothPrinter>>(emptyList())
    val printers = _printers.asStateFlow()

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            getTransactions()
        }
    }

    fun getTransactions() {
        _transactions.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = getTransactionsUseCase()
                _transactions.value = UiState.Success(result)
            } catch (e: Exception) {
                _transactions.value = UiState.Error(e.toErrorMessage())
                _uiEvents.send(UiEvent.ShowToast(e.toErrorMessage()))
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
                _transactionDetail.value = UiState.Error(e.toErrorMessage())
                _uiEvents.send(UiEvent.ShowToast(e.toErrorMessage()))
            }
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun loadPrinters() {
        viewModelScope.launch {
            try {
                _printers.value = printerManager.getAvailablePrinters()
            } catch (e: Exception) {
                _uiEvents.send(UiEvent.ShowToast("Gagal memuat printer: ${e.toErrorMessage()}"))
            }
        }
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
                _uiEvents.send(UiEvent.ShowToast("Struk berhasil dicetak"))
            } catch (e: Exception) {
                _printState.value = UiState.Error(e.toErrorMessage())
                _uiEvents.send(UiEvent.ShowToast("Gagal mencetak: ${e.toErrorMessage()}"))
            }
        }
    }

    fun resetPrintState() {
        _printState.value = UiState.Idle
    }
}