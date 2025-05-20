package com.takumi.kasirkain.presentation.features.main.home

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.takumi.kasirkain.R
import com.takumi.kasirkain.domain.model.Category
import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.domain.model.ProductVariant
import com.takumi.kasirkain.presentation.common.components.AppLazyColumn
import com.takumi.kasirkain.presentation.common.components.AppLazyRow
import com.takumi.kasirkain.presentation.common.components.AppLazyVerticalGrid
import com.takumi.kasirkain.presentation.common.components.ErrorDialog
import com.takumi.kasirkain.presentation.common.components.LoadingDialog
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.features.main.home.components.CategoryCard
import com.takumi.kasirkain.presentation.features.main.home.components.LoadingTabletProduct
import com.takumi.kasirkain.presentation.features.main.home.components.ProductVariantCard
import com.takumi.kasirkain.presentation.features.main.home.components.SearchTextField
import com.takumi.kasirkain.presentation.features.main.home.components.TabletProductCard
import com.takumi.kasirkain.presentation.features.scan.components.AfterScanDialog
import com.takumi.kasirkain.presentation.features.scan.components.ScannerBottomSheet
import com.takumi.kasirkain.presentation.navigation.RequestCameraPermission
import com.takumi.kasirkain.presentation.theme.Black
import com.takumi.kasirkain.presentation.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    scrollBehavior: TopAppBarScrollBehavior
) {
    val context = LocalContext.current

    val products by viewModel.products.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val productVariants by viewModel.productVariants.collectAsStateWithLifecycle()
    val productVariant by viewModel.productVariant.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()

    var showRequestPermission by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showScanner by remember { mutableStateOf(false) }
    var showDeniedDialog by remember { mutableStateOf(false) }
    var scanResult by remember { mutableStateOf("") }

    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var selectedCategory by rememberSaveable { mutableIntStateOf(0) }
    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getCategories()
    }

    LaunchedEffect(selectedCategory) {
        if (selectedCategory > 0) viewModel.getProduct(selectedCategory.toString(), query)
        else viewModel.getProduct(query)
    }

    LaunchedEffect(query) {
        viewModel.getProduct(selectedCategory.toString(), query)
    }

    LaunchedEffect(selectedProduct) {
        selectedProduct?.let {
            viewModel.getProductVariants(it.id)
        }
    }

    Row(
        modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.weight(2f),
        ) {
            TabletHomeHeaderSection(
                query = query,
                onQueryChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                categories = categories,
                onSelectedCategory = { selectedCategory = it },
                selectedCategory = selectedCategory,
                onBarcodeScanning = { showRequestPermission = true }
            )
            TabletHomeContentSection(
                scrollBehavior = scrollBehavior,
                products = products,
                context = context,
                onProductClick = { selectedProduct = it }
            )
        }
        VerticalDivider(
            modifier = Modifier.shadow(
                elevation = 10.dp,
                spotColor = Black.copy(alpha = 0.3f),
                ambientColor = Black.copy(alpha = 0.3f)
            ),
            color = Color.Transparent
        )
        TabletHomeSelectedProduct(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(WindowInsets.statusBars.asPaddingValues()),
            selectedProduct = selectedProduct,
            productVariants = productVariants,
            context = context
        )
    }

    if (showRequestPermission) {
        RequestCameraPermission(
            onGranted = {
                showScanner = true
            },
            onDenied = {
                showDeniedDialog = true
            }
        )
    }

    if (showScanner) {
        showDeniedDialog = false
        ScannerBottomSheet(
            modifier = Modifier,
            sheetState = sheetState,
            onDismiss = {
                showRequestPermission = false
                showScanner = false
            },
            onBarcodeScanned = {
                scanResult = it
            }
        )
    }

    if (showDeniedDialog) {
        ErrorDialog(
            message = "Izin kamera diperlukan!"
        ) {
            showRequestPermission = false
            showDeniedDialog = false
        }
    }

    if (scanResult.isNotEmpty()) {
        productVariant.let { state ->
            when (state) {
                is UiState.Idle -> {}
                is UiState.Loading -> {
                    LoadingDialog()
                }

                is UiState.Success -> {
                    AfterScanDialog(
                        onDismissRequest = { scanResult = "" },
                        onAddToCart = { data ->
                            if (data != null) Log.d(
                                "Product Variant",
                                "Product Variant: $data"
                            )
                        },
                        product = state.data
                    )
                }

                is UiState.Error -> {
                    ErrorDialog(
                        message = state.message
                    ) { scanResult = "" }
                }
            }
        }
    }
}

