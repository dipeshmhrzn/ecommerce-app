package com.example.ecommerce.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes{

    @Serializable
    data object SplashScreen: Routes()

    @Serializable
    data object OnBoardingScreen1: Routes()

    @Serializable
    data object OnBoardingScreen2: Routes()

    @Serializable
    data object OnBoardingScreen3: Routes()

    @Serializable
    data object LoginScreen: Routes()

    @Serializable
    data object RegisterScreen: Routes()

    @Serializable
    data object ForgotPasswordScreen: Routes()

    @Serializable
    data object HomeScreen: Routes()

}