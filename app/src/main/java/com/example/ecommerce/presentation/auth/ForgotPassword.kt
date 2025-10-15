package com.example.ecommerce.presentation.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ecommerce.presentation.auth.authcomponents.CustomButton
import com.example.ecommerce.presentation.auth.authcomponents.CustomText
import com.example.ecommerce.presentation.auth.authcomponents.CustomTextField
import com.example.ecommerce.ui.theme.Montserrat

@Composable
fun ForgotPasswordScreen(navHostController: NavHostController) {
    var username by remember { mutableStateOf("") }
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
                onValueChange = { username = it },
                placeholder = "Enter your email address",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "* We will send you a message to set or reset your new password",
                fontFamily = Montserrat,
                fontSize = 14.sp,
                color = Color(0xFF676767),
            )
            Spacer(modifier = Modifier.height(40.dp))
            CustomButton("Submit")
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