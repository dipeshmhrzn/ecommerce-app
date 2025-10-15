package com.example.ecommerce.presentation.splashscreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.R
import com.example.ecommerce.ui.theme.LibreCaslonText
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    isLoading: Boolean,
    onFinish:()->Unit) {

    LaunchedEffect(isLoading) {
        Log.d("SplashScreen", "isLoading: $isLoading")
        if (!isLoading){
            delay(1500)
            onFinish()
        }
    }
    Scaffold {
        Row(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.stylishlogo),
                contentDescription = null,
                modifier = Modifier
                    .size(180.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Stylish",
                style = TextStyle(
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = LibreCaslonText,
                    color = Color(0xFFF83758),
                    shadow = Shadow(
                        color = Color(0x50000000),
                        blurRadius = 4f,
                        offset = Offset(0f, 6f)
                    )
                )
            )
        }
    }
}

//@Composable
//@Preview(showBackground = true)
//fun SplashScreenPreview() {
//    SplashScreen()
//}