package com.takumi.kasirkain.presentation.features.auth.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.takumi.kasirkain.R
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
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        Text(
            text = "Username",
            style = MaterialTheme.typography.bodyLarge,
        )
        AppTextField(
            value = username,
            onValueChange = onUsernameChange,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Password",
            style = MaterialTheme.typography.bodyLarge,
        )
        AppTextField(
            value = password,
            onValueChange = onPasswordChange,
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    onClick = { showPassword = !showPassword },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            if (showPassword) R.drawable.ic_eye_closed
                            else R.drawable.ic_eye_open
                        ),
                        contentDescription = null
                    )
                }
            }
        )
        Spacer(Modifier.height(36.dp))
        AppButton(
            text = "Masuk",
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraSmall
        )
    }
}