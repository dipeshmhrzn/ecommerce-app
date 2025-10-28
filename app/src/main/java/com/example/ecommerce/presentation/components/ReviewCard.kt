package com.example.ecommerce.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.data.dto.productdto.Review
import com.example.ecommerce.ui.theme.Montserrat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ReviewCard(review: Review) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical =6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFF9F9F9), shape = RoundedCornerShape(10.dp))
                .padding(12.dp),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = review.reviewerName,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = formatDate(review.date),
                    fontSize = 14.sp,
                    fontFamily = Montserrat,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            // Rating stars
            StarRatings(review.rating.toDouble())
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = review.comment,
                fontSize = 16.sp,
                fontFamily = Montserrat,
                color = Color.DarkGray,
            )
        }
    }
}

fun formatDate(dateStr: String): String {
    return try {
        val parsedDate = ZonedDateTime.parse(dateStr)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
        parsedDate.format(formatter)
    } catch (e: Exception) {
        dateStr.take(10) // fallback to first 10 chars (yyyy-MM-dd)
    }
}
