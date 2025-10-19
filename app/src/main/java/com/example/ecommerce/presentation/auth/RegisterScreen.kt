package com.example.ecommerce.presentation.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ecommerce.domain.util.Result
import com.example.ecommerce.domain.util.ValidationErrors
import com.example.ecommerce.navigation.Routes
import com.example.ecommerce.presentation.auth.authcomponents.CustomButton
import com.example.ecommerce.presentation.auth.authcomponents.CustomPasswordField
import com.example.ecommerce.presentation.auth.authcomponents.CustomText
import com.example.ecommerce.presentation.auth.authcomponents.CustomTextField
import com.example.ecommerce.presentation.auth.authcomponents.SocialLoginButtons
import com.example.ecommerce.ui.theme.Montserrat

@Composable
fun RegisterScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel= hiltViewModel()
) {

    LaunchedEffect(Unit) {
        authViewModel.resetAuthState()
    }


    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is Result.Success -> {
                navHostController.navigate(Routes.LoginScreen) {
                    popUpTo(Routes.RegisterScreen) {
                        inclusive = true
                    }
                    authViewModel.resetAuthState()
                }
            }

            is Result.Error -> {
                val error = (authState as Result.Error).message
                when (error) {
                    is ValidationErrors.EmailError -> {
                        emailError=error.message
                        passwordError=null
                        confirmPasswordError =null
                    }
                    is ValidationErrors.PasswordError -> {
                        passwordError=error.message
                        emailError=null
                        confirmPasswordError =null
                    }
                    is ValidationErrors.ConfirmPasswordError -> {
                        confirmPasswordError=error.message
                        passwordError=null
                        emailError=null
                    }
                    is String -> {
                        emailError=null
                        passwordError=null
                        confirmPasswordError =error
                    }
                    else -> {
                        emailError=null
                        passwordError=null
                        confirmPasswordError =null
                    }
                }
            }

            Result.Idle, Result.Loading -> {
                emailError=null
                passwordError=null
                confirmPasswordError =null
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
            CustomText("Create an")
            CustomText("account")
            Spacer(modifier = Modifier.height(50.dp))
            CustomTextField(
                value = username,
                onValueChange = {
                    username = it
                    emailError = null
                },
                placeholder = "Username or Email",
                leadingIcon = Icons.Default.Person,
                keyboardType = KeyboardType.Email,
                isError = emailError != null,
                supportingText = emailError,
                trailingIcon = if (emailError != null) {
                    {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else null
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomPasswordField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                placeholder = "Password",
                leadingIcon = Icons.Default.Lock,
                passwordVisible = passwordVisible,
                onVisibilityToggle = { passwordVisible = !passwordVisible },
                isError = passwordError!=null,
                supportingText = passwordError
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomPasswordField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = null
                },
                placeholder = "Confirm Password",
                leadingIcon = Icons.Default.Lock,
                passwordVisible = passwordVisible,
                onVisibilityToggle = { passwordVisible = !passwordVisible },
                isError = confirmPasswordError!=null,
                supportingText = confirmPasswordError
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "By clicking the Register",
                    color = Color(0xFF575757),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraLight,
                    fontFamily = Montserrat
                )
                Text(
                    text = " button, you agree ",
                    color = Color(0xFF575757),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraLight,
                    fontFamily = Montserrat
                )
            }
            Text(
                text = "to the public offer ",
                color = Color(0xFF575757),
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraLight,
                fontFamily = Montserrat
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomButton(
                buttonText = "Create Account",
                isLoading = authState is Result.Loading,
                onClick = {
                    authViewModel.signup(username, password, confirmPassword)
                })
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = "- OR Continue with -",
                modifier = Modifier.fillMaxWidth(),
                fontFamily = Montserrat,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF575757)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
            ) {
                SocialLoginButtons()
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "I Already Have an Account ",
                    color = Color(0xFF575757),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Montserrat
                )
                Text(
                    text = "Login",
                    color = Color(0xFFFF4757),
                    fontSize = 14.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Medium,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        navHostController.navigate(Routes.LoginScreen)
                    }
                )
            }
        }
    }
}

