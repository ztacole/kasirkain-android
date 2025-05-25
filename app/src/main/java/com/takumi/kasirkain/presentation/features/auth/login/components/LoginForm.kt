package com.takumi.kasirkain.presentation.features.auth.login.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.takumi.kasirkain.presentation.common.components.AppButton
import com.takumi.kasirkain.presentation.common.components.AppTextField
import com.takumi.kasirkain.presentation.theme.LocalSpacing

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    username: String,
    onUsernameChange: (String)-> Unit,
    password: String,
    onPasswordChange: (String)-> Unit,
    onClick: ()-> Unit
) {
    AppTextField(
        value = username,
        onValueChange = onUsernameChange,
        modifier = Modifier.padding(horizontal = LocalSpacing.current.paddingMedium.dp),
        leadingIcon = Icons.Default.Person,
        placeHolder = "Username"
    )
    Spacer(Modifier.height(12.dp))
    AppTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = Modifier.padding(horizontal = LocalSpacing.current.paddingMedium.dp),
        leadingIcon = Icons.Default.Lock,
        placeHolder = "Password",
        visualTransformation = PasswordVisualTransformation()
    )
    Spacer(Modifier.height(24.dp))
    AppButton(
        text = "Log In",
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(LocalSpacing.current.paddingMedium.dp),
    )
}