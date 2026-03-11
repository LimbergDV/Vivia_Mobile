package com.limbergdv.vivia_mobile.features.auth.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DividerWithText(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Color(0xFFCAC4D0)
        )
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = TextStyle(
                fontWeight = FontWeight.Light,
                fontSize = 20.sp,
                color = Color.Black
            )
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Color(0xFFCAC4D0)
        )
    }
}