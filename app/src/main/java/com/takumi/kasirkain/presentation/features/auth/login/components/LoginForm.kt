package com.takumi.kasirkain.presentation.features.auth.login.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.takumi.kasirkain.presentation.common.components.AppButton
import com.takumi.kasirkain.presentation.common.components.AppTextField

@Composable
fun LoginForm(
    username: String,
    onUsernameChange: (String)-> Unit,
    password: String,
    onPasswordChange: (String)-> Unit,
    onClick: ()-> Unit
) {
    AppTextField(
        value = username,
        onValueChange = onUsernameChange,
        modifier = Modifier.padding(horizontal = 16.dp),
        leadingIcon = Icons.Default.Person,
        label = "Username"
    )
    Spacer(Modifier.height(12.dp))
    AppTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = Modifier.padding(horizontal = 16.dp),
        leadingIcon = Icons.Default.Lock,
        label = "Password",
        visualTransformation = PasswordVisualTransformation()
    )
    Spacer(Modifier.height(24.dp))
    AppButton(
        text = "Log In",
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
    )
}