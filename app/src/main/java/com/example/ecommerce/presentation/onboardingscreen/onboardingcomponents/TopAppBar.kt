package com.example.ecommerce.presentation.onboardingscreen.onboardingcomponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.ui.theme.Montserrat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onSkipClick: () -> Unit = {},
    pageNumber: String
) {
    TopAppBar(title = {
        Row {
            Text(
                text = pageNumber,
                fontFamily = Montserrat,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            Text(
                text = "/3",
                fontFamily = Montserrat,
                color = Color.Black.copy(alpha = 0.3f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        }
    }, actions = {
        Text(
            text = "Skip",
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(end = 8.dp)
                .clickable { onSkipClick() }
        )
    })
}