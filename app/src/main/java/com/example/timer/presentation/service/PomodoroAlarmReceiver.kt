package com.example.timer.presentation.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PomodoroAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val serviceIntent = Intent(context, PomodoroAlarmService::class.java).apply {
            action = intent.action
        }
        context.startForegroundService(serviceIntent)
    }

    companion object {
        const val ACTION_TIMER_FINISH = "com.example.timer.ACTION_TIMER_FINISH"
        const val ACTION_TIMER_HALF = "com.example.timer.ACTION_TIMER_HALF"
    }
}
