package com.example.ecommerce.presentation.onboardingscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ecommerce.R
import com.example.ecommerce.navigation.Routes
import com.example.ecommerce.presentation.onboardingscreen.onboardingcomponents.BottomBar
import com.example.ecommerce.presentation.onboardingscreen.onboardingcomponents.MiddleContext
import com.example.ecommerce.presentation.onboardingscreen.onboardingcomponents.TopBar
import com.example.ecommerce.ui.theme.Montserrat

@Composable
fun OnBoardScreen3(navHostController: NavHostController) {
    Scaffold(
        topBar = {
            TopBar(
                pageNumber = "3",
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
                isLastPage = true,
                onNextClick = {
                    navHostController.navigate(Routes.LoginScreen) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                onPrevClick = {
                    navHostController.navigate(Routes.OnBoardingScreen2)
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {

        MiddleContext(
            paddingValues = it,
            imageRes = R.drawable.onboardimage3,
            title = "Get Your Order",
            description = stringResource(R.string.onboarding_description)
        )

    }
}

@Composable
@Preview(showBackground = true)
fun OnBoardPreview3() {

    MaterialTheme {
        OnBoardScreen3(navHostController = rememberNavController())
    }
}