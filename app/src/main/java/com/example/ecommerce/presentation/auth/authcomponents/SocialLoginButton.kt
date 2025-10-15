package com.example.ecommerce.presentation.auth.authcomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ecommerce.R

@Composable
fun SocialLoginButtons(modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Image(
            painter = painterResource(R.drawable.google),
            contentDescription = null,
            modifier = modifier.size(54.dp)
        )
        Image(
            painter = painterResource(R.drawable.apple),
            contentDescription = null,
            modifier = modifier.size(54.dp)
        )
        Image(
            painter = painterResource(R.drawable.facebook),
            contentDescription = null,
            modifier = modifier.size(54.dp)
        )
    }
}