package com.example.ecommerce.presentation.onboardingscreen.onboardingcomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.ui.theme.Montserrat

@Composable
fun BottomBar(
    isFirstPage: Boolean = false,
    isLastPage: Boolean = false,
    onNextClick: () -> Unit = {},
    onPrevClick: () -> Unit = {}
) {
    BottomAppBar(
        containerColor = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isFirstPage) "" else "Prev",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.W600,
                    fontSize = 20.sp,
                    color = Color.Black.copy(0.3f),
                    modifier = Modifier
                        .clickable { onPrevClick() }
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(color = Color.Black.copy(0.3f), shape = CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .height(10.dp)
                            .width(45.dp)
                            .background(color = Color.Black, shape = CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(color = Color.Black.copy(0.3f), shape = CircleShape)
                    )
                }
                Text(
                    text = if (isLastPage) "    Get \n Started" else "Next",
                    fontFamily = Montserrat,
                    color = Color(0xFFF83758),
                    fontWeight = FontWeight.W600,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable { onNextClick() }
                )
            }
        }
    }
}
