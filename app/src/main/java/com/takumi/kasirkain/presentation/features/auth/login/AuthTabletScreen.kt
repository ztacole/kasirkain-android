package com.takumi.kasirkain.presentation.features.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.takumi.kasirkain.R
import com.takumi.kasirkain.presentation.common.components.AppDialog
import com.takumi.kasirkain.presentation.common.components.LoadingDialog
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.features.auth.login.components.LoginForm
import com.takumi.kasirkain.presentation.theme.KasirKainTheme
import com.takumi.kasirkain.presentation.theme.LocalSpacing

@Composable
fun AuthTabletScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: ()-> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }

    when (uiState) {
        is UiState.Idle -> {}
        is UiState.Loading -> {
            LoadingDialog()
        }
        is UiState.Success -> {
            onLoginSuccess()
            viewModel.resetState()
        }
        is UiState.Error -> {
            LaunchedEffect(uiState.message) {
                errorMessage = uiState.message
                showErrorDialog = true
                viewModel.resetState()
            }
        }
    }

    if (showErrorDialog) {
        AppDialog(message = errorMessage) { showErrorDialog = false }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            LoginTabletHeaderSection()
            Spacer(Modifier.width(24.dp))
            Column(
                modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "Login",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 40.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(bottom = LocalSpacing.current.paddingSmall.dp)
                        .padding(horizontal = LocalSpacing.current.paddingMedium.dp)
                )
                LoginForm(
                    modifier = Modifier,
                    username = username,
                    onUsernameChange = { username = it },
                    password = password,
                    onPasswordChange = { password = it }
                ) {
                    viewModel.login(username, password)
                }
            }
        }
    }
}

@Composable
fun LoginTabletHeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.45f)
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer),
        )
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(top = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.kasirkain_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(112.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Column(
                modifier = Modifier
                    .offset(x = (-20).dp)
            ) {
                Text(
                    text = "KasirKain",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "POS Application",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(name = "Tablet", widthDp = 1280, heightDp = 800)
@Composable
private fun Prev () {
    KasirKainTheme {
        AuthTabletScreen {  }
    }
}