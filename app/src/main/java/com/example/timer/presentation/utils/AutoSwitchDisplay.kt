package com.example.timer.presentation.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text

@Composable
fun AutoSwitchDisplay(isOn: Boolean, onToggle: (Boolean) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable { onToggle(!isOn) } // ✅ 터치하면 값 변경
            .size(80.dp) // ✅ 크기 고정
    ) {
        Text(
            text = if (isOn) "ON" else "OFF",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (isOn) Color.Green else Color.Red
        )
    }
}