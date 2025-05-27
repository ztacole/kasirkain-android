package com.takumi.kasirkain.presentation.features.checkout

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.takumi.kasirkain.R
import com.takumi.kasirkain.presentation.common.components.AppButton
import com.takumi.kasirkain.presentation.common.components.AppDropdown
import com.takumi.kasirkain.presentation.common.components.AppOutlinedButton
import com.takumi.kasirkain.presentation.common.components.ConfirmationDialog
import com.takumi.kasirkain.presentation.common.components.AppDialog
import com.takumi.kasirkain.presentation.common.components.LoadingDialog
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.features.checkout.components.RupiahTextField
import com.takumi.kasirkain.presentation.theme.LocalSpacing
import com.takumi.kasirkain.presentation.util.CoreFunction

@Composable
fun CheckoutTabletScreen(
    modifier: Modifier = Modifier,
    viewModel: CheckoutViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onCheckout: () -> Unit
) {
    val totalPayment by viewModel.totalPayment.collectAsState()
    val checkoutState by viewModel.checkoutState.collectAsState()

    val context = LocalContext.current

    val paymentTypes = listOf("QRIS", "Cash")
    var selectedPaymentType by remember { mutableStateOf("Metode pembayaran") }
    var paymentAmount by remember { mutableLongStateOf(0L) }
    val change = paymentAmount - totalPayment

    var showBackDialog by remember { mutableStateOf(false) }
    var showPayDialog by remember { mutableStateOf(false) }

    when (val state = checkoutState) {
        is UiState.Idle -> {}
        is UiState.Loading -> {
            LoadingDialog("Memproses pembayaran...")
        }
        is UiState.Success<Int> -> {
            onCheckout()
        }
        is UiState.Error -> {
            AppDialog(
                message = state.message
            ) {}
        }
    }

    Row(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(2f)
        ) {
            Image(
                painter = painterResource(R.drawable.fashion24_qris),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = LocalSpacing.current.paddingMedium.dp)
                .padding(WindowInsets.statusBars.asPaddingValues()),
        ) {
            Text(
                text = "Pembayaran",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(LocalSpacing.current.paddingSmall.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Nominal yang harus dibayar :",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = CoreFunction.rupiahFormatter(totalPayment.toLong()),
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(Modifier.height(LocalSpacing.current.paddingLarge.dp))
                AppDropdown(
                    modifier = Modifier.fillMaxWidth(),
                    options = paymentTypes,
                    value = selectedPaymentType
                ) {
                    selectedPaymentType = it
                }
                Spacer(Modifier.height(LocalSpacing.current.paddingLarge.dp))
                Text(
                    text = "Nominal",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Rp ",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    RupiahTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = paymentAmount,
                        onValueChange = { paymentAmount = it }
                    )
                }
                Spacer(Modifier.height(LocalSpacing.current.paddingLarge.dp))

                if (change > 0) {
                    Column {
                        Text(
                            text = "Kembalian :",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                        Text(
                            text = CoreFunction.rupiahFormatter(change),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else if (change < 0) {
                    Column {
                        Text(
                            text = "Kurang Bayar :",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = CoreFunction.rupiahFormatter(-change),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = LocalSpacing.current.paddingMedium.dp),
                horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingSmall.dp)
            ) {
                AppOutlinedButton(
                    text = "Kembali",
                    shape = CircleShape,
                    onClick = { showBackDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                AppButton(
                    text = "Selesai & Cetak",
                    shape = CircleShape,
                    enabled = change >= 0 && selectedPaymentType != "Metode pembayaran",
                    onClick = {
                        showPayDialog = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }

        if (showBackDialog) {
            ConfirmationDialog(
                title = "Yakin ingin keluar?",
                onDismiss = { showBackDialog = false },
                onConfirm = {
                    onNavigateBack()
                },
                content = {
                    Text(
                        text = "Semua perubahan akan hilang\nKonfirmasi untuk keluar",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
        }

        if (showPayDialog) {
            ConfirmationDialog(
                title = "Konfirmasi Pembayaran",
                onDismiss = { showPayDialog = false },
                onConfirm = {
                    viewModel.checkoutTransaction(selectedPaymentType, paymentAmount, change)
                    showPayDialog = false
                },
                content = {
                    Text(
                        text = "Pastikan nominal pembayaran sudah benar\nTransaksi tidak dapat dibatalkan setelah dikonfirmasi",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    }
}