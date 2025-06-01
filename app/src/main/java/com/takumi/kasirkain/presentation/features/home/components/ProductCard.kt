package com.takumi.kasirkain.presentation.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.takumi.kasirkain.di.AppModule
import com.takumi.kasirkain.presentation.theme.KasirKainTheme
import com.takumi.kasirkain.presentation.util.CoreFunction
import com.takumi.kasirkain.presentation.util.shimmer
import com.takumi.kasirkain.R
import com.takumi.kasirkain.presentation.theme.LocalSpacing

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    name: String,
    variantCount: Int,
    price: Int,
    imageName: String
) {
    val imageUrl = if (imageName.isEmpty()) null else "${AppModule.provideBaseUrl()}product/$imageName/photo"

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .memoryCacheKey(if (imageName.isEmpty()) null else imageName)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.secondary)
                    .aspectRatio(1f),
                error = painterResource(R.drawable.kasirkain_logo_gray)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = "Jumlah varian: $variantCount",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = CoreFunction.rupiahFormatter(price.toLong()),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun TabletProductCard(
    modifier: Modifier = Modifier,
    name: String,
    variantCount: Int,
    price: Int,
    imageName: String,
    discount: Int,
    finalPrice: Int
) {
    val imageUrl = "${AppModule.provideBaseUrl()}product/$imageName/photo"

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .memoryCacheKey(imageName)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .scale(1f)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.secondary)
                .aspectRatio(1f),
            error = painterResource(R.drawable.kasirkain_logo_gray)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = "Varian: $variantCount",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(Modifier.height(LocalSpacing.current.paddingSmall.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = CoreFunction.rupiahFormatter(finalPrice.toLong()),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                if (discount > 0) {
                    Text(
                        text = CoreFunction.rupiahFormatter(price.toLong()),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f),
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSecondary,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(
                        text = "$discount%",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.tertiary,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                else {
                    Text(
                        text = "",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingTabletProduct() {
    Column(
        modifier = Modifier.width(160.dp)
    ) {
        Spacer(
            modifier = Modifier
                .size(200.dp)
                .clip(MaterialTheme.shapes.large)
                .shimmer()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .width(140.dp)
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
            Spacer(Modifier.height(LocalSpacing.current.paddingMedium.dp))
            Spacer(
                modifier = Modifier
                    .width(120.dp)
                    .height(24.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shimmer()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    KasirKainTheme {
        TabletProductCard(
            name = "Baju",
            variantCount = 3,
            price = 100000,
            imageName = "",
            discount = 20,
            finalPrice = 80000
        )
    }
}