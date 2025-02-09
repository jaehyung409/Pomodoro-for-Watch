package com.example.timer.presentation.theme

import androidx.compose.runtime.Composable


@Composable
fun PomodoroTheme(content: @Composable () -> Unit) {
    androidx.wear.compose.material.MaterialTheme(
        colors = wearColorPalette,
        typography = Typography,
        content = content
    )
}