package com.example.timer.presentation.utils

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("pomodoro_prefs", Context.MODE_PRIVATE)

    fun setFocusTime(minutes: Int) {
        prefs.edit().putInt("FOCUS_TIME", minutes).apply()
    }

    fun getFocusTime(): Int {
        return prefs.getInt("FOCUS_TIME", 25)
    }

    fun setShortBreakTime(minutes: Int) {
        prefs.edit().putInt("SHORT_BREAK_TIME", minutes).apply()
    }

    fun getShortBreakTime(): Int {
        return prefs.getInt("SHORT_BREAK_TIME", 5)
    }

    fun setLongBreakTime(minutes: Int) {
        prefs.edit().putInt("LONG_BREAK_TIME", minutes).apply()
    }

    fun getLongBreakTime(): Int {
        return prefs.getInt("LONG_BREAK_TIME", 15)
    }

    fun setAutoSwitch(enabled: Boolean) {
        prefs.edit().putBoolean("AUTO_SWITCH", enabled).apply()
    }

    fun getAutoSwitch(): Boolean {
        return prefs.getBoolean("AUTO_SWITCH", false)
    }
}