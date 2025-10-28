package com.example.ecommerce.data.remote

import com.example.ecommerce.data.dto.productdto.ProductDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ProductApiServices(
    val httpClient: HttpClient
) {
    suspend fun getProducts(limit: Int = 0): ProductDto {
        return httpClient.get("/products") {
            parameter("limit", limit)
        }.body()
    }

    suspend fun searchProducts(query: String): ProductDto {
        return httpClient.get("/products/search") {
            parameter("q", query)
        }.body()
    }
}