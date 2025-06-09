package com.takumi.kasirkain.presentation.features.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.takumi.kasirkain.R
import com.takumi.kasirkain.domain.model.Category
import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.domain.model.ProductVariant
import com.takumi.kasirkain.domain.model.User
import com.takumi.kasirkain.presentation.common.components.AppButton
import com.takumi.kasirkain.presentation.common.components.AppDialog
import com.takumi.kasirkain.presentation.common.components.AppLazyColumn
import com.takumi.kasirkain.presentation.common.components.AppLazyRow
import com.takumi.kasirkain.presentation.common.components.AppLazyVerticalGrid
import com.takumi.kasirkain.presentation.common.components.ErrorMessage
import com.takumi.kasirkain.presentation.common.components.LoadingIndicator
import com.takumi.kasirkain.presentation.common.state.ScannerState
import com.takumi.kasirkain.presentation.common.state.UiEvent
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.features.home.components.CategoryCard
import com.takumi.kasirkain.presentation.features.home.components.LoadingTabletProduct
import com.takumi.kasirkain.presentation.features.home.components.ProductVariantCard
import com.takumi.kasirkain.presentation.features.home.components.SearchTextField
import com.takumi.kasirkain.presentation.features.home.components.ShowCurrentTime
import com.takumi.kasirkain.presentation.features.home.components.TabletProductCard
import com.takumi.kasirkain.presentation.features.scan.components.AfterScanDialog
import com.takumi.kasirkain.presentation.features.scan.components.ScannerBottomSheet
import com.takumi.kasirkain.presentation.navigation.RequestCameraPermission
import com.takumi.kasirkain.presentation.theme.Black
import kotlinx.coroutines.delay
import com.takumi.kasirkain.presentation.common.state.LoadState as AppLoadState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTabletScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToCart: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val context = LocalContext.current

    // Collect states
    val products = viewModel.products.collectAsLazyPagingItems()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val productVariants by viewModel.productVariants.collectAsStateWithLifecycle()
    val productVariant by viewModel.productVariant.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    // Local states
    val loadState = products.loadState
    var scannerState by remember { mutableStateOf<ScannerState>(ScannerState.Idle) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableIntStateOf(0) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    // Handle UI events
    LaunchedEffect(loadState) {
        snapshotFlow { loadState.refresh }
            .collect { state ->
                when(state) {
                    is LoadState.Loading -> {
                        viewModel.setLoadState(AppLoadState.Loading)
                    }
                    is LoadState.Error -> {
                        viewModel.setLoadState(AppLoadState.Error(state.error.localizedMessage.orEmpty()))
                    }
                    else -> {
                        viewModel.setLoadState(AppLoadState.NotLoading)
                    }
                }
            }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Effects
    LaunchedEffect(selectedCategory) {
        products.refresh()
        viewModel.getProduct(
            category = selectedCategory.takeIf { it > 0 },
            search = searchQuery.takeUnless { it.isEmpty() }
        )
    }

    LaunchedEffect(searchQuery) {
        products.refresh()
        delay(300)
        viewModel.getProduct(
            category = selectedCategory.takeIf { it > 0 },
            search = searchQuery.takeUnless { it.isEmpty() }
        )
    }

    LaunchedEffect(selectedProduct) {
        selectedProduct?.let { product ->
            viewModel.getProductVariants(product.id)
        }
    }

    // Scanner handling
    when (scannerState) {
        ScannerState.Idle -> {}
        ScannerState.RequestPermission -> {
            RequestCameraPermission(
                onGranted = { scannerState = ScannerState.Active },
                onDenied = { scannerState = ScannerState.PermissionDenied }
            )
        }
        ScannerState.Active -> {
            ScannerBottomSheet(
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                onDismiss = { scannerState = ScannerState.Idle },
                onBarcodeScanned = { barcode ->
                    scannerState = ScannerState.Idle
                    viewModel.getProductVariantDetail(barcode)
                }
            )
        }
        ScannerState.PermissionDenied -> {
            AppDialog(
                message = "Izin kamera diperlukan untuk memindai barcode!",
                onDismiss = { scannerState = ScannerState.Idle }
            )
        }
    }

    // Handle scan result
    when (val state = productVariant) {
        is UiState.Success -> {
            AfterScanDialog(
                product = state.data,
                onDismissRequest = { viewModel.resetProductVariantState() },
                onAddToCart = { variant ->
                    if (variant == null) return@AfterScanDialog
                    viewModel.addProductToCart(state.data.toProduct(), variant)
                }
            )
        }
        is UiState.Error -> {
            AppDialog(
                message = state.message,
                onDismiss = { viewModel.resetProductVariantState() }
            )
        }
        else -> {}
    }

    // Main layout
    Row(modifier.fillMaxSize()) {
        // Left content (2/3 width)
        Column(Modifier.weight(2f)) {
            HeaderSection(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                userProfile = userProfile,
                categories = categories,
                selectedCategory = selectedCategory,
                onSelectedCategory = { selectedCategory = it },
                onBarcodeScanning = { scannerState = ScannerState.RequestPermission }
            )

            ProductListPanel(
                scrollBehavior = scrollBehavior,
                products = products,
                onProductClick = { product ->
                    selectedProduct = product.takeIf { it != selectedProduct } ?: selectedProduct
                }
            )
        }

        // Divider
        VerticalDivider(
            modifier = Modifier.shadow(
                elevation = 10.dp,
                spotColor = Black.copy(alpha = 0.3f),
                ambientColor = Black.copy(alpha = 0.3f)
            ),
            color = Color.Transparent
        )

        // Right sidebar (1/3 width)
        ProductVariantListPanel(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.primaryContainer),
            selectedProduct = selectedProduct,
            productVariants = productVariants,
            onAddToCart = { variant ->
                selectedProduct?.let { product ->
                    viewModel.addProductToCart(product, variant)
                }
            },
            onNavigateToCart = onNavigateToCart
        )
    }
}

