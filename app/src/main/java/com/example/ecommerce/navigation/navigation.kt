package com.example.ecommerce.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ecommerce.presentation.auth.ForgotPasswordScreen
import com.example.ecommerce.presentation.auth.LoginScreen
import com.example.ecommerce.presentation.auth.RegisterScreen
import com.example.ecommerce.presentation.home.HomeScreen
import com.example.ecommerce.presentation.home.ProductDetailScreen
import com.example.ecommerce.presentation.onboardingscreen.OnBoardScreen1
import com.example.ecommerce.presentation.onboardingscreen.OnBoardScreen3
import com.example.ecommerce.presentation.onboardingscreen.OnScreenBoard2
import com.example.ecommerce.presentation.search.SearchScreen
import com.example.ecommerce.presentation.splashscreen.SplashScreen
import com.example.ecommerce.presentation.userpreferences.UserPreferencesViewModel
import com.example.ecommerce.presentation.wishlist.WishlistScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    val userPreferencesViewModel : UserPreferencesViewModel = viewModel()

    val userPreferencesState by userPreferencesViewModel.state.collectAsState()

    Log.d("Navigation", "UserPreferencesState: isLoading = ${userPreferencesState.isLoading}")

    NavHost(navController = navController, startDestination = Routes.SplashScreen) {

        composable<Routes.SplashScreen> {
            SplashScreen(
                isLoading = userPreferencesState.isLoading,
                onFinish = {
                    val destination = when {
                        userPreferencesState.isLoggedIn -> Routes.HomeScreen
                        userPreferencesState.isFirstTimeLogin -> Routes.OnBoardingScreen1
                        else -> Routes.LoginScreen
                    }
                    Log.d("SplashScreen", "Navigating to: $destination")
                    navController.navigate(destination) {
                        popUpTo(Routes.SplashScreen) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Routes.OnBoardingScreen1> {
            OnBoardScreen1(navHostController = navController)
        }

        composable<Routes.OnBoardingScreen2> {
            OnScreenBoard2(navHostController = navController)
        }

        composable<Routes.OnBoardingScreen3> {
            OnBoardScreen3(navHostController = navController)
        }

        composable<Routes.LoginScreen> {
            userPreferencesViewModel.setFirstTimeLogin(false)
            LoginScreen(navHostController = navController)
        }

        composable<Routes.RegisterScreen> {
            RegisterScreen(navHostController = navController)
        }

        composable<Routes.ForgotPasswordScreen> {
            ForgotPasswordScreen(navHostController = navController)
        }

        composable<Routes.HomeScreen> {
            HomeScreen(navHostController = navController)
        }

        composable<Routes.ProductDetailScreen> {backStackEntry ->
            val product =backStackEntry.toRoute<Routes.ProductDetailScreen>()
            ProductDetailScreen(product.id, navHostController = navController)
        }

        composable<Routes.SearchScreen>{
            SearchScreen(navHostController = navController)
        }

        composable<Routes.WishlistScreen> {
            WishlistScreen(navHostController = navController)
        }

    }
}