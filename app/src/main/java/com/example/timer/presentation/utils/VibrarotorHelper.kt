package com.example.timer.presentation.utils

import android.content.Context
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.VibratorManager

fun triggerDoubleVibration(context: Context) {
    val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
                            as VibratorManager
    val vibrator = vibratorManager.defaultVibrator
    val attributes =  VibrationAttributes.Builder()
        .setUsage(VibrationAttributes.USAGE_ALARM)
        .build()
    val doubleEffect = VibrationEffect.createWaveform(longArrayOf(0, 200, 100, 200), -1)
    vibrator.vibrate(doubleEffect, attributes)
}

fun triggerSingleVibration(context: Context) {
    val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
                            as VibratorManager
    val vibrator = vibratorManager.defaultVibrator
    val attributes =  VibrationAttributes.Builder()
        .setUsage(VibrationAttributes.USAGE_ALARM)
        .build()
    val singleEffect =
            VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
    vibrator.vibrate(singleEffect, attributes)
}