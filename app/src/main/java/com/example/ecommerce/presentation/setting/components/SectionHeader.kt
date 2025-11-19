package com.example.ecommerce.presentation.setting.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ecommerce.ui.theme.Montserrat

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontFamily = Montserrat,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black
    )
}