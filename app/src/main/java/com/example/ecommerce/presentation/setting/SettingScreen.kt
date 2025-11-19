package com.example.ecommerce.presentation.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ecommerce.domain.util.Result
import com.example.ecommerce.navigation.Routes
import com.example.ecommerce.presentation.auth.AuthViewModel

@Composable
fun SettingsScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val authState by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { viewModel.logout() }
        ) {
            Text("Logout")
        }
    }

    // Observe logout result
    when (authState) {
        is Result.Loading -> CircularProgressIndicator()

        is Result.Success -> {
            LaunchedEffect(Unit) {
                navHostController.navigate(Routes.LoginScreen){
                    popUpTo(Routes.SettingScreen){
                        inclusive=true
                    }
                }
                viewModel.resetAuthState()
            }
        }

        is Result.Error -> {
            Text(
                text = (authState as Result.Error).message.toString(),
                color = Color.Red
            )
        }

        Result.Idle -> Unit
    }
}
