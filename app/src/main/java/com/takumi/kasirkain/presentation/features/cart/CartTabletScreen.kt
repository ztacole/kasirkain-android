package com.takumi.kasirkain.presentation.features.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.takumi.kasirkain.domain.model.CartItem
import com.takumi.kasirkain.presentation.common.components.AppLazyColumn
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.features.cart.components.CartItemCard
import com.takumi.kasirkain.presentation.theme.LocalSpacing

@Composable
fun CartTabletScreen(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = hiltViewModel()
) {
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCartItems()
    }

    Row(
        modifier = modifier.fillMaxSize()
    ) {
        AppLazyColumn(
            modifier = Modifier.weight(2f).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingSmall.dp),
            contentPadding = PaddingValues(LocalSpacing.current.paddingSmall.dp)
        ) {
            cartItems.let { state ->
                when (state) {
                    is UiState.Idle -> {}
                    is UiState.Loading -> {

                    }
                    is UiState.Success<List<CartItem>> -> {
                        items(state.data, key = {it.id}) { item ->
                            CartItemCard(
                                modifier = Modifier,
                                name = item.productName,
                                imageName = item.productImage,
                                barcode = item.barcode,
                                stock = item.stock,
                                quantity = item.quantity,
                                onDecrease = {
                                    viewModel.decreaseCartItemQuantity(item)
                                },
                                onIncrease = {
                                    viewModel.increaseCartItemQuantity(item)
                                }
                            )
                        }
                    }
                    is UiState.Error -> {
                        item { Text(state.message) }
                    }
                }
            }
        }
    }
}