package com.takumi.kasirkain.presentation.features.main.home

import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.takumi.kasirkain.R
import com.takumi.kasirkain.domain.model.Category
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.features.main.home.components.CategoryCard
import com.takumi.kasirkain.presentation.features.main.home.components.ProductCard
import com.takumi.kasirkain.presentation.features.main.home.components.SearchTextField
import com.takumi.kasirkain.presentation.theme.KasirKainTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val products = viewModel.products.collectAsState().value
    val categories = viewModel.categories.collectAsState().value

    var selectedCategory by remember { mutableIntStateOf(0) }
    var query by remember { mutableStateOf("") }

    LaunchedEffect(selectedCategory) {
        if (selectedCategory > 0) viewModel.getProduct(category = selectedCategory.toString())
        else viewModel.getProduct()
    }

    LazyColumn(
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
        items(products, key = {it.id}) { product ->
            ProductCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                name = product.name,
                price = product.price,
                variantCount = product.variantCount,
                imageName = product.image
            )
        }
    }
}

@Composable
fun HomeHeaderSection(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String)-> Unit,
    categories: List<Category>,
    selectedCategory: Int,
    onSelectedCategory: (Int)-> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .shadow(2.dp)
            .padding(bottom = 12.dp)
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
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories, key = {it.id}) { category ->
                CategoryCard(
                    modifier = Modifier
                        .clickable(
                            onClick = { onSelectedCategory(category.id) }
                        ),
                    text = category.name,
                    selected = selectedCategory ==  category.id
                )
            }
        }
    }
}

//@Preview()
//@Composable
//private fun Preview() {
//    KasirKainTheme {
//        HomeHeaderSection(
//            query = "",
//            onQueryChange = {}
//        )
//    }
//
//}