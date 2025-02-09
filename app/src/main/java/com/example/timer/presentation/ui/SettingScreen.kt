package com.example.timer.presentation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.Icon
import com.example.timer.presentation.utils.AutoSwitchDisplay
import com.example.timer.presentation.utils.SettingManager
import com.example.timer.presentation.utils.WheelPicker
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(settingManager: SettingManager, onSwipeLeft: () -> Unit) {
    val settingOptions = listOf("Focus Time", "Short Break Time", "Long Break Time", "Auto Switch")
    var selectedSettingIndex by remember { mutableIntStateOf(0) }
    var autoSwitchValue by remember { mutableStateOf(settingManager.isAutoSwitchEnabled) }

    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val configuration = LocalConfiguration.current
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        // 드래그할 때마다 Animatable offset 업데이트
                        scope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount)
                        }
                    },
                    onDragEnd = {
                        // 일정 드래그 거리(threshold) 이상일 때만 페이지 전환 적용
                        scope.launch {
                            val threshold = screenWidth / 4 // 화면 폭의 25%
                            if (offsetX.value > threshold && selectedSettingIndex > 0) {
                                // 오른쪽으로 드래그 → 이전 설정
                                offsetX.animateTo(screenWidth, animationSpec = tween(250))
                                selectedSettingIndex--
                                offsetX.snapTo(0f)
                            } else if (offsetX.value < -threshold && selectedSettingIndex < settingOptions.size - 1) {
                                // 왼쪽으로 드래그 → 다음 설정
                                offsetX.animateTo(-screenWidth, animationSpec = tween(250))
                                selectedSettingIndex++
                                offsetX.snapTo(0f)
                            } else if (offsetX.value > threshold && selectedSettingIndex == 0) {
                                // 🔹 첫 화면에서 왼쪽으로 스와이프하면 타이머 화면으로 이동
                                onSwipeLeft()
                            } else {
                                // 드래그가 부족하면 원래 위치로 복귀
                                offsetX.animateTo(0f, animationSpec = tween(250))
                            }
                        }
                    }
                )
            },
    ) {
        Spacer(modifier = Modifier.size(5.dp))

        Text("Setting", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.size(5.dp))

        Text(
            text = settingOptions[selectedSettingIndex],
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.secondary
        )

        Spacer(modifier = Modifier.size(5.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 20.dp)
                .offset { IntOffset(offsetX.value.roundToInt(), 0) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔹 왼쪽 화살표 (첫 화면에서는 안 보이도록 설정)
            if (selectedSettingIndex > 0) {
                IconButton(
                    onClick = { selectedSettingIndex-- },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colors.secondary
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(40.dp)) // 🔹 첫 화면에서는 공간만 차지하도록 설정
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(Alignment.Center)
                    .offset { IntOffset(offsetX.value.roundToInt(), 0) }, // ✅ 애니메이션 적용
                contentAlignment = Alignment.Center
            ) {
                when (selectedSettingIndex) {
                    0 -> WheelPicker(value = settingManager.focusTime, onValueChange = { settingManager.focusTime = it })
                    1 -> WheelPicker(value = settingManager.shortBreakTime, onValueChange = { settingManager.shortBreakTime = it })
                    2 -> WheelPicker(value = settingManager.longBreakTime, onValueChange = { settingManager.longBreakTime = it })
                    3 -> AutoSwitchDisplay(isOn = autoSwitchValue, onToggle = { autoSwitchValue = it; settingManager.isAutoSwitchEnabled = it })
                }
            }
            // 🔹 오른쪽 화살표 (마지막 화면에서는 안 보이도록 설정)
            if (selectedSettingIndex < settingOptions.size - 1) {
                IconButton(
                    onClick = { selectedSettingIndex++ },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
                        tint = MaterialTheme.colors.secondary
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(40.dp)) // 🔹 마지막 화면에서는 공간만 차지하도록 설정
            }
        }
    }
}