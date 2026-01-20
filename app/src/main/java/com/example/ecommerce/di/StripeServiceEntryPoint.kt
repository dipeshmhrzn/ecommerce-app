package com.example.ecommerce.di


import com.example.ecommerce.data.remote.StripeService
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface StripeServiceEntryPoint {
    fun stripeService(): StripeService
}