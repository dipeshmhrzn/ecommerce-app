package com.example.ecommerce.data.local.converter

import androidx.room.TypeConverter
import com.example.ecommerce.data.dto.productdto.Dimensions
import com.example.ecommerce.data.dto.productdto.Meta
import com.example.ecommerce.data.dto.productdto.Review
import kotlinx.serialization.json.Json

class ProductConverters {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @TypeConverter
    fun fromListToString(value: List<String>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToList(value: String): List<String> {
        return try {
            json.decodeFromString<List<String>>(value)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Convert Dimensions object to String
    @TypeConverter
    fun fromDimensions(value: Dimensions): String {
        return json.encodeToString(value)
    }

    // Convert String back to Dimensions object
    @TypeConverter
    fun toDimensions(value: String): Dimensions {
        return json.decodeFromString(value)
    }

    // Convert Meta object to String
    @TypeConverter
    fun fromMeta(value: Meta): String {
        return json.encodeToString(value)
    }

    // Convert String back to Meta object
    @TypeConverter
    fun toMeta(value: String): Meta {
        return json.decodeFromString(value)
    }

    // Convert Review object to String
    @TypeConverter
    fun fromReviewList(value: List<Review>): String {
        return json.encodeToString(value)
    }

    // Convert String back to Review object
    @TypeConverter
    fun toReviewList(value: String): List<Review> {
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }
}