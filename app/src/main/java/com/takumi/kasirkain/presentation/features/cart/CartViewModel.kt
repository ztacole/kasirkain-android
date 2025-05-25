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
    private val _cartItems: MutableStateFlow<List<CartItem>> = MutableStateFlow(emptyList())

    val cartItems = _cartItems.asStateFlow()

    fun getCartItems() {
        UiState.Loading
        viewModelScope.launch {
            try {
                val result = getCartItemsUseCase()
                _cartItems.value = result
            } catch (e: Exception) {
                _cartItems.value = emptyList()
            }
        }
    }

    private fun updateItemInState(updatedItem: CartItem) {
        _cartItems.value = _cartItems.value.let { data ->
            data.map { item ->
                if (item.id == updatedItem.id) updatedItem else item
            }.filter { item ->
                item.quantity > 0
            }
        }
    }

    fun increaseCartItemQuantity(item: CartItem) {
        viewModelScope.launch {
            if (item.quantity < item.stock) {
                val updatedItem = item.copy(quantity = item.quantity + 1)
                updateCartItemQuantityUseCase(updatedItem)
                updateItemInState(updatedItem)
            }
        }
    }

    fun decreaseCartItemQuantity(item: CartItem) {
        viewModelScope.launch {
            val updatedItem = item.copy(quantity = item.quantity - 1)
            if (item.quantity > 1) {
                updateCartItemQuantityUseCase(updatedItem)
            } else {
                deleteCartItemUseCase(item)
            }
            updateItemInState(updatedItem)
        }
    }
}