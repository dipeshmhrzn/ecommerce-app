package com.example.ecommerce.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ecommerce.R
import com.example.ecommerce.presentation.home.ProductViewModel
import com.example.ecommerce.presentation.home.components.Filter
import com.example.ecommerce.ui.theme.Montserrat

@Composable
fun ProductToolBar(
    totalItems: String,
    selectedSort: SortOption?,
    onSortClick: (order: Boolean) -> Unit = {},
    onFilterClick: () -> Unit = {},
    isFilterApplied: Boolean = false
) {
//    var selectedSort by remember { mutableStateOf<SortOption?>(null) }

    val sortIcon = when (selectedSort?.ascending) {
        true -> painterResource(R.drawable.sortup)
        false -> painterResource(R.drawable.sortdown)
        else -> painterResource(R.drawable.sort)
    }

    val sortOption = listOf(
        SortOption(ascending = true, R.drawable.sortup),
        SortOption(ascending = false, R.drawable.sortdown)
    )

    val sortLabel = if (selectedSort == null) "Sort" else "Price"

    Row(
        modifier = Modifier
            .fillMaxWidth()
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
            text = sortLabel,
            icon = sortIcon,
            hasDropdown = true,
            dropdownOptions = sortOption,
            onOptionSelected = { option ->
                onSortClick(option.ascending)
            }
        )

        Spacer(modifier = Modifier.size(10.dp))

        CommonSortFilter(
            modifier = Modifier.padding(end = 8.dp)
                .border(
                    width = 1.dp,
                    color = if (isFilterApplied) Color.Red else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                ),
            text = "Filter",
            icon = painterResource(R.drawable.filter),
            onClick = {
                onFilterClick()
            }
        )
    }
}
