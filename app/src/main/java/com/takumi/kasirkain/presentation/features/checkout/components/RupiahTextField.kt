package com.takumi.kasirkain.presentation.features.checkout.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import com.takumi.kasirkain.presentation.util.CoreFunction
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

@Composable
fun RupiahTextField(
    modifier: Modifier = Modifier,
    value: Long,
    onValueChange: (Long) -> Unit,
    maxLength: Int = 18
) {
    val formatter = remember {
        DecimalFormat("#,##0", DecimalFormatSymbols().apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        })
    }

    var textFieldValue by remember { mutableStateOf(TextFieldValue(value.toString())) }
    var isEditing by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(value) {
        if (!isEditing) {
            textFieldValue = TextFieldValue(formatter.format(value))
        }
    }

    TextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            try {
                isEditing = true
                val digitsOnly = newValue.text.filter { it.isDigit() }
                val numericValue = digitsOnly.ifEmpty { "0" }.toLong()

                if (digitsOnly.length > maxLength) {
                    return@TextField
                }

                textFieldValue = TextFieldValue(
                    text = digitsOnly,
                    selection = newValue.selection
                )

                onValueChange(numericValue)
            } catch (e: Exception) {
                Log.e("Long Exc", "RupiahTextField: $e", )
            }
        },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                isEditing = false
                focusManager.moveFocus(FocusDirection.Right)
                textFieldValue = TextFieldValue(formatter.format(value))
            }
        ),
        textStyle = MaterialTheme.typography.headlineLarge,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground
        )
    )
}