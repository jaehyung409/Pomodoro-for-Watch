package com.example.timer.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.example.timer.presentation.utils.triggerDoubleVibration
import com.example.timer.presentation.utils.triggerSingleVibration

class PomodoroAlarmService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground(1, createNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE)

        when (intent?.action) {
            PomodoroAlarmReceiver.ACTION_TIMER_FINISH -> {
                triggerDoubleVibration(applicationContext)
            }
            PomodoroAlarmReceiver.ACTION_TIMER_HALF -> {
                triggerSingleVibration(applicationContext)
            }
        }

        // 일정 시간 후 자동 종료
        Handler(Looper.getMainLooper()).postDelayed({
            stopSelf()
        }, 1000)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification {
        val notificationChannelId = "Pomodoro Timer"
        val notificationChannelName = "Pomodoro Alarm Service"
        val notificationChannelImportance = NotificationManager.IMPORTANCE_HIGH

        val notificationChannel = NotificationChannel(
            notificationChannelId,
            notificationChannelName,
            notificationChannelImportance
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)


        return NotificationCompat.Builder(this, notificationChannelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Pomodoro Timer")
            .setContentText("Timer is triggered")
            .build()
    }
}
