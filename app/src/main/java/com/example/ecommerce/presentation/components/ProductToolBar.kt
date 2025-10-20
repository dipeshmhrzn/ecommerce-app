package com.example.ecommerce.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.R
import com.example.ecommerce.ui.theme.Montserrat

@Composable
fun ProductToolBar(totalItems: String) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .height(50.dp)
            .background(color = Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$totalItems items",
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 10.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        CommonSortFilter(
            text = "Sort", icon = painterResource(R.drawable.sort)
        )
        Spacer(modifier = Modifier.size(10.dp))
        CommonSortFilter(
            modifier = Modifier.padding(end = 8.dp),
            text = "Filter",
            icon = painterResource(
                R.drawable.filter,
            )
        )
    }
}