package com.example.ecommerce.data.repositoryimpl

import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.data.remote.ProductApiServices
import com.example.ecommerce.domain.repository.ProductRepository
import com.example.ecommerce.domain.util.Result

class ProductRepositoryImpl(
    private val apiServices: ProductApiServices
) : ProductRepository {

    override suspend fun getProducts(): Result<List<Product>>{
        return try {
            val productResponse = apiServices.getProducts()
            Result.Success(productResponse.products)
        } catch (e: Exception) {
            Result.Error("Error occurred : ${e.localizedMessage}")
        }
    }

    override suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val searchResponse =apiServices.searchProducts(query)
            Result.Success(searchResponse.products)
        }catch (e: Exception){
            Result.Error("Error occurred : ${e.localizedMessage}")
        }
    }


}