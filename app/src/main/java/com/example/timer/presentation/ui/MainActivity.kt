package com.example.timer.presentation.ui

import PomodoroViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timer.presentation.utils.SettingManager
import com.example.timer.presentation.viewmodel.PomodoroViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SwipeNavigation()
        }
    }
}

@Composable
fun SwipeNavigation() {
    var currentScreen by remember { mutableIntStateOf(1) } // 0 = Data, 1 = Timer, 2 = Setting
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val settingManager = rememberUpdatedState(SettingManager(context))
    val pomodoroViewModel: PomodoroViewModel = viewModel(
        factory = PomodoroViewModelFactory(context)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(currentScreen) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        coroutineScope.launch {
                            val newValue = (offsetX.value + dragAmount).coerceIn(-300f, 300f) // 이동 범위 제한
                            offsetX.snapTo(newValue)
                        }
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            val targetScreen = when {
                                offsetX.value > 100 && currentScreen > 0 -> currentScreen - 1
                                offsetX.value < -100 && currentScreen < 2 -> currentScreen + 1
                                else -> currentScreen
                            }
                            offsetX.animateTo(0f, tween(300))
                            if (targetScreen != currentScreen) {
                                currentScreen = targetScreen
                            }
                        }

                    }
                )
            }
    ) {
        when (currentScreen) {
            0 -> DataScreen()
            1 -> PomodoroTimerScreen(pomodoroViewModel, context)
            2 -> SettingsScreen(settingManager.value, onSwipeLeft = { currentScreen = 1 })
        }
    }
}
