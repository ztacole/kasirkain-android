package com.takumi.kasirkain.presentation.features.checkout

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.takumi.kasirkain.R
import com.takumi.kasirkain.presentation.common.components.AppButton
import com.takumi.kasirkain.presentation.common.components.AppDialog
import com.takumi.kasirkain.presentation.common.components.AppDropdown
import com.takumi.kasirkain.presentation.common.components.AppLazyColumn
import com.takumi.kasirkain.presentation.common.components.AppOutlinedButton
import com.takumi.kasirkain.presentation.common.components.ConfirmationDialog
import com.takumi.kasirkain.presentation.common.components.LoadingDialog
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.features.checkout.components.RupiahTextField
import com.takumi.kasirkain.presentation.theme.LocalSpacing
import com.takumi.kasirkain.presentation.util.CoreFunction
import com.takumi.kasirkain.presentation.util.PrinterManager

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckoutTabletScreen(
    modifier: Modifier = Modifier,
    viewModel: CheckoutViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onCheckout: () -> Unit
) {
    val totalPayment by viewModel.totalPayment.collectAsState()
    val checkoutState by viewModel.checkoutState.collectAsState()
    val printState by viewModel.printState.collectAsStateWithLifecycle()
    val printers by viewModel.printers.collectAsState()

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val paymentTypes = listOf("QRIS", "Cash")
    var selectedPaymentType by remember { mutableStateOf("Metode pembayaran") }
    var paymentAmount by remember { mutableLongStateOf(0L) }
    val change = paymentAmount - totalPayment
    var selectedPrinter by remember { mutableStateOf<PrinterManager.BluetoothPrinter?>(null) }

    var showBackDialog by remember { mutableStateOf(false) }
    var showPayDialog by remember { mutableStateOf(false) }
    var showPrinterDialog by remember { mutableStateOf(false) }

    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN
        )
    } else {
        listOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )
    }

    val permissionState = rememberMultiplePermissionsState(permissions = permissions)
    var requestPermission by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) return@LaunchedEffect
        viewModel.loadPrinters()
    }

    LaunchedEffect(requestPermission) {
        if (requestPermission) {
            permissionState.launchMultiplePermissionRequest()
            requestPermission = false
        }
    }

    Row(
        modifier = modifier.fillMaxSize()
    ) {
        QRISPanel(Modifier.weight(2f))
        CheckoutPanel(
            modifier = Modifier.weight(1f),
            scrollState = scrollState,
            totalPayment = totalPayment,
            paymentTypes = paymentTypes,
            selectedPaymentType = selectedPaymentType,
            onPaymentTypeChange = { selectedPaymentType = it },
            paymentAmount = paymentAmount,
            onPaymentAmountChange = { paymentAmount = it },
            change = change,
            onNavigateBack = {
                if (paymentAmount != 0L && selectedPaymentType != "Metode pembayaran") {
                    showBackDialog = true
                } else onNavigateBack()
            },
            onCheckout = {
                showPayDialog = true
            }
        )

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

    when (val state = checkoutState) {
        is UiState.Idle -> {}
        is UiState.Loading -> {
            LoadingDialog("Memproses pembayaran...")
        }
        is UiState.Success<Int> -> {
            if (permissionState.allPermissionsGranted) {
                showPrinterDialog = true
            }
            else if (permissionState.shouldShowRationale){
                requestPermission = true
                Toast.makeText(context, "Aplikasi memerlukan akses Bluetooth untuk mencetak struk", Toast.LENGTH_SHORT).show()
            } else {
                onCheckout()
            }
            viewModel.resetCheckoutState()
        }
        is UiState.Error -> {
            AppDialog(
                message = state.message
            ) {
                viewModel.resetCheckoutState()
            }
        }
    }

    when (val state = printState) {
        is UiState.Idle -> {}
        is UiState.Loading -> {
            LoadingDialog(
                text = "Mencetak..."
            )
        }
        is UiState.Success<Boolean> -> {
            AppDialog(
                title = "Berhasil!",
                message = "Struk berhasil dicetak"
            ) {
                viewModel.resetPrintState()
                onCheckout()
            }
        }
        is UiState.Error -> {
            AppDialog(
                message = state.message
            ) {
                viewModel.resetPrintState()
                onCheckout()
            }
        }
    }

    if (showPrinterDialog) {
        ConfirmationDialog(
            title = "Pilih Printer",
            onDismiss = {
                showPrinterDialog = false
                onCheckout()
            },
            enableConfirmButton = selectedPrinter != null,
            onConfirm = {
                viewModel.printReceipt(
                    printer = selectedPrinter!!,
                    paymentType = selectedPaymentType,
                    cashReceived = paymentAmount,
                    context = context
                )
                showPrinterDialog = false
            },
            content = {
                AppLazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(printers) { printer ->
                        Text(
                            text = printer.name,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = LocalSpacing.current.paddingMedium.dp)
                                .background(
                                    color = if (selectedPrinter != printer) Color.White else MaterialTheme.colorScheme.tertiary,
                                    shape = MaterialTheme.shapes.small
                                )
                                .clickable {
                                    selectedPrinter = printer
                                }
                        )
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutPanel(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    totalPayment: Int,
    paymentTypes: List<String>,
    selectedPaymentType: String,
    onPaymentTypeChange: (String) -> Unit,
    paymentAmount: Long,
    onPaymentAmountChange: (Long) -> Unit,
    change: Long,
    onNavigateBack: () -> Unit,
    onCheckout: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = LocalSpacing.current.paddingMedium.dp)
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
                .verticalScroll(scrollState)
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
                onPaymentTypeChange(it)
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
                    onValueChange = { onPaymentAmountChange(it) }
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
                .padding(
                    bottom = LocalSpacing.current.paddingMedium.dp,
                    top = LocalSpacing.current.paddingSmall.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingSmall.dp)
        ) {
            AppOutlinedButton(
                text = "Kembali",
                shape = CircleShape,
                onClick = onNavigateBack,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            AppButton(
                text = "Selesai & Cetak",
                shape = CircleShape,
                enabled = change >= 0 && selectedPaymentType != "Metode pembayaran",
                onClick = onCheckout,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun QRISPanel(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.fashion24_qris),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}