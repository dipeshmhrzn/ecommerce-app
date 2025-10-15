package com.example.ecommerce.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecommerce.data.local.UserPreferencesDataStore
import com.example.ecommerce.data.repositoryimpl.AuthRepositoryImpl
import com.example.ecommerce.data.repositoryimpl.UserPreferencesRepositoryImpl
import com.example.ecommerce.domain.repository.UserPreferencesRepository
import com.example.ecommerce.domain.usecase.GetUserPreferencesUseCase
import com.example.ecommerce.domain.usecase.LoginUseCase
import com.example.ecommerce.domain.usecase.SetUserPreferenceUseCase
import com.example.ecommerce.domain.usecase.SignupUseCase
import com.example.ecommerce.presentation.auth.AuthViewModel
import com.example.ecommerce.presentation.auth.ForgotPasswordScreen
import com.example.ecommerce.presentation.auth.LoginScreen
import com.example.ecommerce.presentation.auth.RegisterScreen
import com.example.ecommerce.presentation.home.HomeScreen
import com.example.ecommerce.presentation.onboardingscreen.OnBoardScreen1
import com.example.ecommerce.presentation.onboardingscreen.OnBoardScreen3
import com.example.ecommerce.presentation.onboardingscreen.OnScreenBoard2
import com.example.ecommerce.presentation.splashscreen.SplashScreen
import com.example.ecommerce.presentation.userpreferences.UserPreferencesViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val userPreferencesDataStore = remember { UserPreferencesDataStore(context) }
    val userPreferencesRepo = remember { UserPreferencesRepositoryImpl(userPreferencesDataStore) }
    val getUserPreferencesUseCase = remember { GetUserPreferencesUseCase(userPreferencesRepo) }
    val setUserPreferencesUseCase = remember { SetUserPreferenceUseCase(userPreferencesRepo) }
    val userPreferencesViewModel = remember { UserPreferencesViewModel(getUserPreferencesUseCase, setUserPreferencesUseCase) }


    val authRepo = remember { AuthRepositoryImpl(FirebaseAuth.getInstance()) }
    val loginUseCase = remember { LoginUseCase(authRepo) }
    val signupUseCase = remember { SignupUseCase(authRepo) }
    val authViewModel = remember { AuthViewModel(loginUseCase, signupUseCase, userPreferencesViewModel) }

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
            LoginScreen(navHostController = navController, authViewModel = authViewModel)
        }

        composable<Routes.RegisterScreen> {
            RegisterScreen(navHostController = navController, authViewModel = authViewModel)
        }

        composable<Routes.ForgotPasswordScreen> {
            ForgotPasswordScreen(navHostController = navController)
        }

        composable<Routes.HomeScreen> {
            HomeScreen()
        }

    }
}