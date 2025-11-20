package com.example.ecommerce.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.ecommerce.R
import com.example.ecommerce.ui.theme.LibreCaslonText

@Composable
fun CustomTopBar(profilePictureUrl: String? = null, isLoading: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { },
            modifier = Modifier
                .background(color = Color(0xFFF2F2F2), shape = CircleShape)
                .size(45.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.menu),
                contentDescription = null,
                tint = Color(0xFF323232),
                modifier = Modifier.size(25.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.stylishlogo),
                contentDescription = null,
                modifier = Modifier.size(45.dp)
            )
            Text(
                text = "Stylish",
                fontFamily = LibreCaslonText,
                color = Color(0xFF4392F9),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(45.dp),
                strokeWidth = 2.dp
            )
        }else {
            if (profilePictureUrl != null) {
                AsyncImage(
                    model = profilePictureUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Default Profile Image if URL is not available
                Image(
                    painter = painterResource(R.drawable.profile), // Use a default profile image
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TopAppBarPreview(modifier: Modifier = Modifier) {
    CustomTopBar()
}