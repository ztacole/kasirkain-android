package com.takumi.kasirkain.presentation.features.history

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.takumi.kasirkain.domain.model.GroupedTransaction
import com.takumi.kasirkain.domain.model.TransactionHeader
import com.takumi.kasirkain.presentation.common.components.AppButton
import com.takumi.kasirkain.presentation.common.components.AppLazyColumn
import com.takumi.kasirkain.presentation.common.components.ConfirmationDialog
import com.takumi.kasirkain.presentation.common.components.AppDialog
import com.takumi.kasirkain.presentation.common.components.LoadingDialog
import com.takumi.kasirkain.presentation.common.state.UiEvent
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.features.history.components.TransactionCard
import com.takumi.kasirkain.presentation.theme.Black
import com.takumi.kasirkain.presentation.theme.LocalSpacing
import com.takumi.kasirkain.presentation.util.CoreFunction
import com.takumi.kasirkain.presentation.util.PrinterManager

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun HistoryTabletScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
    scrollBehavior: TopAppBarScrollBehavior
) {
    val context = LocalContext.current

    // States
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val transactionDetail by viewModel.transactionDetail.collectAsStateWithLifecycle()
    val printState by viewModel.printState.collectAsStateWithLifecycle()
    val printers by viewModel.printers.collectAsStateWithLifecycle()

    // Local states
    var selectedPrinter by remember { mutableStateOf<PrinterManager.BluetoothPrinter?>(null) }
    var selectedTransactionId by rememberSaveable { mutableIntStateOf(-1) }
    var paymentType by rememberSaveable { mutableStateOf("") }
    var cashReceived by rememberSaveable { mutableLongStateOf(0L) }

    // Dialog states
    var showPrinterDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Permissions
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

    val permissionState = rememberMultiplePermissionsState(permissions)

    // Handle UI events
    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Load printers when permissions are granted
    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) return@LaunchedEffect
            viewModel.loadPrinters()
        }
    }

    // Load transaction details when selection changes
    LaunchedEffect(selectedTransactionId) {
        viewModel.getTransactionById(selectedTransactionId)
    }

    when (printState) {
        is UiState.Success -> showSuccessDialog = true
        is UiState.Error -> showErrorDialog = true
        is UiState.Loading -> {
            LoadingDialog("Mencetak...")
        }
        is UiState.Idle -> {}
    }

    // Main layout
    Row(modifier.fillMaxSize()) {
        // Left panel - Transaction list
        TransactionListPanel(
            modifier = Modifier.weight(2f),
            transactions = transactions,
            scrollBehavior = scrollBehavior,
            selectedTransactionId = selectedTransactionId,
            onTransactionSelected = { id ->
                selectedTransactionId = id.takeIf { it != selectedTransactionId } ?: selectedTransactionId
            }
        )

        // Divider
        VerticalDivider(
            modifier = Modifier.shadow(
                elevation = 10.dp,
                spotColor = Black.copy(alpha = 0.3f),
                ambientColor = Black.copy(alpha = 0.3f)
            ),
            color = Color.Transparent
        )

        // Right panel - Transaction details
        TransactionDetailPanel(
            modifier = Modifier.weight(1f),
            transactionDetail = transactionDetail,
            isPrintEnabled = selectedTransactionId != -1,
            onPrintClick = {
                if (permissionState.allPermissionsGranted) {
                    showPrinterDialog = true
                } else {
                    permissionState.launchMultiplePermissionRequest()
                }
            },
            onPaymentTypeChanged = { paymentType = it },
            onCashReceivedChanged = { cashReceived = it }
        )
    }

    // Printer selection dialog
    if (showPrinterDialog) {
        PrinterSelectionDialog(
            printers = printers,
            selectedPrinter = selectedPrinter,
            onPrinterSelected = { selectedPrinter = it },
            onConfirm = {
                selectedPrinter?.let { printer ->
                    viewModel.printReceipt(
                        printer = printer,
                        paymentType = paymentType,
                        cashReceived = cashReceived,
                        context = context,
                        transactionId = selectedTransactionId
                    )
                }
                showPrinterDialog = false
            },
            onDismiss = { showPrinterDialog = false }
        )
    }

    // Print success dialog
    if (showSuccessDialog) {
        AppDialog(
            title = "Berhasil!",
            message = "Struk berhasil dicetak",
            onDismiss = {
                showSuccessDialog = false
                viewModel.resetPrintState()
            }
        )
    }

    // Print error dialog
    if (showErrorDialog) {
        AppDialog(
            title = "Gagal",
            message = (printState as? UiState.Error)?.message ?: "Terjadi kesalahan saat mencetak",
            onDismiss = {
                showErrorDialog = false
                viewModel.resetPrintState()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionListPanel(
    modifier: Modifier = Modifier,
    transactions: UiState<List<GroupedTransaction>>,
    scrollBehavior: TopAppBarScrollBehavior,
    selectedTransactionId: Int,
    onTransactionSelected: (Int) -> Unit
) {
    AppLazyColumn(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingLarge.dp),
        contentPadding = PaddingValues(vertical = LocalSpacing.current.paddingMedium.dp)
    ) {
        when (transactions) {
            is UiState.Loading -> items(2) { LoadingTransaction() }
            is UiState.Success -> {
                items(transactions.data) { transaction ->
                    TransactionCard(
                        date = transaction.date,
                        transactions = transaction.transactions,
                        selectedTransactionId = selectedTransactionId,
                        onSelected = onTransactionSelected
                    )
                }
            }
            is UiState.Error -> {
                item {
                    ErrorMessage(message = transactions.message)
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun TransactionDetailPanel(
    modifier: Modifier = Modifier,
    transactionDetail: UiState<TransactionHeader>,
    isPrintEnabled: Boolean,
    onPrintClick: () -> Unit,
    onPaymentTypeChanged: (String) -> Unit,
    onCashReceivedChanged: (Long) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(LocalSpacing.current.paddingMedium.dp),
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingMedium.dp)
    ) {
        Text(
            text = "Detail Transaksi",
            style = MaterialTheme.typography.titleLarge,
        )

        when (transactionDetail) {
            is UiState.Success<TransactionHeader> -> {
                AppLazyColumn(
                    Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(vertical = LocalSpacing.current.paddingSmall.dp)
                ) {
                    item {
                        ReceiptPreview(
                            data = transactionDetail.data,
                            fillPaymentTypeValue = onPaymentTypeChanged,
                            fillCashReceivedValue = onCashReceivedChanged,
                        )
                    }
                }
            }
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                ErrorMessage(message = transactionDetail.message)
            }
            else -> {
                Box(Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Pilih transaksi untuk melihat detail",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
        AppButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Cetak Struk",
            enabled = isPrintEnabled,
            onClick = onPrintClick,
            shape = CircleShape
        )
    }
}

@Composable
private fun PrinterSelectionDialog(
    printers: List<PrinterManager.BluetoothPrinter>,
    selectedPrinter: PrinterManager.BluetoothPrinter?,
    onPrinterSelected: (PrinterManager.BluetoothPrinter) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ConfirmationDialog(
        title = "Pilih Printer",
        onDismiss = onDismiss,
        enableConfirmButton = selectedPrinter != null,
        onConfirm = onConfirm,
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
                                color = if (selectedPrinter != printer) Color.White
                                else MaterialTheme.colorScheme.tertiary,
                                shape = MaterialTheme.shapes.small
                            )
                            .clickable { onPrinterSelected(printer) }
                    )
                }
            }
        }
    )
}

@Composable
private fun ReceiptPreview(
    data: TransactionHeader,
    fillPaymentTypeValue: (String) -> Unit,
    fillCashReceivedValue: (Long) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        fillPaymentTypeValue(data.paymentType)
        fillCashReceivedValue(data.cashReceived.toLong())

        val totalPriceBeforeDiscount = data.details.sumOf { it.product.price * it.quantity }.toLong()
        val totalPrice = data.details.sumOf { it.product.finalPrice * it.quantity}.toLong()

        Column(
            verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingSmall.dp),
            modifier = Modifier.padding(LocalSpacing.current.paddingMedium.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.fashion24_logo_b_w),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.3f)
                    .aspectRatio(1f)
            )
            Text(
                text = """SMK Negeri 24 Jakarta
                                            |Jl. Bambu Hitam No.3, RT.3/RW.1, Bambu Apus, Kec. Cipayung, Kota Jakarta Timur, Daerah Khusus Ibukota Jakarta 13890
                                        """.trimMargin(),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            DoubleLineHorizontalDivider()
            Text(
                text = "Kasir : ${data.user.username}",
                style = MaterialTheme.typography.bodyLarge
            )
            DoubleLineHorizontalDivider()
            Spacer(Modifier.height(LocalSpacing.current.paddingMedium.dp))
            data.details.forEach { detail ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingSmall.dp)
                ) {
                    Column(Modifier.weight(2f)) {
                        Text(
                            text = "${detail.quantity} ${detail.product.name}",
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1
                        )
                        Text(
                            text = "   Size: ${detail.product.variants[0].size} | Warna: ${detail.product.variants[0].color}",
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1
                        )
                        if (detail.product.discount > 0) {
                            Text(
                                text = "   Diskon:\n   Subtotal:",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    Text(
                        text = CoreFunction.currencyFormatter(detail.product.price.toLong()),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = CoreFunction.currencyFormatter((detail.product.price * detail.quantity).toLong()),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                        if (detail.product.discount > 0) {
                            Text(
                                text = "",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "${CoreFunction.currencyFormatter((detail.product.price - detail.product.finalPrice).toLong())}\n${CoreFunction.currencyFormatter((detail.product.finalPrice * detail.quantity).toLong())}",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(LocalSpacing.current.paddingMedium.dp))
            HorizontalDivider()
            TotalSection(
                text = "Total Item (${data.details.count()}) : ",
                number = totalPriceBeforeDiscount
            )
            TotalSection(
                text = "Total Disc. : ",
                number = totalPriceBeforeDiscount - totalPrice
            )
            TotalSection(
                text = "Total Belanja : ",
                number = totalPrice
            )
            TotalSection(
                text = "${data.paymentType} : ",
                number = data.cashReceived.toLong()
            )
            TotalSection(
                text = "Kembalian : ",
                number = data.changeReturned.toLong()
            )
            DoubleLineHorizontalDivider()
            Text(
                text = "Terima kasih telah berbelanja!\n${data.createdAt}",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = LocalSpacing.current.paddingLarge.dp)
            )
        }
    }
}

@Composable
fun TotalSection(text: String, number: Long) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = CoreFunction.rupiahFormatter(number),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun DoubleLineHorizontalDivider() {
    HorizontalDivider()
    HorizontalDivider()
}

@Composable
private fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
