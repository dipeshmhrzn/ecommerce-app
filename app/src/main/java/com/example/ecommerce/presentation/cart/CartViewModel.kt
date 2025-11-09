package com.example.ecommerce.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.domain.model.CartItem
import com.example.ecommerce.domain.usecase.cartusecase.AddToCartUseCase
import com.example.ecommerce.domain.usecase.cartusecase.GetCartItemsUseCase
import com.example.ecommerce.domain.usecase.cartusecase.RemoveSelectedCartItemsUseCase
import com.example.ecommerce.domain.usecase.cartusecase.UpdateCartQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

data class CartState(
    val cartItems: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalPrice: Int = 0,
    val totalItems: Int = 0,
    val selectedItems: Set<Int> = emptySet(),
    val isAllSelected:Boolean=false
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val removeSelectedCartItemsUseCase: RemoveSelectedCartItemsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        getCartItems()
    }

    private fun recalcTotals() {
        val selectedItems =
            _state.value.cartItems.filter { _state.value.selectedItems.contains(it.product.id) }
        val totalItems = selectedItems.sumOf { it.quantity }
        val totalPrice =selectedItems.sumOf { (it.product.price.roundToInt() * 141) * it.quantity }

        _state.value = _state.value.copy(
            totalItems = totalItems,
            totalPrice = totalPrice
        )
    }

    fun toggleSelection(productId: Int) {
        val jsonItems = _state.value.selectedItems.toMutableSet()
        if (jsonItems.contains(productId)) {
            jsonItems.remove(productId)
        } else {
            jsonItems.add(productId)
        }


        val allSelected = jsonItems.size == _state.value.cartItems.size && _state.value.cartItems.isNotEmpty()

        _state.value = _state.value.copy(
            selectedItems = jsonItems,
            isAllSelected = allSelected
        )
        recalcTotals()
    }

    fun toggleAllSelection(){

        val allSelected = _state.value.cartItems.map { it.product.id }.toSet()
        val newSelection = if (_state.value.selectedItems.size==_state.value.cartItems.size){
            emptySet()
        }else{
            allSelected
        }

        _state.value = _state.value.copy(
            selectedItems = newSelection,
            isAllSelected = newSelection.size == _state.value.cartItems.size
        )

        recalcTotals()
    }


    fun removeSelectedItems() {
        viewModelScope.launch {
            try {
                val selectedIds = _state.value.selectedItems

                if (selectedIds.isEmpty()) {
                    _uiEvent.emit("Please select items to remove!")
                    return@launch
                }

                val removedCount = _state.value.cartItems
                    .filter { selectedIds.contains(it.product.id) }
                    .sumOf { it.quantity }

                removeSelectedCartItemsUseCase(selectedIds.toList())

                val updatedCartItems =
                    _state.value.cartItems.filter { !selectedIds.contains(it.product.id) }
                _state.value = _state.value.copy(
                    cartItems = updatedCartItems,
                    selectedItems = emptySet()
                )
                recalcTotals()
                _uiEvent.emit("Removed $removedCount ${if (removedCount == 1) "item" else "items"} from cart successfully!")
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.localizedMessage ?: "Failed to remove selected items"
                )
            }
        }
    }


    private fun getCartItems() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                getCartItemsUseCase().collect { items ->

                    _state.value = _state.value.copy(
                        cartItems = items,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.localizedMessage ?: "Failed to get cart items",
                    isLoading = false
                )
            }
        }
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            try {
                addToCartUseCase(product, quantity)
                _uiEvent.emit("Added to cart \nsuccessfully !!")
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.localizedMessage ?: "Failed to add to cart"
                )
            }
        }
    }

    fun updateItemQuantity(productId: Int, newQuantity: Int) {
        viewModelScope.launch {
            try {
                updateCartQuantityUseCase(productId, newQuantity)

                val updatedCartItems = _state.value.cartItems.map {
                    if (it.product.id == productId) it.copy(quantity = newQuantity) else it
                }
                _state.value = _state.value.copy(cartItems = updatedCartItems)

                recalcTotals()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.localizedMessage ?: "Failed to update quantity"
                )
            }
        }
    }

}