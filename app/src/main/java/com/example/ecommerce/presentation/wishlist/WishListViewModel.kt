package com.example.ecommerce.presentation.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.domain.usecase.wishlistusecase.WishlistUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.category

data class WishlistState(
    val allProducts: List<Product> = emptyList(),
    val filteredProducts: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

@HiltViewModel
class WishListViewModel @Inject constructor(
    private val wishlistUseCases: WishlistUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(WishlistState())
    val state = _state.asStateFlow()


    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.value = state.value.copy(
                isLoading = true
            )
            try {
                wishlistUseCases.getAllWishlistItems().collect() { products ->
                    _state.value = _state.value.copy(
                        allProducts = products,
                        filteredProducts = products,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load wishlist"
                )
            }
        }
    }

    fun addToWishlist(product: Product) {

        viewModelScope.launch {
            try {
                wishlistUseCases.insertIntoWishlist(product)
                _uiEvent.emit("Added to wishlist \nsuccessfully !!")
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to add to wishlist"
                )
            }
        }
    }

    fun deleteFromWishList(id: Int) {
        viewModelScope.launch {
            try {
                wishlistUseCases.deleteFromWishlist(id)
                _uiEvent.emit("Deleted item from wishlist \nsuccessfully !!")
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to remove item from wishlist"
                )
            }
        }
    }

    fun deleteAllFromWishlist() {
        viewModelScope.launch {
            try {
                wishlistUseCases.deleteAllWishlistItems()
                _uiEvent.emit("All items removed from wishlist \nsuccessfully!!")
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to clear wishlist"
                )
            }
        }
    }

    suspend fun isItemInWishlist(productId: Int): Boolean {
        return try {
            wishlistUseCases.isItemInWishlist(productId)
        } catch (e: Exception) {
            false
        }
    }

    fun searchWishListItems(query: String) {
        _state.value = _state.value.copy(
            searchQuery = query
        )

        val filteredProducts = if (query.isBlank()) {
            _state.value.allProducts
        } else {
            _state.value.allProducts.filter { product ->
                product.title.contains(query, ignoreCase = true) ||
                        product.description.contains(query, ignoreCase = true) ||
                        product.category.contains(query, ignoreCase = true)
            }
        }

        _state.value = _state.value.copy(
            filteredProducts = filteredProducts
        )
    }

}