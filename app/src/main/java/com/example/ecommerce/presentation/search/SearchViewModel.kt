package com.example.ecommerce.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.domain.usecase.productusecase.SearchProductUseCase
import com.example.ecommerce.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


@HiltViewModel
class SearchViewModel @Inject constructor(
    val getSearchProductUseCase: SearchProductUseCase,
) : ViewModel() {

    private val _searchState = MutableStateFlow<Result<List<Product>>>(Result.Idle)
    val searchState = _searchState.asStateFlow()

    private var searchJob: Job? = null

    private val allowedCategories = listOf(
        "mens-shirts", "mens-shoes", "mens-watches",
        "womens-dresses", "womens-shoes", "womens-bags", "womens-jewellery", "womens-watches",
        "sunglasses", "tops", "beauty", "fragrances", "skin-care"
    )

    private var currentProductList: List<Product> = emptyList()
    private var originalProductList: List<Product> = emptyList()

    private var minPrice: Double = 0.0
    private var maxPrice: Double = Double.MAX_VALUE

    private val _isFilterApplied = MutableStateFlow(false)
    val isFilterApplied = _isFilterApplied.asStateFlow()


    fun searchProducts(query: String, showAllOnBlank: Boolean = true, order: Boolean? = null) {

        if (query.isBlank()) {
            searchJob?.cancel()

            if (!showAllOnBlank) {
                _searchState.value = Result.Idle
            }
            return
        }

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(400)
            _searchState.value = Result.Loading
            try {
                if (currentProductList.isNotEmpty()) {
                    val sortedData = if (order == true) {
                        currentProductList.sortedBy { it.price }
                    } else {
                        currentProductList.sortedByDescending { it.price }
                    }

                    currentProductList = sortedData
                    _searchState.value = Result.Success(sortedData)
                } else {
                    val orderType = if (order == true) "asc" else "desc"

                    val searchData =
                        getSearchProductUseCase(query, showAllOnBlank, order = orderType)

                    when (searchData) {
                        is Result.Success -> {
                            val filteredProducts = filterProductsByCategory(searchData.data)
                            currentProductList = filteredProducts
                            originalProductList = filteredProducts
                            _searchState.value = Result.Success(filteredProducts)
                        }

                        is Result.Idle -> {
                            _searchState.value = Result.Idle
                        }

                        is Result.Loading -> {
                            _searchState.value = Result.Loading
                        }

                        is Result.Error -> {
                            emptyList<Product>()
                            _searchState.value = Result.Error(searchData.message)
                        }
                    }
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _searchState.value = Result.Error(e.localizedMessage ?: "Error occurred")
            }
        }
    }

    fun filterProducts(
        min: Double? = null,
        max: Double? = null,
        selectedRating: Int? = null
    ) {
        viewModelScope.launch {
            minPrice = min ?: 0.0
            maxPrice = max ?: Double.MAX_VALUE

            _searchState.value = Result.Loading
            val productList = originalProductList
            if (productList.isNotEmpty()) {

                var filteredProducts = productList
                filteredProducts = if (minPrice != 0.0 || maxPrice != Double.MAX_VALUE) {
                    filterProductsByPrice(products = productList, minPrice, maxPrice)
                } else {
                    filteredProducts
                }

                filteredProducts = if (selectedRating != null) {
                    filterProductsByRating(filteredProducts, selectedRating)
                } else {
                    filteredProducts
                }

                currentProductList = filteredProducts

                _searchState.value = Result.Success(filteredProducts)

                _isFilterApplied.value = true
            }
        }
    }

    fun resetFilters() {
        minPrice = 0.0
        maxPrice = Double.MAX_VALUE
        _isFilterApplied.value = false
        currentProductList = originalProductList
        _searchState.value = Result.Success(currentProductList)
    }

    private fun filterProductsByCategory(products: List<Product>): List<Product> {
        return products.filter { product ->
            product.category.lowercase() in allowedCategories
        }
    }

    private fun filterProductsByPrice(
        products: List<Product>,
        minPrice: Double,
        maxPrice: Double
    ): List<Product> {
        return products.filter { product ->
            val productPrice = product.price * 141

            productPrice in minPrice..maxPrice
        }
    }

    private fun filterProductsByRating(
        products: List<Product>,
        selectedRating: Int
    ): List<Product> {
        return products.filter { product ->
            product.rating >= selectedRating
        }
    }

    fun resetState() {
        _searchState.value = Result.Idle
    }


}