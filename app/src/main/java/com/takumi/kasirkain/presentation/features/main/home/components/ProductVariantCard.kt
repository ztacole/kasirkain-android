package com.takumi.kasirkain.presentation.features.main.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.takumi.kasirkain.domain.model.ProductVariant
import com.takumi.kasirkain.presentation.theme.KasirKainTheme
import com.takumi.kasirkain.presentation.util.CoreFunction

@Composable
fun ProductVariantCard(
    modifier: Modifier = Modifier,
    barcode: String,
    size: String,
    color: String,
    stock: Int,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                uncheckedColor = MaterialTheme.colorScheme.primary,
                checkedColor = MaterialTheme.colorScheme.primary,
                checkmarkColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = barcode,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = "Ukuran: $size",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = "Warna: $color",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Stok: $stock",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Top)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    KasirKainTheme {
        ProductVariantCard(
            barcode = "PRD001LBLUE",
            size = "L",
            color = "Blue",
            stock = 12,
            isChecked = false
        ) { }
    }

}