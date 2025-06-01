package com.takumi.kasirkain.presentation.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.takumi.kasirkain.presentation.theme.Gray
import com.takumi.kasirkain.presentation.theme.KasirKainTheme
import com.takumi.kasirkain.presentation.theme.LightGray

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String)-> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    readOnly: Boolean = false,
    hint: String = "",
    containerColor: Color = Color.White,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = RectangleShape
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        visualTransformation = visualTransformation,
        readOnly = readOnly,
        placeholder = { Text(hint) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = Gray
        ),
        singleLine = true,
        shape = shape,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    KasirKainTheme {
        AppTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.padding(8.dp),
            hint = "Username"
        )
    }
}