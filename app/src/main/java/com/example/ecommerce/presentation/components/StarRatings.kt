package com.example.ecommerce.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.dp

@Composable
fun StarRatings(rating: Double) {

    Row {
        repeat(5) { index ->

            val starRating = rating - index

            val starFill = when {
                starRating >= 1.0 -> 1.0
                starRating >= 0.75 -> 0.75
                starRating >= 0.5 -> 0.5
                starRating >= 0.25 -> 0.25
                else -> 0.0

            }

            Box {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFE0E0E0),
                    modifier = Modifier.size(18.dp)
                )

                if (starFill>0){
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFA726),
                        modifier = Modifier
                            .size(18.dp)
                            .clipToBounds()
                            .drawWithContent{
                                clipRect(right = size.width * starFill.toFloat()) {
                                    this@drawWithContent.drawContent()
                                }
                            }
                    )
                }

            }
        }
    }
}