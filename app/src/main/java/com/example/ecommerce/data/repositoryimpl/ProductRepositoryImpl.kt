package com.example.ecommerce.data.repositoryimpl

import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.data.remote.ProductApiServices
import com.example.ecommerce.domain.repository.ProductRepository
import com.example.ecommerce.domain.util.Result
import kotlin.coroutines.cancellation.CancellationException

class ProductRepositoryImpl(
    private val apiServices: ProductApiServices
) : ProductRepository {

    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val productResponse = apiServices.getProducts()
            Result.Success(productResponse.products)
        } catch (e: Exception) {
            Result.Error("Error occurred : ${e.localizedMessage}")
        }
    }

    override suspend fun searchProducts(query: String, order: String): Result<List<Product>> {
        return try {
            val searchResponse = apiServices.searchProducts(query = query, order = order)
            Result.Success(searchResponse.products)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.Error("Error occurred : ${e.localizedMessage}")
        }
    }

    override suspend fun sortProduct(order: String): Result<List<Product>> {
        return try {
            val sortResponse = apiServices.sortProducts(order = order)
            Result.Success(sortResponse.products)
        }catch (e:Exception){
            Result.Error("Error occurred : ${e.localizedMessage}")
        }
    }

}