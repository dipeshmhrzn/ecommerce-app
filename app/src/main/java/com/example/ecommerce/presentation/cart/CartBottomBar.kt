package com.example.ecommerce.presentation.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.ui.theme.Montserrat
import kotlin.math.roundToInt

@Composable
fun CartBottomBar(
    isAllSelected: Boolean=false,
    onAllSelectionToggle: () -> Unit,
    totalPrice: Int,
    totalItems: Int,
    onCheckout: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 16.dp, end = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isAllSelected,
                    onCheckedChange = { onAllSelectionToggle() },
                )
                Text(
                    text = "All"
                )
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Text(
                        text = "Total ($totalItems ${if (totalItems == 1) "item" else "items"})",
                        fontSize = 14.sp,
                        fontFamily = Montserrat,
                        color = Color.Gray
                    )
                    Text(
                        text = "Rs. ${(totalPrice)}",
                        fontSize = 24.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = onCheckout,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF83758)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(56.dp)
                        .width(130.dp)
                ) {
                    Text(
                        text = "Checkout",
                        fontFamily = Montserrat,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}