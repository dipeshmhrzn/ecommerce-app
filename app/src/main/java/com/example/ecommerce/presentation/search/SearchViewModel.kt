package com.example.ecommerce.presentation.search

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

    fun searchProducts(query: String, showAllOnBlank: Boolean = true) {

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
            val searchData = getSearchProductUseCase(query,showAllOnBlank)
            try {
                when (searchData) {
                    is Result.Success -> {
                        val filteredProducts = filterProductsByCategory(searchData.data)
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
            catch (e: CancellationException) {
                throw e
            }
            catch (e: Exception) {
                _searchState.value = Result.Error(e.localizedMessage ?: "Error occurred")
            }
        }
    }

    private fun filterProductsByCategory(products: List<Product>): List<Product> {
        return products.filter { product ->
            product.category.lowercase() in allowedCategories
        }
    }

    fun resetState() {
        _searchState.value = Result.Idle
    }


}