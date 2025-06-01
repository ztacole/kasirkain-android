package com.takumi.kasirkain.presentation.features.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.LocalImageLoader
import coil.request.ImageRequest
import coil.size.Size
import com.takumi.kasirkain.di.AppModule
import com.takumi.kasirkain.R
import com.takumi.kasirkain.presentation.common.components.DiscountBadge
import com.takumi.kasirkain.presentation.theme.KasirKainTheme
import com.takumi.kasirkain.presentation.theme.LocalSpacing
import com.takumi.kasirkain.presentation.util.CoreFunction

@Composable
fun CartItemCard(
    modifier: Modifier = Modifier,
    name: String,
    imageName: String,
    size: String,
    color: String,
    stock: Int,
    quantity: Int,
    price: Int,
    discount: Int,
    finalPrice: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    val context = LocalContext.current
    val imageUrl = remember(imageName) {
        "${AppModule.provideBaseUrl()}product/$imageName/photo"
    }
    val imageLoader = LocalImageLoader.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .memoryCacheKey(imageName)
                .diskCacheKey(imageName)
                .size(Size.ORIGINAL)
                .build(),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.secondary)
                .aspectRatio(1f),
            imageLoader = imageLoader,
            error = painterResource(id = R.drawable.kasirkain_logo_gray),
            placeholder = painterResource(id = R.drawable.kasirkain_logo_gray)
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .height(120.dp)
                .padding(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = "Size : $size | Warna : $color",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    if (discount > 0) {
                        Spacer(Modifier.weight(1f))
                        DiscountBadge(discount)
                        Row(
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = CoreFunction.rupiahFormatter(finalPrice.toLong()),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.Bottom),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                            Spacer(Modifier.width(LocalSpacing.current.paddingSmall.dp))
                            Text(
                                text = CoreFunction.rupiahFormatter(price.toLong()),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.Bottom),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSecondary,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.weight(1f).padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = CoreFunction.rupiahFormatter(finalPrice.toLong()),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Bottom),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
            Column {
                Text(
                    text = "Stok: $stock",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = LocalSpacing.current.paddingMedium.dp, top = 4.dp)
                        .weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Row(
                    modifier = Modifier.offset(y = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDecrease,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_minus),
                            contentDescription = "Kurang",
                            modifier = Modifier.size(LocalSpacing.current.smallIconSize.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.requiredWidth(24.dp),
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        onClick = onIncrease
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = "Tambah",
                            modifier = Modifier.size(LocalSpacing.current.smallIconSize.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    KasirKainTheme {
        CartItemCard(
            name = "Produk",
            imageName = "",
            size = "XXL",
            color = "Blue",
            stock = 12,
            quantity = 1,
            price = 100000,
            discount = 20,
            finalPrice = 80000,
            onIncrease = {},
            onDecrease = {}
        )
    }
}