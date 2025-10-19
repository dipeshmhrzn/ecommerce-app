package com.example.ecommerce.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.ui.theme.Montserrat

@Composable
fun CommonSortFilter(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(color = Color.Gray.copy(alpha = .1f), shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
            .height(20.dp)
            .wrapContentWidth()
            .clickable(onClick = {
                onClick()
            }),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color(0xFF232327),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
