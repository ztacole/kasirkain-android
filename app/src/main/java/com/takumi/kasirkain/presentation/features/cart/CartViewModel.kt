package com.takumi.kasirkain.presentation.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takumi.kasirkain.domain.model.CartItem
import com.takumi.kasirkain.domain.usecase.DeleteCartItemUseCase
import com.takumi.kasirkain.domain.usecase.GetCartItemsUseCase
import com.takumi.kasirkain.domain.usecase.UpdateCartItemQuantityUseCase
import com.takumi.kasirkain.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase
): ViewModel() {
    private val _cartItems: MutableStateFlow<UiState<List<CartItem>>> = MutableStateFlow(UiState.Idle)

    val cartItems = _cartItems.asStateFlow()

    fun getCartItems() {
        UiState.Loading
        viewModelScope.launch {
            try {
                val result = getCartItemsUseCase()
                _cartItems.value = UiState.Success(result)
            } catch (e: Exception) {
                _cartItems.value = UiState.Error(e.message ?: "Unknow Error")
            }
        }
    }

    fun increaseCartItemQuantity(item: CartItem) {
        viewModelScope.launch {
            if (item.quantity < item.stock) {
                updateCartItemQuantityUseCase(item.copy(quantity = item.quantity + 1))
            }
        }
    }

    fun decreaseCartItemQuantity(item: CartItem) {
        viewModelScope.launch {
            if (item.quantity > 1) {
                updateCartItemQuantityUseCase(item.copy(quantity = item.quantity - 1))
            } else {
                deleteCartItemUseCase(item)
            }
        }
    }
}