@Composable
private fun HeaderSection(
    query: String,
    onQueryChange: (String) -> Unit,
    userProfile: UiState<User>,
    categories: UiState<List<Category>>,
    selectedCategory: Int,
    onSelectedCategory: (Int) -> Unit,
    onBarcodeScanning: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // User profile
        when (userProfile) {
            is UiState.Success -> {
                Text(
                    text = "Halo, ${userProfile.data.username}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            else -> {  }
        }

        // Current time
        ShowCurrentTime(
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Search and scan row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchTextField(
                value = query,
                onValueChange = onQueryChange,
                hint = "Cari produk...",
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onBarcodeScanning,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_scanner),
                    contentDescription = "Scan Barcode",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Categories
        when (categories) {
            is UiState.Success -> {
                AppLazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories.data) { category ->
                        CategoryCard(
                            text = category.name,
                            selected = selectedCategory == category.id,
                            onClick = { onSelectedCategory(category.id) }
                        )
                    }
                }
            }
            is UiState.Loading -> {
                AppLazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(5) {
                        LoadingCategory()
                    }
                }
            }
            else -> {}
        }

        HorizontalDivider(
            modifier = Modifier.shadow(
                elevation = 10.dp,
                spotColor = Black.copy(alpha = 0.3f),
                ambientColor = Black.copy(alpha = 0.3f)
            ),
            color = Color.Transparent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductListPanel(
    scrollBehavior: TopAppBarScrollBehavior,
    products: LazyPagingItems<Product>,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    AppLazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(products.itemCount) { index ->
            val product = products[index]
            product?.let {
                TabletProductCard(
                    modifier = Modifier,
                    name = product.name,
                    price = product.price,
                    variantCount = product.variantCount,
                    imageName = product.image,
                    discount = product.discount,
                    finalPrice = product.finalPrice,
                    onClick = { onProductClick(product) }
                )
            }
        }

        when {
            products.loadState.refresh is LoadState.Loading -> {
                items(3) { LoadingTabletProduct() }
            }
            products.loadState.append is LoadState.Loading -> {
                items(3) { LoadingTabletProduct() }
            }
            products.loadState.refresh is LoadState.Error -> {
                val error = products.loadState.refresh as LoadState.Error
                item(span = { GridItemSpan(4) }) {
                    ErrorItem(error = error.error, onRetry = { products.retry() })
                }
            }
            products.loadState.append is LoadState.Error -> {
                val error = products.loadState.append as LoadState.Error
                item(span = { GridItemSpan(4) }) {
                    ErrorItem(error = error.error, onRetry = { products.retry() })
                }
            }
        }
    }
}

@Composable
private fun ErrorItem(
    error: Throwable,
    onRetry: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        IconButton(onRetry) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Segarkan"
            )
        }
    }
}

@Composable
private fun ProductVariantListPanel(
    selectedProduct: Product?,
    productVariants: UiState<List<ProductVariant>>,
    onAddToCart: (ProductVariant) -> Unit,
    onNavigateToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = selectedProduct?.name ?: "Varian Produk",
            style = MaterialTheme.typography.titleLarge,
        )
        when (productVariants) {
            is UiState.Success -> {
                if (productVariants.data.isEmpty()){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Produk ini belum memiliki varian",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    AppLazyColumn(
                        Modifier.fillMaxSize().weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(productVariants.data, key = { it.id }) { variant ->
                            ProductVariantCard(
                                modifier = Modifier,
                                barcode = variant.barcode,
                                size = variant.size,
                                color = variant.color,
                                stock = variant.stock,
                                onClick = {
                                    onAddToCart(variant)
                                }
                            )
                        }
                    }
                }
            }
            is UiState.Loading -> {
                LoadingIndicator(Modifier.weight(1f))
            }
            is UiState.Error -> {
                ErrorMessage(productVariants.message)
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Pilih produk terlebih dahulu",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
        AppButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Buka Keranjang",
            onClick = onNavigateToCart,
            shape = CircleShape
        )
    }
}