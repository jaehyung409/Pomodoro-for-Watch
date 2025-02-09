package com.example.timer.presentation.utils

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerState
import kotlinx.coroutines.launch

@Composable
fun WheelPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range : IntRange = 1..59
) {
    // Implementation
    val state = rememberPickerState(
        initialNumberOfOptions = range.last - range.first + 1,
        initiallySelectedOption = value - range.first
    )

    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .onRotaryScrollEvent { event ->
                coroutineScope.launch {
                    state.scrollBy(event.verticalScrollPixels)
                }
                true
            }
            .focusable()
    ) {
        Picker(
            state = state,
            modifier = Modifier.align(Alignment.Center),
            contentDescription = "Time Picker",
        ) {
            Text(
                text = "${range.first + it}",
                fontSize = if (it == state.selectedOption) 24.sp else 18.sp,
                color = if (it == state.selectedOption) Color.Red else Color.Gray
            )
        }
    }

    LaunchedEffect(state.selectedOption) {
        onValueChange(state.selectedOption + range.first)
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
