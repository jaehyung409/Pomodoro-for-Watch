package com.example.timer.presentation.ui

import DiscreteCircularProgress
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.timer.presentation.theme.PomodoroTheme
import com.example.timer.presentation.utils.SettingManager
import com.example.timer.presentation.utils.formatTime
import com.example.timer.presentation.viewmodel.PomodoroViewModel
import kotlinx.coroutines.delay

@Composable
fun PomodoroTimerScreen(viewModel: PomodoroViewModel, context: Context) {
    val remainingTime by viewModel.remainingTime.collectAsState()
    val totalTime by viewModel.totalTime.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()
    val isFocusMode by viewModel.isFocusMode.collectAsState()
    val isWaitingCall by viewModel.isWaitingCall.collectAsState()
    val currentCycle by viewModel.currentCycle.collectAsState()
    val startTrigger by viewModel.startTrigger.collectAsState()
    val settingManager = SettingManager(context)

    PomodoroTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.onBackground).clickable{}
        ) {
            DiscreteCircularProgress(
                remainingTime = remainingTime,
                totalTime = totalTime,
                totalSegments = 90,
                canvasSize = 200.dp,
                filledColor = if (isFocusMode) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
                backgroundColor = MaterialTheme.colors.background
            )

            LaunchedEffect(key1 = remainingTime, key2 = startTrigger) {
                if (isFocusMode && totalTime != settingManager.focusTime * 60L) {
                    viewModel.reset(context)
                }
                if (!isFocusMode && currentCycle == 0 && totalTime != settingManager.longBreakTime * 60L) {
                    viewModel.reset(context)
                }
                if (!isFocusMode && currentCycle != 0 && totalTime != settingManager.shortBreakTime * 60L) {
                    viewModel.reset(context)
                }
                viewModel.updateTime()
                if (remainingTime > 0) {
                    delay(1000L)
                    viewModel.updateTime()
                }
                if (remainingTime == 0L) {
                    viewModel.setNextState(context)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formatTime(remainingTime),
                    fontSize = MaterialTheme.typography.title1.fontSize,
                    color = MaterialTheme.colors.onPrimary
                )
                Text(
                    text = "$currentCycle / 4",
                    fontSize = MaterialTheme.typography.body2.fontSize,
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.size(20.dp))

                when {
                    isWaitingCall -> {
                        Button(onClick = {
                            viewModel.start(context) }) { Text("Start") }
                    }
                    !isPaused -> {
                        Button(onClick = { viewModel.pause(context) }) { Text("Pause") }
                    }
                    isFocusMode -> {
                        Row {
                            Button(onClick = { viewModel.resume(context) }) { Text("Resume") }
                            Spacer(modifier = Modifier.size(15.dp))
                            Button(onClick = { viewModel.reset(context) }) { Text("Reset") }
                        }
                    }
                    else -> {
                        Row {
                            Button(onClick = { viewModel.resume(context) }) { Text("Resume") }
                            Spacer(modifier = Modifier.size(15.dp))
                            Button(onClick = { viewModel.check(context) }) { Text("Check") }
                        }
                    }
                }
            }
        }
    }
}