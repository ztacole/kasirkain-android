package com.takumi.kasirkain.presentation.features.scan.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.takumi.kasirkain.domain.model.ProductDetail
import com.takumi.kasirkain.domain.model.ProductVariant
import com.takumi.kasirkain.presentation.util.CoreFunction

@Composable
fun AfterScanDialog(
    onDismissRequest: ()-> Unit,
    onAddToCart: (ProductVariant?)-> Unit,
    product: ProductDetail
) {
    val productVariant = product.variants[0]
    AlertDialog(
        onDismissRequest = { },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        title = {
            Text(text = "Produk ditemukan")
        },
        text = {
            Text(
                text = """Nama: ${product.name}
                    |Ukuran: ${productVariant.size}
                    |Warna: ${productVariant.color}
                    |Stok: ${productVariant.stock}
                    |Harga: ${CoreFunction.formatToRupiah(product.price)}""".trimMargin()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    onAddToCart(productVariant)
                }
            ) { Text("Tambah ke Keranjang") }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    onAddToCart(null)
                }
            ) { Text("Scan Ulang") }
        }
    )
}