package com.takumi.kasirkain.presentation.features.scan.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.takumi.kasirkain.domain.model.ProductDetail
import com.takumi.kasirkain.domain.model.ProductVariant
import com.takumi.kasirkain.presentation.common.components.ConfirmationDialog
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
            onDismissRequest()
            onAddToCart(null)
        },
        onConfirm = {
            onDismissRequest()
            onAddToCart(productVariant)
        },
        confirmText = "Masukkan keranjang",
        dismissText = "Scan ulang",
        content = {
            Text(
                text = """Nama: ${product.name}
                    |Size: ${productVariant.size}
                    |Warna: ${productVariant.color}
                    |Stok tersisa: ${productVariant.stock}
                    |Harga saat ini: ${CoreFunction.rupiahFormatter(product.finalPrice.toLong())}""".trimMargin()
            )
        }
    )
}