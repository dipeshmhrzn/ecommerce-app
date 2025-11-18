package com.example.ecommerce.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ecommerce.domain.util.Result
import com.example.ecommerce.domain.util.ValidationErrors
import com.example.ecommerce.navigation.Routes
import com.example.ecommerce.presentation.auth.authcomponents.CustomButton
import com.example.ecommerce.presentation.auth.authcomponents.CustomText
import com.example.ecommerce.presentation.auth.authcomponents.CustomTextField
import com.example.ecommerce.ui.theme.Montserrat
import kotlinx.coroutines.delay

@Composable
fun ForgotPasswordScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    var username by remember { mutableStateOf("") }

    var isSuccess by remember { mutableStateOf(false) }


    val context = LocalContext.current

    val authState by authViewModel.authState.collectAsState()

    var emailError by remember { mutableStateOf<String?>(null) }

    var countdown by remember { mutableIntStateOf(60) }
    var isCountingDown by remember { mutableStateOf(false) }

    LaunchedEffect(isCountingDown, isSuccess) {
        if (isCountingDown && isSuccess) {
            while (countdown > 0) {
                delay(1000)
                countdown--
            }
            isCountingDown = false
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is Result.Success -> {
                Toast.makeText(
                    context, (authState as Result.Success<String>).data,
                    Toast.LENGTH_SHORT
                ).show()

                countdown = 60
                isCountingDown = true
                isSuccess = true

                authViewModel.resetAuthState()
            }

            is Result.Error -> {
                val error = (authState as Result.Error).message
                emailError = when (error) {
                    is ValidationErrors.EmailError -> {
                        error.message
                    }

                    is String -> {
                        error
                    }

                    else -> {
                        null
                    }
                }
                isCountingDown = false
            }

            else -> {

            }
        }
    }



    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            CustomText("Forgot")
            CustomText("Password?")
            Spacer(modifier = Modifier.height(40.dp))
            CustomTextField(
                value = username,
                onValueChange = {
                    username = it
                    emailError = null
                },
                placeholder = "Enter your email address",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                isError = emailError != null,
                supportingText = emailError
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))

                if (countdown == 0) {
                    Text(
                        text = "Resend link",
                        fontFamily = Montserrat,
                        fontSize = 16.sp,
                        color = Color(0xFFFF7622),
                        fontWeight = FontWeight.W600,
                        modifier = Modifier.clickable {
                            authViewModel.resetPassword(username)
                        }
                    )
                } else {
                    Text(
                        text = "Resend in ${countdown}s",
                        fontFamily = Montserrat,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.W400
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "* We will send you a message to set or reset your new password",
                fontFamily = Montserrat,
                fontSize = 14.sp,
                color = Color(0xFF676767),
            )
            Spacer(modifier = Modifier.height(40.dp))
            CustomButton(
                buttonText = if (isSuccess) "Go back to Login" else "SEND Link",
                onClick = {
                    if (isSuccess) {
                        navHostController.navigate(Routes.LoginScreen) {
                            popUpTo(Routes.LoginScreen) {
                                inclusive = true
                            }
                        }
                    } else {
                        authViewModel.resetPassword(username)
                    }
                }
            )
        }
    }
}

//@Composable
//@Preview(showBackground = true)
//fun ForgotPasswordPreview(modifier: Modifier = Modifier) {
//    MaterialTheme {
//        ForgotPasswordScreen()
//    }
//}