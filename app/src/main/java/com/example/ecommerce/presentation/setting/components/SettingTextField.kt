package com.example.ecommerce.presentation.setting.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.ui.theme.Montserrat

@Composable
fun SettingsTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    trailingText: String? = null,
    enabled: Boolean = true,
    onTrailingClick: (() -> Unit)? = null
) {
    Column {
        Text(
            text = label,
            fontFamily = Montserrat,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp),
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            trailingIcon = {
                if (trailingText != null) {
                    TextButton(
                        onClick = {
                            onTrailingClick?.invoke()
                        }
                    ) {
                        Text(
                            text = trailingText,
                            fontFamily = Montserrat,
                            fontSize = 14.sp,
                            color = Color(0xFFF83758),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color(0xFFE0E0E0),
                unfocusedIndicatorColor = Color(0xFFE0E0E0),
                disabledIndicatorColor = Color(0xFFE0E0E0),
                disabledTextColor = Color.Gray
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}