@Composable
fun TabletHomeSelectedProduct(
    modifier: Modifier = Modifier,
    selectedProduct: Product?,
    productVariants: UiState<List<ProductVariant>>,
    context: Context
) {
    Column(
        modifier = modifier
    ) {
        if (selectedProduct == null) {
            Box(Modifier
                .fillMaxSize()
                .padding(horizontal = LocalSpacing.current.paddingMedium.dp)) {
                Text(
                    text = "Varian Produk",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.TopStart)
                )
                Text(
                    text = "Pilih produk terlebih dahulu",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            productVariants.let { state ->
                when (state) {
                    is UiState.Idle -> {}
                    is UiState.Loading -> {}
                    is UiState.Success<List<ProductVariant>> -> {
                        selectedProduct.let { product ->
                            Column(
                                Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = LocalSpacing.current.paddingMedium.dp),
                            ) {
                                Text(
                                    text = product.name,
                                    style = MaterialTheme.typography.titleLarge,
                                )
                                Spacer(modifier = Modifier.height(LocalSpacing.current.paddingSmall.dp))
                                AppLazyColumn(
                                    modifier = Modifier.weight(1f).fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingSmall.dp),
                                    contentPadding = PaddingValues(horizontal = LocalSpacing.current.paddingSmall.dp)
                                ) {
                                    items(state.data, key = {it.id}) { variant ->
                                        ProductVariantCard(
                                            modifier = Modifier
                                                .offset(
                                                    x = -LocalSpacing.current.paddingSmall.dp
                                                ),
                                            barcode = variant.barcode,
                                            size = variant.size,
                                            color = variant.color,
                                            stock = variant.stock,
                                            isChecked = false
                                        ) { }
                                    }
                                }
                            }
                        }
                    }
                    is UiState.Error -> {
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletHomeHeaderSection(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    categories: UiState<List<Category>>,
    selectedCategory: Int,
    onSelectedCategory: (Int) -> Unit,
    onBarcodeScanning: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = LocalSpacing.current.paddingMedium.dp),
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingSmall.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = LocalSpacing.current.paddingMedium.dp)
        ) {
            Text(
                text = "Hi, Cashier",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "17:00 WIB",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LocalSpacing.current.paddingMedium.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingSmall.dp),
        ) {
            SearchTextField(
                value = query,
                onValueChange = onQueryChange,
                hint = "Cari produk...",
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    onBarcodeScanning()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_scanner),
                    contentDescription = null,
                    modifier = Modifier.size(LocalSpacing.current.smallIconSize.dp),
                )
            }
        }
        AppLazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = LocalSpacing.current.paddingMedium.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.let { state ->
                when (state) {
                    is UiState.Idle -> {}
                    is UiState.Loading -> {
                        items(3) { LoadingCategory() }
                    }

                    is UiState.Success<List<Category>> -> {
                        items(state.data, key = { it.id }) { category ->
                            CategoryCard(
                                onClick = { onSelectedCategory(category.id) },
                                modifier = Modifier,
                                text = category.name,
                                selected = selectedCategory == category.id
                            )
                        }
                    }

                    is UiState.Error -> {
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(LocalSpacing.current.paddingSmall.dp))
        HorizontalDivider(
            modifier = Modifier.shadow(
                elevation = 5.dp,
                spotColor = Black.copy(alpha = 0.3f),
                ambientColor = Black.copy(alpha = 0.3f)
            ),
            color = Color.Transparent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletHomeContentSection(
    scrollBehavior: TopAppBarScrollBehavior,
    products: UiState<List<Product>>,
    context: Context,
    onProductClick: (Product) -> Unit
) {
    AppLazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingMedium.dp),
        horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingMedium.dp),
        contentPadding = PaddingValues(
            LocalSpacing.current.paddingMedium.dp
        )
    ) {
        products.let { state ->
            when (state) {
                is UiState.Idle -> {}
                is UiState.Loading -> {
                    items(5) { LoadingTabletProduct() }
                }

                is UiState.Success<List<Product>> -> {
                    items(state.data, key = { it.id }) { product ->
                        TabletProductCard(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.large)
                                .clickable {
                                    onProductClick(product)
                                },
                            name = product.name,
                            price = product.price,
                            variantCount = product.variantCount,
                            imageName = product.image
                        )
                    }
                }

                is UiState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}