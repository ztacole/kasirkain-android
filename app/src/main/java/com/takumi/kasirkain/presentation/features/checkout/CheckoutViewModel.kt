package com.takumi.kasirkain.presentation.features.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takumi.kasirkain.domain.model.CartItem
import com.takumi.kasirkain.domain.usecase.CheckoutUseCase
import com.takumi.kasirkain.domain.usecase.ClearCartUseCase
import com.takumi.kasirkain.domain.usecase.GetCartItemsUseCase
import com.takumi.kasirkain.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val checkoutUseCase: CheckoutUseCase,
    private val clearCartUseCase: ClearCartUseCase
): ViewModel() {
    private val _cartItems : MutableStateFlow<List<CartItem>> = MutableStateFlow(emptyList())
    private val _totalPayment : MutableStateFlow<Int> = MutableStateFlow(0)
    private val _checkoutState : MutableStateFlow<UiState<Int>> = MutableStateFlow(UiState.Idle)

    val totalPayment = _totalPayment.asStateFlow()
    val checkoutState = _checkoutState.asStateFlow()

    init {
        getTotalPayment()
    }

    private fun getTotalPayment() {
        viewModelScope.launch {
            _cartItems.value = getCartItemsUseCase()
            _totalPayment.value = _cartItems.value.sumOf { it.quantity * it.productPrice }
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
                _checkoutState.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}