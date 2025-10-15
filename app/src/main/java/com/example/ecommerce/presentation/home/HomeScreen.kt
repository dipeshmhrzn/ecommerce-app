package com.example.ecommerce.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.R
import com.example.ecommerce.presentation.components.Categories
import com.example.ecommerce.presentation.components.CommonSortFilter
import com.example.ecommerce.presentation.components.CustomTopBar
import com.example.ecommerce.presentation.components.SearchBar
import com.example.ecommerce.ui.theme.Montserrat

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val categories = listOf(
        CategoryItem("Beauty", R.drawable.beauty),
        CategoryItem("Fashion", R.drawable.fashion),
        CategoryItem("Kids", R.drawable.kids),
        CategoryItem("Mens", R.drawable.mens),
        CategoryItem("Womens", R.drawable.womens),
        CategoryItem("Electronics", R.drawable.electronics)
    )

    Scaffold(
        topBar = { CustomTopBar() }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = Color(0xFFF9F9F9))
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            SearchBar()
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "All Featured",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                CommonSortFilter(
                    text = "Sort", icon = painterResource(R.drawable.sort)
                )
                Spacer(modifier = Modifier.size(10.dp))
                CommonSortFilter(
                    text = "Filter", icon = painterResource(R.drawable.filter)
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            LazyRow(
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.background(
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )

            ) {
                items(categories) { category ->
                    Categories(item = category)
                }
            }


        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomePreview(modifier: Modifier = Modifier) {
    HomeScreen()
}

data class CategoryItem(val name: String, val imageRes: Int)
