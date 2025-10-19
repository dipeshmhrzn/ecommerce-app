package com.example.ecommerce.di

import android.content.Context
import com.example.ecommerce.data.local.UserPreferencesDataStore
import com.example.ecommerce.data.remote.ProductApiServices
import com.example.ecommerce.data.repositoryimpl.AuthRepositoryImpl
import com.example.ecommerce.data.repositoryimpl.ProductRepositoryImpl
import com.example.ecommerce.data.repositoryimpl.UserPreferencesRepositoryImpl
import com.example.ecommerce.domain.repository.AuthRepository
import com.example.ecommerce.domain.repository.ProductRepository
import com.example.ecommerce.domain.repository.UserPreferencesRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.basicAuth
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(@ApplicationContext context: Context): UserPreferencesDataStore {
        return UserPreferencesDataStore(context)
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(userPreferencesDataStore: UserPreferencesDataStore): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(userPreferencesDataStore)
    }

    @Provides
    @Singleton
    fun provideProductHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 15000
                connectTimeoutMillis = 15000
                socketTimeoutMillis = 15000
            }

            defaultRequest {

                url {
                    protocol= URLProtocol.HTTPS
                    host="dummyjson.com"
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideProductApiServices(httpClient: HttpClient): ProductApiServices{
        return ProductApiServices(httpClient)
    }

    @Provides
    @Singleton
    fun provideProductRepository(apiServices: ProductApiServices): ProductRepository{
        return ProductRepositoryImpl(apiServices)
    }

}