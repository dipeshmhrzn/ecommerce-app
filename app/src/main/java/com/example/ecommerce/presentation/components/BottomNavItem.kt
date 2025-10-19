package com.example.ecommerce.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.presentation.home.BottomNavItemData
import com.example.ecommerce.ui.theme.Montserrat

@Composable
fun BottomNavItem(
    isCenter: Boolean = false,
    isSelected: Boolean,
    item: BottomNavItemData,
    onClick: () -> Unit
) {

    val modifier = if (isCenter) {
        Modifier
            .size(60.dp)
            .offset(y = (-20).dp)
            .shadow(5.dp, CircleShape)
            .background(Color.White, CircleShape)
            .padding(8.dp)
    } else {
        Modifier
            .size(60.dp)
    }

    val iconColor = if (isSelected) Color(0xFFF83758) else Color.Black
    val textColor = if (isSelected) Color(0xFFF83758) else Color.Black


    Box(
        modifier = modifier.clickable(
            onClick = onClick
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(item.imageRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(iconColor)
            )
            if (!isCenter) {
                Text(
                    text = item.text ?: "",
                    fontFamily = Montserrat,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
            }
        }

    }


}

