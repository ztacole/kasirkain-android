package com.takumi.kasirkain.presentation.features.main.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.takumi.kasirkain.domain.model.TransactionHeader
import com.takumi.kasirkain.presentation.common.components.AppLazyColumn

@Composable
fun TransactionCard(
    modifier: Modifier = Modifier,
    date: String,
    transactionHeaders: List<TransactionHeader>
) {
    Column(modifier.fillMaxWidth()) {
        Text(
            text = date,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            transactionHeaders.map { data ->
                TransactionHeaderCard(
                    modifier = Modifier,
                    paymentType = data.paymentType,
                    productCount = data.productCount,
                    totalPayment = data.total,
                    time = data.time
                )
            }
        }
    }
}