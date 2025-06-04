package com.takumi.kasirkain.presentation.features.scan.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.takumi.kasirkain.domain.model.ProductDetail
import com.takumi.kasirkain.domain.model.ProductVariant
import com.takumi.kasirkain.presentation.common.components.ConfirmationDialog
import com.takumi.kasirkain.presentation.features.home.components.ProductVariantCard
import com.takumi.kasirkain.presentation.util.CoreFunction

@Composable
fun AfterScanDialog(
    onDismissRequest: ()-> Unit,
    onAddToCart: (ProductVariant?)-> Unit,
    product: ProductDetail
) {
    val productVariant = product.variants[0]
    ConfirmationDialog(
        title = "Produk ditemukan",
        onDismiss = {
            onAddToCart(null)
            onDismissRequest()
        },
        onConfirm = {
            onAddToCart(productVariant)
            onDismissRequest()
        },
        confirmText = "Masukkan keranjang",
        dismissText = "Scan ulang",
        content = {
            ProductScannedCard(
                modifier = Modifier.fillMaxWidth(),
                name = product.name,
                imageName = product.image,
                size = productVariant.size,
                color = productVariant.color,
                stock = productVariant.stock,
                price = product.price,
                discount = product.discount,
                finalPrice = product.finalPrice
            )
        }
    )
}