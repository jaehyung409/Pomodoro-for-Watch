package com.example.timer.presentation.utils

import android.content.Context
import android.content.SharedPreferences

class SettingManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var focusTime: Int
        get() = sharedPreferences.getInt("FOCUS_TIME", 25)
        set(value) = sharedPreferences.edit().putInt("FOCUS_TIME", value).apply()

    var shortBreakTime: Int
        get() = sharedPreferences.getInt("SHORT_BREAK_TIME", 5)
        set(value) = sharedPreferences.edit().putInt("SHORT_BREAK_TIME", value).apply()

    var longBreakTime: Int
        get() = sharedPreferences.getInt("LONG_BREAK_TIME", 15)
        set(value) = sharedPreferences.edit().putInt("LONG_BREAK_TIME", value).apply()

    var isAutoSwitchEnabled: Boolean
        get() = sharedPreferences.getBoolean("AUTO_SWITCH", false)
        set(value) = sharedPreferences.edit().putBoolean("AUTO_SWITCH", value).apply()
}