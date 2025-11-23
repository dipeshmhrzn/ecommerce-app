package com.example.ecommerce.presentation.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ecommerce.R
import com.example.ecommerce.domain.util.Result
import com.example.ecommerce.navigation.Routes
import com.example.ecommerce.presentation.cart.CartViewModel
import com.example.ecommerce.presentation.components.ProductCard
import com.example.ecommerce.presentation.components.ProductToolBar
import com.example.ecommerce.presentation.components.SortOption
import com.example.ecommerce.presentation.home.components.BannerCarousel
import com.example.ecommerce.presentation.home.components.BottomNavItem
import com.example.ecommerce.presentation.home.components.Categories
import com.example.ecommerce.presentation.home.components.CustomTopBar
import com.example.ecommerce.presentation.home.components.Filter
import com.example.ecommerce.presentation.home.components.SearchBar
import com.example.ecommerce.presentation.setting.SettingsViewModel
import com.example.ecommerce.presentation.wishlist.WishListViewModel
import com.example.ecommerce.ui.theme.Montserrat

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    productViewModel: ProductViewModel = hiltViewModel(),
    wishlistViewModel: WishListViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        settingsViewModel.getUserProfile()
    }

    val categories = listOf(
        CategoryItem("Men", R.drawable.mens),
        CategoryItem("Eyewear", R.drawable.eyewear),
        CategoryItem("Shoes", R.drawable.shoes),
        CategoryItem("Accessories", R.drawable.accessories),
        CategoryItem("Beauty", R.drawable.beauty),
        CategoryItem("Women", R.drawable.womens)
    )

    var minPrice by remember { mutableStateOf("") }
    var maxPrice by remember { mutableStateOf("") }
    var selectedRating by remember { mutableStateOf<Int?>(null) }

    var isFilterVisible by remember { mutableStateOf(false) }

    val userProfile by settingsViewModel.userProfile.collectAsState()
    val profilePictureUrl = (userProfile as? Result.Success)?.data?.profilePicture
    val isLoading = userProfile is Result.Loading

    val productState by productViewModel.productState.collectAsState()

    val wishlistState by wishlistViewModel.state.collectAsState()
    val wishlistCount = wishlistState.allProducts.size

    val cartState by cartViewModel.state.collectAsState()
    val cartCount = cartState.cartItems.sumOf { it.quantity }

    var selectedSort by remember { mutableStateOf<SortOption?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }


    val isFilterApplied by productViewModel.isFilterApplied.collectAsState()



    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CustomTopBar(
                    profilePictureUrl = profilePictureUrl,
                    isLoading = isLoading
                )
            },
            bottomBar = {

                if (!isFilterVisible) {
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
                                badgeCount = when (index) {
                                    1 -> wishlistCount
                                    2 -> cartCount  // cart count
                                    else -> 0
                                },
                                onClick = {
                                    selectedItem = index

                                    when (index) {
                                        1 -> navHostController.navigate(Routes.WishlistScreen)
                                        2 -> navHostController.navigate(Routes.CartScreen)
                                        3 -> navHostController.navigate(Routes.SearchScreen)
                                        4 -> navHostController.navigate(Routes.SettingScreen)
                                        else -> {

                                        }
                                    }
                                },
                            )
                        }
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

                    SearchBar(onClick = {
                        navHostController.navigate(Routes.SearchScreen)
                    })

                    BannerCarousel()

                    LazyRow(
                        contentPadding = PaddingValues(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(10.dp)
                            )

                    ) {
                        items(categories) { category ->
                            Categories(
                                item = category,
                                isSelected = selectedCategory == category.name,
                                onClick = {
                                    if (selectedCategory == category.name) {
                                        selectedCategory = null
                                        productViewModel.getProducts()
                                    } else {
                                        selectedCategory = category.name
                                        productViewModel.filterByHomeCategory(category.name)
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                when (val state = productState) {
                    is Result.Success -> {
                        val totalItems = state.data.size

                        stickyHeader {
                            ProductToolBar(
                                selectedSort = selectedSort,
                                totalItems = totalItems.toString(),
                                onSortClick = { order ->
                                    selectedSort = if (order) {
                                        SortOption(ascending = true, iconRes = R.drawable.sortup)
                                    } else {
                                        SortOption(ascending = false, iconRes = R.drawable.sortdown)
                                    }
                                    productViewModel.sortProducts(order)
                                }, onFilterClick = {
                                    isFilterVisible = !isFilterVisible
                                },
                                isFilterApplied = isFilterApplied
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    else -> {
                        stickyHeader {
                            ProductToolBar(
                                selectedSort = selectedSort,
                                totalItems = "Loading",
                                isFilterApplied = isFilterApplied
                            )
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
        if (isFilterVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray.copy(alpha = .5f))
                    .clickable {
                        isFilterVisible = false
                    }
            )
        }

        AnimatedVisibility(
            visible = isFilterVisible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 500)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(durationMillis = 500)
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clickable(enabled = false) {}
        ) {
            Filter(
                minPrice = minPrice,
                maxPrice = maxPrice,
                selectedRating = selectedRating,
                onApplyFilter = { minPrice, maxPrice, selectedRating ->
                    productViewModel.filterProductsByPrice(minPrice, maxPrice, selectedRating)
                    isFilterVisible = false
                },
                onFilterValuesChange = { newMinPrice, newMaxPrice, newSelectedRating ->
                    minPrice = newMinPrice
                    maxPrice = newMaxPrice
                    selectedRating = newSelectedRating
                },
                onResetClick = {
                    productViewModel.resetFilters()
                    isFilterVisible = false
                    selectedSort = null
                    selectedCategory = null
                }
            )
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

