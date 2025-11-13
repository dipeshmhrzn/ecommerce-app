package com.example.ecommerce.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.domain.usecase.productusecase.GetProductsUseCase
import com.example.ecommerce.domain.usecase.productusecase.SortProductUseCase
import com.example.ecommerce.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductViewModel @Inject constructor(
    val getProductsUseCase: GetProductsUseCase,
    val sortProductUseCase: SortProductUseCase
) : ViewModel() {

    private val _productState = MutableStateFlow<Result<List<Product>>>(Result.Idle)
    val productState = _productState.asStateFlow()

    private var currentProductList: List<Product> = emptyList()

    private var minPrice: Double = 0.0
    private var maxPrice: Double = Double.MAX_VALUE

    private val _isFilterApplied = MutableStateFlow(false)
    val isFilterApplied = _isFilterApplied.asStateFlow()

    private val allowedCategories = listOf(
        "mens-shirts", "mens-shoes", "mens-watches",
        "womens-dresses", "womens-shoes", "womens-bags", "womens-jewellery", "womens-watches",
        "sunglasses", "tops", "beauty", "fragrances", "skin-care"
    )


    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            _productState.value = Result.Loading
            val productsData = getProductsUseCase()
            try {
                when (productsData) {
                    is Result.Success -> {
                        val filteredProducts =
                            filterProductsByCategory(productsData.data, allowedCategories)
                        currentProductList = filteredProducts
                        _productState.value = Result.Success(filteredProducts)
                    }

                    is Result.Idle -> {
                        _productState.value = Result.Idle
                    }

                    is Result.Loading -> {
                        _productState.value = Result.Loading
                    }

                    is Result.Error -> {
                        emptyList<Product>()
                        _productState.value = Result.Error(productsData.message)
                    }
                }
                Log.d("ProductViewModel", "getProducts: $productsData")
            } catch (e: Exception) {
                Log.d("ProductViewModel", "Error: ${e.localizedMessage}")
                _productState.value = Result.Error(e.localizedMessage ?: "Error occurred")
            }
        }

    }

    fun filterProductsByPrice(
        min: Double? = null,
        max: Double? = null,
        selectedRating: Int? = null
    ) {
        viewModelScope.launch {

            minPrice = min ?: 0.0
            maxPrice = max ?: Double.MAX_VALUE

            _productState.value = Result.Loading
            val productsData = getProductsUseCase()
            try {
                when (productsData) {
                    is Result.Success -> {
                        var filteredProducts =
                            filterProductsByCategory(productsData.data, allowedCategories)
                        filteredProducts = if (minPrice != 0.0 || maxPrice != Double.MAX_VALUE) {
                            filterProductsByPrice(filteredProducts, minPrice, maxPrice)
                        } else {
                            filteredProducts
                        }

                        filteredProducts = if (selectedRating != null) {
                            filterProductsByRating(filteredProducts, selectedRating)
                        } else {
                            filteredProducts
                        }

                        currentProductList = filteredProducts

                        _productState.value = Result.Success(filteredProducts)

                        _isFilterApplied.value = true

                    }

                    is Result.Idle -> {
                        _productState.value = Result.Idle
                    }

                    is Result.Loading -> {
                        _productState.value = Result.Loading
                    }

                    is Result.Error -> {
                        emptyList<Product>()
                        _productState.value = Result.Error(productsData.message)
                    }
                }
                Log.d("ProductViewModel", "getProducts: $productsData")
            } catch (e: Exception) {
                Log.d("ProductViewModel", "Error: ${e.localizedMessage}")
                _productState.value = Result.Error(e.localizedMessage ?: "Error occurred")
            }
        }
    }


    fun resetFilters() {
        minPrice = 0.0
        maxPrice = Double.MAX_VALUE
        _isFilterApplied.value = false
        Log.d("ProductViewModel", "resetFilters: ${_isFilterApplied.value}")
        getProducts()
    }

    fun sortProducts(order: Boolean) {
        viewModelScope.launch {
            _productState.value = Result.Loading
            try {
                if (currentProductList.isNotEmpty()) {
                    val sortedData = if (order) {
                        currentProductList.sortedBy { it.price }
                    } else {
                        currentProductList.sortedByDescending { it.price }
                    }

                    currentProductList = sortedData
                    _productState.value = Result.Success(sortedData)
                } else {
                    val orderType = if (order) "asc" else "desc"
                    val sortData = sortProductUseCase(orderType)
                    when (sortData) {
                        is Result.Success -> {
                            val filteredProducts =
                                filterProductsByCategory(sortData.data, allowedCategories)
                            _productState.value = Result.Success(filteredProducts)
                        }

                        is Result.Idle -> {
                            _productState.value = Result.Idle
                        }

                        is Result.Loading -> {
                            _productState.value = Result.Loading
                        }

                        is Result.Error -> {
                            emptyList<Product>()
                            _productState.value = Result.Error(sortData.message)
                        }
                    }
                    Log.d("ProductViewModel", "getProducts: $sortData")
                }
            } catch (e: Exception) {
                Log.d("ProductViewModel", "Error: ${e.localizedMessage}")
                _productState.value = Result.Error(e.localizedMessage ?: "Error occurred")
            }
        }
    }

    private fun filterProductsByCategory(
        products: List<Product>,
        allowedCategories: List<String>
    ): List<Product> {
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
        _productState.value = Result.Idle
    }


}