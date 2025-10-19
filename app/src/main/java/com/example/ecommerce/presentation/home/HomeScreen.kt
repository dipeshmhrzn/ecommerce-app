package com.example.ecommerce.presentation.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ecommerce.R
import com.example.ecommerce.domain.util.Result
import com.example.ecommerce.presentation.components.BottomNavItem
import com.example.ecommerce.presentation.components.Categories
import com.example.ecommerce.presentation.components.CustomTopBar
import com.example.ecommerce.presentation.components.ProductCard
import com.example.ecommerce.presentation.components.ProductToolBar
import com.example.ecommerce.presentation.components.SearchBar

@Composable
fun HomeScreen(productViewModel: ProductViewModel = hiltViewModel()) {

    val categories = listOf(
        CategoryItem("Beauty", R.drawable.beauty),
        CategoryItem("Fashion", R.drawable.fashion),
        CategoryItem("Kids", R.drawable.kids),
        CategoryItem("Mens", R.drawable.mens),
        CategoryItem("Womens", R.drawable.womens),
        CategoryItem("Electronics", R.drawable.electronics)
    )

    var searchQuery by remember { mutableStateOf("") }

    val totalItems = productViewModel.totalItems

    val productState by productViewModel.productState.collectAsState()

    val filteredProducts = when (val state = productState) {
        is Result.Success -> {
            if (searchQuery.isEmpty()) {
                state.data.products
            } else {
                state.data.products.filter { product ->
                    product.title.contains(searchQuery, ignoreCase = true) || product.category.contains(searchQuery, ignoreCase = true)
                }
            }
        }

        else -> {
            emptyList()
        }

    }

    Scaffold(
        topBar = {
            CustomTopBar()
        },
        bottomBar = {

            var selectedItem by remember { mutableIntStateOf(0) }

            val navItems = listOf(
                BottomNavItemData(text = "Home", imageRes = R.drawable.home),
                BottomNavItemData(text = "Wishlist", imageRes = R.drawable.wishlist),
                BottomNavItemData(imageRes = R.drawable.cart),
                BottomNavItemData(text = "Search", imageRes = R.drawable.search),
                BottomNavItemData(text = "Settings", imageRes = R.drawable.settings),

                )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                navItems.forEachIndexed { index, item ->
                    BottomNavItem(
                        item = item,
                        isSelected = index == selectedItem,
                        isCenter = index == 2,
                        onClick = {
                            selectedItem = index
                        }
                    )
                }

            }
        },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            item {
                Spacer(modifier = Modifier.height(16.dp))

                SearchBar(
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(10.dp)

                        )

                ) {
                    items(categories) { category ->
                        Categories(item = category)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            stickyHeader {
                ProductToolBar(totalItems)
                Spacer(modifier = Modifier.height(16.dp))
            }
            when (productState) {

                is Result.Success -> {

                    if (filteredProducts.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No products found.",
                                    fontSize = 18.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    } else {
                        val chunkedProducts = filteredProducts.chunked(2) // 2 per row

                        Log.d("CategoryLog", filteredProducts.size.toString())
                        filteredProducts.forEach { category ->
                            Log.d("CategoryLog", category.category)
                        }

                        items(chunkedProducts) { rowItems ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp)
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                for (product in rowItems) {

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(Color.White, RoundedCornerShape(10.dp))
                                    ) {
                                        ProductCard(product)
                                    }
                                }
                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f)) // balance row if odd count
                                }
                            }
                        }
                    }
                }

                is Result.Error -> {
                    item { Text("Error loading products.") }
                }

                is Result.Loading, Result.Idle -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )

                        }
                    }
                }
            }


        }
    }
}

data class CategoryItem(
    val name: String,
    val imageRes: Int
)

data class BottomNavItemData(
    val text: String? = null,
    val imageRes: Int,
)

