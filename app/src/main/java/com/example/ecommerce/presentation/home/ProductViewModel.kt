package com.example.ecommerce.presentation.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.dto.productdto.ProductDto
import com.example.ecommerce.domain.usecase.GetProductsUseCase
import com.example.ecommerce.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductViewModel @Inject constructor(
    val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _productState = MutableStateFlow<Result<ProductDto>>(Result.Idle)
    val productState = _productState.asStateFlow()

    var totalItems by mutableStateOf("")


    init {
        getProducts()
    }

    fun getProducts(){
        _productState.value = Result.Loading
        viewModelScope.launch {
            try {
                val productsData = getProductsUseCase()
                _productState.value = Result.Success(productsData)
                totalItems=productsData.total.toString()
                Log.d("ProductViewModel", "getProducts: $productsData")
            }catch (e: Exception){
                Log.d("ProductViewModel", "Error: ${e.localizedMessage}")
                _productState.value = Result.Error(e.localizedMessage ?: "Error occurred")
            }
        }

    }


}