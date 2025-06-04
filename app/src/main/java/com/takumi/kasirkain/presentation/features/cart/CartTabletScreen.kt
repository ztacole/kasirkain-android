package com.takumi.kasirkain.presentation.features.cart

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.takumi.kasirkain.presentation.common.components.AppButton
import com.takumi.kasirkain.presentation.common.components.AppLazyColumn
import com.takumi.kasirkain.presentation.features.cart.components.CartItemCard
import com.takumi.kasirkain.presentation.theme.Black
import com.takumi.kasirkain.presentation.theme.LocalSpacing
import com.takumi.kasirkain.presentation.util.CoreFunction

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CartTabletScreen(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = hiltViewModel(),
    onNavigateToCheckoutScreen: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCartItems()
    }

    Row(
        modifier = modifier.fillMaxSize()
    ) {
        // Left Panel - Cart Item list
        AppLazyColumn(
            modifier = Modifier
                .weight(2f)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingSmall.dp),
            contentPadding = PaddingValues(LocalSpacing.current.paddingSmall.dp)
        ) {
            items(cartItems, key = { it.id }) { item ->
                CartItemCard(
                    modifier = Modifier,
                    name = item.productName,
                    imageName = item.productImage,
                    size = item.productSize,
                    color = item.productColor,
                    stock = item.stock,
                    quantity = item.quantity,
                    price = item.productPrice,
                    discount = item.productDiscount,
                    finalPrice = item.productFinalPrice,
                    onDecrease = {
                        viewModel.decreaseCartItemQuantity(item)
                    },
                    onIncrease = {
                        viewModel.increaseCartItemQuantity(item)
                    }
                )
            }
        }
        // Right Panel - Detail
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(LocalSpacing.current.paddingMedium.dp)
        ) {
            Text(
                text = "Transaksi",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(LocalSpacing.current.paddingSmall.dp))
            AppLazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingMedium.dp),
                contentPadding = PaddingValues(
                    horizontal = LocalSpacing.current.paddingSmall.dp,
                    vertical = LocalSpacing.current.paddingLarge.dp
                )
            ) {
                stickyHeader {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Item",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Subtotal",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                items(cartItems, key = {it.id}) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = item.productName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Jumlah: ${item.quantity}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Text(
                            text = CoreFunction.rupiahFormatter((item.quantity * item.productFinalPrice).toLong()),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(5f),
                verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingSmall.dp)
            ) {
                Text(
                    text = "Total: ${CoreFunction.rupiahFormatter(cartItems.sumOf { it.quantity * it.productFinalPrice }.toLong())}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Start)
                )
                AppButton(
                    text = "Proses Transaksi",
                    shape = CircleShape,
                    enabled = cartItems.isNotEmpty(),
                    onClick = {
                        onNavigateToCheckoutScreen()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}