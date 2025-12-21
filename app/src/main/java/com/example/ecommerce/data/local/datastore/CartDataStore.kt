package com.example.ecommerce.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.domain.model.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class CartDataStore(
    private val context: Context
) {

    companion object {
        private val Context.cartDataStore: DataStore<Preferences> by preferencesDataStore("cart_preferences")
        private val CART_ITEMS = stringPreferencesKey("cart_items")
    }

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    val cartItems: Flow<List<CartItem>> = context.cartDataStore.data.map { preferences ->
        val jsonItems = preferences[CART_ITEMS] ?: "[]"
        try {
            json.decodeFromString<List<CartItem>>(jsonItems)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addToCart(product: Product, quantity: Int = 1) {
        context.cartDataStore.edit { preferences ->
            val jsonItems = preferences[CART_ITEMS] ?: "[]"
            val currentItems = try {
                json.decodeFromString<List<CartItem>>(jsonItems)
            } catch (e: Exception) {
                emptyList()
            }

            val existingItemIndex = currentItems.indexOfFirst { it.product.id == product.id }
            val updatedItems = if (existingItemIndex != -1) {
                currentItems.toMutableList().apply {
                    this[existingItemIndex] = this[existingItemIndex].copy(
                        quantity = this[existingItemIndex].quantity + quantity
                    )
                }
            } else {
                currentItems + CartItem(product, quantity)
            }

            preferences[CART_ITEMS] = json.encodeToString(updatedItems)

        }

    }

    suspend fun updateQuantity(productId: Int, newQuantity: Int) {
        context.cartDataStore.edit { preferences ->
            val jsonItems = preferences[CART_ITEMS] ?: "[]"
            val currentItems = try {
                json.decodeFromString<List<CartItem>>(jsonItems)
            } catch (e: Exception) {
                emptyList()
            }

            val updatedItems = currentItems.map { item ->
                if (item.product.id == productId) {
                    item.copy(quantity = newQuantity)
                } else {
                    item
                }
            }.filter { it.quantity > 0 }
            preferences[CART_ITEMS] = json.encodeToString(updatedItems)
        }

    }


    suspend fun getCartItemCount(): Int {
        var count = 0
        context.cartDataStore.data.map { preferences ->
            val jsonItems = preferences[CART_ITEMS] ?: "[]"
            val currentItems = try {
                json.decodeFromString<List<CartItem>>(jsonItems)
            } catch (e: Exception) {
                emptyList()
            }
            count = currentItems.sumOf { it.quantity }
        }.collect { }
        return count
    }

    suspend fun removeSelectedItemsFromCart(productIds: List<Int>) {
        context.cartDataStore.edit { preferences ->
            val currentJson = preferences[CART_ITEMS] ?: "[]"
            val currentItems = try {
                json.decodeFromString<List<CartItem>>(currentJson)
            } catch (e: Exception) {
                emptyList()
            }

            // Filter out items whose productId is in productIds list
            val updatedItems = currentItems.filter { it.product.id !in productIds }
            preferences[CART_ITEMS] = json.encodeToString(updatedItems)
        }
    }


}