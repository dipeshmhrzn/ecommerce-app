package com.example.ecommerce.presentation.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.data.dto.productdto.ProductDto
import com.example.ecommerce.domain.usecase.GetProductsUseCase
import com.example.ecommerce.domain.usecase.SearchProductUseCase
import com.example.ecommerce.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.category
import kotlin.text.lowercase


@HiltViewModel
class ProductViewModel @Inject constructor(
    val getProductsUseCase: GetProductsUseCase,
    val getSearchProductUseCase: SearchProductUseCase
) : ViewModel() {

    private val _productState = MutableStateFlow<Result<List<Product>>>(Result.Idle)
    val productState = _productState.asStateFlow()

    private var seachJob: Job? =null

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
                        val filteredProducts = filterProductsByCategory(productsData.data)
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

    fun searchProducts(query :String){
        seachJob?.cancel()

        seachJob =viewModelScope.launch {
            delay(400)
            _productState.value=Result.Loading
            val searchData = getSearchProductUseCase(query)
            try {
                when (searchData) {
                    is Result.Success -> {
                        val filteredProducts = filterProductsByCategory(searchData.data)
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
                        _productState.value = Result.Error(searchData.message)
                    }
                }

            }catch (e: Exception){
                _productState.value=Result.Error(e.localizedMessage ?: "Error occurred")
            }
        }
    }

    private fun filterProductsByCategory(products: List<Product>): List<Product> {
        return products.filter { product ->
            product.category.lowercase() in allowedCategories
        }
    }


}