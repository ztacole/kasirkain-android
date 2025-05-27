package com.takumi.kasirkain.presentation.features.main.history.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.takumi.kasirkain.R
import com.takumi.kasirkain.presentation.theme.KasirKainTheme
import com.takumi.kasirkain.presentation.theme.LocalSpacing
import com.takumi.kasirkain.presentation.util.CoreFunction

@Composable
fun TransactionHeaderCard(
    modifier: Modifier = Modifier,
    paymentType: String,
    productCount: Int,
    totalPayment: Int,
    time: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Icon(
                painter = painterResource(
                    if (paymentType == "QRIS") R.drawable.ic_qris
                    else R.drawable.ic_money
                ),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = paymentType,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = "Jenis Produk: $productCount",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = "Total: ${CoreFunction.rupiahFormatter(totalPayment.toLong())}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = time,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Prev() {
    KasirKainTheme {
        TransactionHeaderCard(
            paymentType = "QRIS",
            productCount = 2,
            totalPayment = 120000,
            time = "17:00 WIB"
        )
    }

}