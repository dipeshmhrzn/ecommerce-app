package com.example.ecommerce.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.ui.theme.Montserrat



data class SortOption(
    val ascending: Boolean,
    @DrawableRes val iconRes: Int
)
@Composable
fun CommonSortFilter(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter,
    hasDropdown: Boolean = false,
    dropdownOptions: List<SortOption> = emptyList(),
    onOptionSelected: (SortOption) -> Unit = {},
    onClick: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(color = Color.Gray.copy(alpha = .1f), shape = RoundedCornerShape(8.dp))
            .clickable {
                if (hasDropdown) expanded = true else onClick()
            }
            .padding(8.dp)
            .wrapContentWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color(0xFF232327),
                modifier = Modifier.size(16.dp)
            )
        }

        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            dropdownOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Price",
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Light,
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                painter = painterResource(option.iconRes),
                                contentDescription = null,
                                tint = Color(0xFF232327),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}
