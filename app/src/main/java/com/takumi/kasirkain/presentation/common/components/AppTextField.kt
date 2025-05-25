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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.takumi.kasirkain.presentation.theme.KasirKainTheme

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String)-> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    readOnly: Boolean = false,
    placeHolder: String,
    containerColor: Color = Color.White,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    shape: Shape = MaterialTheme.shapes.medium
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        visualTransformation = visualTransformation,
        readOnly = readOnly,
        placeholder = { Text(placeHolder) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        singleLine = true,
        shape = shape,
        leadingIcon = {
            leadingIcon?.let { Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            ) }
        },
        trailingIcon = {
            trailingIcon?.let { Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            ) }
        }
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
            placeHolder = "Username"
        )
    }
}