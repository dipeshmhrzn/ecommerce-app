package com.example.ecommerce.di

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.room.Room
import com.example.ecommerce.R
import com.example.ecommerce.data.local.datastore.UserPreferencesDataStore
import com.example.ecommerce.data.local.dao.WishlistDao
import com.example.ecommerce.data.local.database.StylishDatabase
import com.example.ecommerce.data.local.datastore.CartDataStore
import com.example.ecommerce.data.remote.ProductApiServices
import com.example.ecommerce.data.repositoryimpl.AuthRepositoryImpl
import com.example.ecommerce.data.repositoryimpl.CartRepositoryImpl
import com.example.ecommerce.data.repositoryimpl.ProductRepositoryImpl
import com.example.ecommerce.data.repositoryimpl.UserPreferencesRepositoryImpl
import com.example.ecommerce.data.repositoryimpl.UserProfileRepositoryImplementation
import com.example.ecommerce.data.repositoryimpl.WishlistRepositoryImpl
import com.example.ecommerce.domain.repository.AuthRepository
import com.example.ecommerce.domain.repository.CartRepository
import com.example.ecommerce.domain.repository.ProductRepository
import com.example.ecommerce.domain.repository.UserPreferencesRepository
import com.example.ecommerce.domain.repository.UserProfileRepository
import com.example.ecommerce.domain.repository.WishlistRepository
import com.example.ecommerce.domain.usecase.wishlistusecase.DeleteAllWishlistItemsUseCase
import com.example.ecommerce.domain.usecase.wishlistusecase.DeleteFromWishlistUseCase
import com.example.ecommerce.domain.usecase.wishlistusecase.GetAllWishListItemsUseCase
import com.example.ecommerce.domain.usecase.wishlistusecase.InsertIntoWishlistUseCase
import com.example.ecommerce.domain.usecase.wishlistusecase.IsItemInWishlistUseCase
import com.example.ecommerce.domain.usecase.wishlistusecase.WishlistUseCases
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton
import com.example.ecommerce.BuildConfig
import io.github.jan.supabase.storage.Storage

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
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideUserProfileRepository(firestore: FirebaseFirestore, supabaseClient: SupabaseClient): UserProfileRepository {
        return UserProfileRepositoryImplementation(firestore,supabaseClient)
    }

    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
        return CredentialManager.create(context)
    }


    @Provides
    fun provideGoogleIdOption(
        @ApplicationContext context: Context
    ): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.web_client_id))
            .setAutoSelectEnabled(false)
            .setFilterByAuthorizedAccounts(false)
            .build()
    }


    @Provides
    fun provideGetCredentialRequest(
        googleIdOption: GetGoogleIdOption
    ): GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
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
                    protocol = URLProtocol.HTTPS
                    host = "dummyjson.com"
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideProductApiServices(httpClient: HttpClient): ProductApiServices {
        return ProductApiServices(httpClient)
    }

    @Provides
    @Singleton
    fun provideProductRepository(apiServices: ProductApiServices): ProductRepository {
        return ProductRepositoryImpl(apiServices)
    }

    @Provides
    @Singleton
    fun provideStylishDatabase(@ApplicationContext context: Context): StylishDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = StylishDatabase::class.java,
            name = "stylish_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWishlistDao(database: StylishDatabase): WishlistDao {
        return database.wishlistDao()
    }

    @Provides
    @Singleton
    fun provideWishlistRepository(wishlistDao: WishlistDao): WishlistRepository {
        return WishlistRepositoryImpl(wishlistDao)
    }

    @Provides
    @Singleton
    fun provideWishlistUseCases(repository: WishlistRepository): WishlistUseCases {
        return WishlistUseCases(
            getAllWishlistItems = GetAllWishListItemsUseCase(repository),
            insertIntoWishlist = InsertIntoWishlistUseCase(repository),
            deleteFromWishlist = DeleteFromWishlistUseCase(repository),
            deleteAllWishlistItems = DeleteAllWishlistItemsUseCase(repository),
            isItemInWishlist = IsItemInWishlistUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideCartDataStore(@ApplicationContext context: Context): CartDataStore {
        return CartDataStore(context)
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartDataStore: CartDataStore): CartRepository {
        return CartRepositoryImpl(cartDataStore)
    }

    @Provides
    @Singleton
    fun provideSupaBaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {

            install(Storage)

        }
    }


}