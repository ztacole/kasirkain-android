package com.takumi.kasirkain.presentation.features.history

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.takumi.kasirkain.domain.model.GroupedTransaction
import com.takumi.kasirkain.presentation.common.components.AppLazyColumn
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.features.history.components.TransactionCard
import com.takumi.kasirkain.presentation.theme.LocalSpacing
import com.takumi.kasirkain.presentation.util.shimmer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val transactions by viewModel.transactions.collectAsStateWithLifecycle()

    AppLazyColumn(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        stickyHeader {
            HistoryHeaderSection()
        }
        transactions.let { state ->
            when (state) {
                is UiState.Idle -> {}
                is UiState.Loading -> {
                    items(2) { LoadingTransaction() }
                }
                is UiState.Success<List<GroupedTransaction>> -> {
                    items(state.data) { transaction ->
                        TransactionCard(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            date = transaction.date,
                            transactions = transaction.transactions,
                            selectedTransactionId = 0,
                            onSelected = {}
                        )
                    }
                }
                is UiState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
fun HistoryHeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
    ) {
        Text(
            text = "Riwayat",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(16.dp)
        )
        HorizontalDivider()
    }
}

@Composable
fun LoadingTransaction() {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = LocalSpacing.current.paddingMedium.dp)) {
        Spacer(
            modifier = Modifier
                .width(140.dp)
                .height(26.dp)
                .clip(MaterialTheme.shapes.medium)
                .shimmer()
        )
        Spacer(Modifier.height(LocalSpacing.current.paddingLarge.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.paddingMedium.dp),
        ) {
            repeat(2) { LoadingTransactionHeader() }
        }
    }
}

@Composable
fun LoadingTransactionHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(
            modifier = Modifier
                .size(32.dp)
                .clip(MaterialTheme.shapes.medium)
                .shimmer()
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Spacer(
                modifier = Modifier
                    .width(90.dp)
                    .height(26.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shimmer()
            )
            Spacer(Modifier.height(6.dp))
            Spacer(
                modifier = Modifier
                    .width(120.dp)
                    .height(22.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shimmer()
            )
            Spacer(Modifier.height(6.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shimmer()
            )
        }
        Spacer(Modifier.width(12.dp))
        Spacer(
            modifier = Modifier
                .width(100.dp)
                .height(22.dp)
                .clip(MaterialTheme.shapes.medium)
                .shimmer()
        )
    }
}

@Preview
@Composable
private fun PreviewLoading() {
    LoadingTransactionHeader()
}