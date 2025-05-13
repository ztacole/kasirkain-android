package com.takumi.kasirkain.presentation.features.main.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takumi.kasirkain.domain.model.Transaction
import com.takumi.kasirkain.domain.usecase.GetTransactionsUseCase
import com.takumi.kasirkain.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
): ViewModel() {
    private val _transactions: MutableStateFlow<UiState<List<Transaction>>> = MutableStateFlow(UiState.Idle)

    val transaction = _transactions.asStateFlow()

    init {
        getTransactions()
    }

    private fun getTransactions() {
        _transactions.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = getTransactionsUseCase()
                _transactions.value = UiState.Success(response)
            } catch (e: Exception) {
                _transactions.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}