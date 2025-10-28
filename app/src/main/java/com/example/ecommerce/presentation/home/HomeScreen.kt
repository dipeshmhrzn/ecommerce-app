package com.example.ecommerce.presentation.home

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ecommerce.R
import com.example.ecommerce.domain.util.Result
import com.example.ecommerce.navigation.Routes
import com.example.ecommerce.presentation.components.ProductCard
import com.example.ecommerce.presentation.components.ProductToolBar
import com.example.ecommerce.presentation.home.components.BannerCarousel
import com.example.ecommerce.presentation.home.components.BottomNavItem
import com.example.ecommerce.presentation.home.components.Categories
import com.example.ecommerce.presentation.home.components.CustomTopBar
import com.example.ecommerce.presentation.home.components.SearchBar
import com.example.ecommerce.presentation.wishlist.WishListViewModel
import com.example.ecommerce.ui.theme.Montserrat

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    productViewModel: ProductViewModel = hiltViewModel(),
    wishlistViewModel: WishListViewModel = hiltViewModel()
) {

    val categories = listOf(
        CategoryItem("Beauty", R.drawable.beauty),
        CategoryItem("Fashion", R.drawable.fashion),
        CategoryItem("Kids", R.drawable.kids),
        CategoryItem("Mens", R.drawable.mens),
        CategoryItem("Womens", R.drawable.womens),
        CategoryItem("Electronics", R.drawable.electronics)
    )

    val productState by productViewModel.productState.collectAsState()

    val wishlistState by wishlistViewModel.state.collectAsState()
    val wishlistCount = wishlistState.allProducts.size


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
                        isCenter = index == 2,
                        isSelected = index == selectedItem,
                        item = item,
                        badgeCount = if (index==1) wishlistCount else 0,
                        onClick = {
                            selectedItem = index

                            when (index){
                                1 -> navHostController.navigate(Routes.WishlistScreen)
                                3 -> navHostController.navigate(Routes.SearchScreen)
                                else -> {

                                }
                            }
                        },
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

                SearchBar (onClick = {
                    navHostController.navigate(Routes.SearchScreen)
                })

                BannerCarousel()

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

            when (val state = productState) {
                is Result.Success -> {
                    val totalItems = state.data.size

                    stickyHeader {
                        ProductToolBar(totalItems.toString())
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                else -> {
                    stickyHeader {
                        ProductToolBar("Loading")
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            when (val state = productState) {
                is Result.Success -> {
                    val products = state.data

                    if (products.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No products found",
                                    fontFamily = Montserrat,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 20.sp
                                )
                            }
                        }
                    } else {
                        val chunkedProducts = products.chunked(2) // 2 per row

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
                                        ProductCard(
                                            item = product,
                                            onClick = {
                                                navHostController.navigate(
                                                    Routes.ProductDetailScreen(
                                                        id = product.id
                                                    )
                                                )
                                            })
                                    }
                                }
                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f)) // balance row if odd count
                                }
                            }
                        }
                    }
                }

                is Result.Idle, Result.Loading -> {
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

                is Result.Error -> {
                    item {
                        Text("Error Loading Products : ${state.message}")
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

