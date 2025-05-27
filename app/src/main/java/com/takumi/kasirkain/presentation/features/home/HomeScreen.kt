package com.takumi.kasirkain.presentation.features.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.takumi.kasirkain.R
import com.takumi.kasirkain.domain.model.Category
import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.presentation.common.components.AppLazyColumn
import com.takumi.kasirkain.presentation.common.components.AppLazyRow
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.features.home.components.CategoryCard
import com.takumi.kasirkain.presentation.features.home.components.ProductCard
import com.takumi.kasirkain.presentation.features.home.components.SearchTextField
import com.takumi.kasirkain.presentation.util.shimmer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val products by viewModel.products.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    var selectedCategory by remember { mutableIntStateOf(0) }
    var query by remember { mutableStateOf("") }

    LaunchedEffect(selectedCategory) {
        if (selectedCategory > 0) viewModel.getProduct(category = selectedCategory.toString())
        else viewModel.getProduct()
    }

    LaunchedEffect(Unit) {
        viewModel.getCategories()
    }

    AppLazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        stickyHeader {
            HomeHeaderSection(
                query = query,
                onQueryChange = {
                    query = it
                    viewModel.getProduct(selectedCategory.toString(), query)
                },
                categories = categories,
                selectedCategory = selectedCategory,
                onSelectedCategory = {selectedCategory = it}
            )
        }
        products.let { state ->
            when (state) {
                is UiState.Idle -> {}
                is UiState.Loading -> {
                    items(4) { LoadingProduct() }
                }
                is UiState.Success<List<Product>> -> {
                    items(state.data, key = {it.id}) { product ->
                        ProductCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            name = product.name,
                            price = product.finalPrice,
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

@Composable
fun HomeHeaderSection(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String)-> Unit,
    categories: UiState<List<Category>>,
    selectedCategory: Int,
    onSelectedCategory: (Int)-> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .shadow(
                elevation = 2.dp,
                ambientColor = Color.Black.copy(alpha = 0.3f),
                spotColor = Color.Black.copy(alpha = 0.3f)
            )
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Hi, Geyzz")
            }
            Image(
                painter = painterResource(id = R.drawable.kasirkain_logo),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(Modifier.height(2.dp))
        SearchTextField(
            value = query,
            onValueChange = onQueryChange,
            hint = "Cari produk...",
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(Modifier.height(8.dp))
        AppLazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.let { state ->
                when(state) {
                    is UiState.Idle -> {}
                    is UiState.Loading -> {
                        items(3) { LoadingCategory() }
                    }
                    is UiState.Success<List<Category>> -> {
                        items(state.data, key = {it.id}) { category ->
                            CategoryCard(
                                onClick = { onSelectedCategory(category.id) },
                                modifier = Modifier,
                                text = category.name,
                                selected = selectedCategory ==  category.id
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
}

@Composable
fun LoadingProduct() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(120.dp)
                .clip(MaterialTheme.shapes.large)
                .shimmer()
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shimmer()
            )
            Spacer(Modifier.height(2.dp))
            Spacer(
                modifier = Modifier
                    .width(60.dp)
                    .height(18.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shimmer()
            )
            Spacer(Modifier.height(14.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(24.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shimmer()
            )
        }
    }
}

@Composable
fun LoadingCategory() {
    Spacer(
        modifier = Modifier
            .width(110.dp)
            .size(40.dp)
            .clip(MaterialTheme.shapes.small)
            .shimmer()
    )
}

@Preview
@Composable
private fun Shimmer() {
    LoadingProduct()
//    LoadingCategory()
}