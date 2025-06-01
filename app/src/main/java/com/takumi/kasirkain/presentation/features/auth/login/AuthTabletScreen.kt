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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.takumi.kasirkain.R
import com.takumi.kasirkain.presentation.common.components.AppDialog
import com.takumi.kasirkain.presentation.common.components.LoadingDialog
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.features.auth.login.components.LoginForm
import com.takumi.kasirkain.presentation.theme.LocalSpacing

@Composable
fun AuthTabletScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
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
            LoginTabletLeftSection(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .weight(7f)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .fillMaxHeight(0.8f)
                        .align(Alignment.Center)
                        .padding(horizontal = LocalSpacing.current.paddingMedium.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "Login",
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp,
                            style = MaterialTheme.typography.headlineLarge,
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .width(42.dp),
                            color = MaterialTheme.colorScheme.primary,
                            thickness = 2.dp
                        )
                    }
                    LoginForm(
                        modifier = Modifier.align(Alignment.Center),
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
}

@Composable
fun LoginTabletLeftSection(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        )
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-16).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.kasirkain_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(112.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
            Column(
                modifier = Modifier
                    .offset(x = (-20).dp)
            ) {
                Text(
                    text = "KasirKain",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "POS Application",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}