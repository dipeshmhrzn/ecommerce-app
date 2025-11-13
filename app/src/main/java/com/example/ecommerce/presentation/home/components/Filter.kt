package com.example.ecommerce.presentation.home.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.ui.theme.Montserrat

@Composable
fun Filter(
    minPrice: String,
    maxPrice: String,
    selectedRating: Int?,
    onApplyFilter: (Double?, Double?, Int?) -> Unit,
    onFilterValuesChange: (String, String, Int?) -> Unit,
    onResetClick: () -> Unit
) {

    val context = LocalContext.current

    var localMinPrice by remember { mutableStateOf(minPrice) }
    var localMaxPrice by remember { mutableStateOf(maxPrice) }
    var localSelectedRating by remember { mutableStateOf(selectedRating) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Price",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = localMinPrice,
                    onValueChange = { newValue ->
                        localMinPrice = newValue
                        onFilterValuesChange(newValue, localMaxPrice, localSelectedRating)
                    },
                    placeholder = {
                        Text(
                            text = "Min",
                            fontFamily = Montserrat,
                            fontSize = 18.sp,
                            color = Color.Gray.copy(alpha = .5f)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Black.copy(alpha = .4f),
                        focusedIndicatorColor = Color.Red.copy(alpha = .4f)
                    )
                )

                Text(
                    text = "-",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(12.dp)
                )

                OutlinedTextField(
                    value = localMaxPrice,
                    onValueChange = { newValue ->
                        localMaxPrice = newValue
                        onFilterValuesChange(localMinPrice, localMaxPrice, localSelectedRating)
                    },
                    placeholder = {
                        Text(
                            text = "Max",
                            fontFamily = Montserrat,
                            fontSize = 18.sp,
                            color = Color.Gray.copy(alpha = .5f)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Black.copy(alpha = .4f),
                        focusedIndicatorColor = Color.Red.copy(alpha = .4f)
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Rating",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val ratings = listOf(5, 4, 3, 2, 1)

                ratings.forEach { ratingValue ->
                    val ratingText = when (ratingValue) {
                        5 -> "5"
                        4 -> "≥4"
                        3 -> "≥3"
                        2 -> "≥2"
                        1 -> "≥1"
                        else -> ""
                    }

                    val isSelected = localSelectedRating == ratingValue

                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFF9F9F9),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                localSelectedRating = if (isSelected) null else ratingValue
                                onFilterValuesChange(
                                    localMinPrice,
                                    localMaxPrice,
                                    localSelectedRating
                                )
                                Log.d("Filter", "Selected Rating: $localSelectedRating")
                            }
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color.Red else Color.Transparent, // Apply red border if selected
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(top = 6.dp, bottom = 6.dp, start = 10.dp, end = 10.dp)

                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = ratingText,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                            )

                            Spacer(modifier = Modifier.width(5.dp))
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating star",
                                modifier = Modifier.size(18.dp),
                                tint = Color(0xFFFFA726)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Black.copy(alpha = .4f)),
                    onClick = {

                        onResetClick()

                        localMinPrice = ""
                        localMaxPrice = ""
                        localSelectedRating = null
                        
                        onFilterValuesChange(localMinPrice, localMaxPrice, null)

                    }
                ) {
                    Text(
                        text = "Reset",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                    )
                }

                Button(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF83758),
                        contentColor = Color.White
                    ),
                    onClick = {
                        onApplyFilter(
                            minPrice.toDoubleOrNull(),
                            maxPrice.toDoubleOrNull(),
                            selectedRating
                        )
                    }
                ) {
                    Text(
                        text = "Done",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                    )
                }
            }
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//private fun FilterPrev() {
//    MaterialTheme {
//        Filter(
//            onClose = {}
//        )
//    }
//}