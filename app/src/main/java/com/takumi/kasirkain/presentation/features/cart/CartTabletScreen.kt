package com.takumi.kasirkain.presentation.features.cart

import android.widget.Space
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.takumi.kasirkain.domain.model.CartItem
import com.takumi.kasirkain.presentation.common.components.AppLazyColumn
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.features.cart.components.CartItemCard
import com.takumi.kasirkain.presentation.theme.Black
import com.takumi.kasirkain.presentation.theme.LocalSpacing
import com.takumi.kasirkain.presentation.util.CoreFunction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CartTabletScreen(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = hiltViewModel(),
    onNavigateToCheckoutScreen: () -> Unit
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
            items(cartItems, key = { it.id }) { item ->
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
        VerticalDivider(
            modifier = Modifier.shadow(
                elevation = 10.dp,
                spotColor = Black.copy(alpha = 0.3f),
                ambientColor = Black.copy(alpha = 0.3f)
            ),
            color = Color.Transparent
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = LocalSpacing.current.paddingMedium.dp)
                .padding(WindowInsets.statusBars.asPaddingValues())
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
                            text = CoreFunction.formatToRupiah(item.quantity * item.productPrice),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth().zIndex(5f),
                verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingSmall.dp)
            ) {
                Text(
                    text = "Total: ${CoreFunction.formatToRupiah(cartItems.sumOf { it.quantity * it.productPrice })}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Start)
                )
                FloatingActionButton(
                    onClick = {
                        onNavigateToCheckoutScreen()
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = LocalSpacing.current.paddingMedium.dp),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(LocalSpacing.current.paddingMedium.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Proses Transaksi",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}