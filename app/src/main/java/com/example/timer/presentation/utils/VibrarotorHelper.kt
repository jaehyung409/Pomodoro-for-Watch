package com.example.timer.presentation

import android.content.Context
import android.os.VibrationEffect
import android.os.VibratorManager

fun triggerVibration(context: Context) {
    val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
                            as VibratorManager
    val vibrator = vibratorManager.defaultVibrator
    val doubleEffect = VibrationEffect.createWaveform(longArrayOf(0, 200, 100, 200), -1)
    vibrator.vibrate(doubleEffect)
}