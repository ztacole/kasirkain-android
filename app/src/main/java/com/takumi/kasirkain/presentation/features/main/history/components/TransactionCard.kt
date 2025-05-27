package com.takumi.kasirkain.presentation.features.main.history.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.takumi.kasirkain.domain.model.Transaction
import com.takumi.kasirkain.presentation.theme.LocalSpacing

@Composable
fun TransactionCard(
    modifier: Modifier = Modifier,
    date: String,
    transactions: List<Transaction>,
    onSelected: (Int) -> Unit
) {
    Column(modifier.fillMaxWidth()) {
        Text(
            text = date,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = LocalSpacing.current.paddingMedium.dp)
        )
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            transactions.map { data ->
                TransactionHeaderCard(
                    modifier = Modifier
                        .clickable {
                            onSelected(data.id)
                        },
                    paymentType = data.paymentType,
                    productCount = data.productCount,
                    totalPayment = data.total,
                    time = data.time
                )
            }
        }
    }
}