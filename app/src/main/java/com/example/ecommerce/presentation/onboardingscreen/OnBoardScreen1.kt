package com.example.ecommerce.presentation.onboardingscreen

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ecommerce.R
import com.example.ecommerce.navigation.Routes
import com.example.ecommerce.presentation.onboardingscreen.onboardingcomponents.BottomBar
import com.example.ecommerce.presentation.onboardingscreen.onboardingcomponents.MiddleContext
import com.example.ecommerce.presentation.onboardingscreen.onboardingcomponents.TopBar

@Composable
fun OnBoardScreen1(navHostController: NavHostController) {
    Scaffold(
        topBar = {
            TopBar(
                pageNumber = "1",
                onSkipClick = {
                    navHostController.navigate(Routes.LoginScreen) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                isFirstPage = true,
                onNextClick = {
                    navHostController.navigate(Routes.OnBoardingScreen2)
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        MiddleContext(
            paddingValues = it,
            imageRes = R.drawable.onboardimage1,
            title = "Choose Products",
            description = stringResource(R.string.onboarding_description)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun OnBoardPreview1() {

    MaterialTheme {
        OnBoardScreen1(navHostController = rememberNavController())
    }
}