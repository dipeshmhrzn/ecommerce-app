package com.example.ecommerce.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ecommerce.domain.util.Result
import com.example.ecommerce.navigation.Routes
import com.example.ecommerce.presentation.components.ProductCard
import com.example.ecommerce.presentation.components.ProductToolBar
import com.example.ecommerce.presentation.search.components.SearchBar
import com.example.ecommerce.ui.theme.Montserrat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    navHostController: NavHostController
) {

    val searchState by searchViewModel.searchState.collectAsState()

    val focusRequester = remember { FocusRequester() }

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {

            Box(
                modifier = Modifier
                    .padding(
                        top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                    )
                    .padding(top = 16.dp, bottom = 16.dp)
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                        searchViewModel.searchProducts(it, showAllOnBlank = false)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
            }

        },
        containerColor = Color(0xFFF9F9F9)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            when (val state = searchState) {
                is Result.Success -> {

                    val searchedProducts = state.data
                    val totalItems = state.data.size

                    if (searchedProducts.isEmpty()) {
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
                        val chunkedProduct = searchedProducts.chunked(2)

                        stickyHeader {
                            ProductToolBar(totalItems.toString())
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        items(items = chunkedProduct) { rowItems ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp)
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                rowItems.forEach { item ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(Color.White, RoundedCornerShape(10.dp))
                                    ) {
                                        ProductCard(
                                            item = item,
                                            onClick = {
                                                navHostController.navigate(
                                                    Routes.ProductDetailScreen(
                                                        id = item.id
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }
                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }

                    }

                }

                is Result.Idle -> {
                    if (searchQuery.isBlank()) {
                        item {
                            Spacer(modifier = Modifier.height(150.dp))
                            Text(
                                text = "Search any Product...",
                                fontSize = 20.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                Result.Loading -> {
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
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