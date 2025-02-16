package com.example.timer.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.lifecycle.ViewModel
import com.example.timer.presentation.service.PomodoroAlarmReceiver
import com.example.timer.presentation.utils.SettingManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PomodoroViewModel(context: Context) : ViewModel() {
    private val settingManager = SettingManager(context)
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val _remainingTime = MutableStateFlow(settingManager.focusTime * 60L)
    val remainingTime: StateFlow<Long> = _remainingTime

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused

    private val _isFocusMode = MutableStateFlow(true)
    val isFocusMode: StateFlow<Boolean> = _isFocusMode

    private val _isWaitingCall = MutableStateFlow(true)
    val isWaitingCall: StateFlow<Boolean> = _isWaitingCall

    private val _totalTime = MutableStateFlow(settingManager.focusTime * 60L)
    val totalTime: StateFlow<Long> = _totalTime

    private val _currentCycle = MutableStateFlow(0)
    val currentCycle: MutableStateFlow<Int> = _currentCycle

    private val _startTrigger = MutableStateFlow(false)
    val startTrigger: StateFlow<Boolean> = _startTrigger

    private var startTime: Long = 0L
    private var savedRemainingTime: Long = _remainingTime.value

    var accumulateTime = 0L
        private set

    private val autoSwitchEnabled = settingManager.isAutoSwitchEnabled

    fun start(context: Context) {
        _startTrigger.value = !_startTrigger.value
        if (!_isWaitingCall.value) return
        _isPaused.value = false
        _isWaitingCall.value = false

        startTime = SystemClock.elapsedRealtime()
        savedRemainingTime = _remainingTime.value
        scheduleAlarm(savedRemainingTime, PomodoroAlarmReceiver.ACTION_TIMER_FINISH, context)
        if (savedRemainingTime > totalTime.value / 2) {
            scheduleAlarm(savedRemainingTime - totalTime.value / 2, PomodoroAlarmReceiver.ACTION_TIMER_HALF, context)
        }
    }

    fun pause(context: Context) {
        if (_isPaused.value || _isWaitingCall.value) return
        _isPaused.value = true
        cancelAlarm(PomodoroAlarmReceiver.ACTION_TIMER_FINISH, context)
        cancelAlarm(PomodoroAlarmReceiver.ACTION_TIMER_HALF, context)
        savedRemainingTime = getUpdatedRemainingTime()
    }

    fun resume(context: Context) {
        if (!_isPaused.value) return
        _isPaused.value = false
        _startTrigger.value = !_startTrigger.value

        startTime = SystemClock.elapsedRealtime()
        scheduleAlarm(savedRemainingTime,PomodoroAlarmReceiver.ACTION_TIMER_FINISH, context)
        if (savedRemainingTime > totalTime.value / 2) {
            scheduleAlarm(savedRemainingTime - totalTime.value / 2, PomodoroAlarmReceiver.ACTION_TIMER_HALF, context)
        }
    }

    fun reset(context: Context) {
        _isPaused.value = false
        _isWaitingCall.value = true
        cancelAlarm(PomodoroAlarmReceiver.ACTION_TIMER_FINISH, context)
        cancelAlarm(PomodoroAlarmReceiver.ACTION_TIMER_HALF, context)

        _totalTime.value = if (_isFocusMode.value) {
            settingManager.focusTime * 60L
        } else if (_currentCycle.value == 0) {
            settingManager.longBreakTime * 60L
        } else {
            settingManager.shortBreakTime * 60L
        }
        _remainingTime.value = _totalTime.value
        savedRemainingTime = _remainingTime.value
    }

    fun check(context: Context) {
        cancelAlarm(PomodoroAlarmReceiver.ACTION_TIMER_FINISH, context)
        cancelAlarm(PomodoroAlarmReceiver.ACTION_TIMER_HALF, context)
        setNextState(context)
    }

    fun setNextState(context: Context) {
        _isWaitingCall.value = true

        if (_isFocusMode.value) {
            _currentCycle.value++
            _totalTime.value = if (_currentCycle.value ==  4) {
                settingManager.longBreakTime * 60L
            } else {
                settingManager.shortBreakTime * 60L
            }
            if (currentCycle.value == 4) {
                _currentCycle.value = 0
            }
        } else {
            _totalTime.value = settingManager.focusTime * 60L
        }
        _remainingTime.value = _totalTime.value
        _isFocusMode.value = !_isFocusMode.value

        if (autoSwitchEnabled) {
            start(context)
        }
    }

    fun updateTime() {
        if (_isPaused.value || _isWaitingCall.value) return
        _remainingTime.value = getUpdatedRemainingTime()
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleAlarm(triggerTime: Long, action: String, context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Android 12(API 31) 이상인 경우 권한 확인
        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            context.startActivity(intent)
        }

        val intent = Intent(context, PomodoroAlarmReceiver::class.java).apply {
            this.action = action
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, action.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + triggerTime * 1000,
            pendingIntent)
    }

    private fun cancelAlarm(action: String, context: Context) {
        val intent = Intent(context, PomodoroAlarmReceiver::class.java).apply {
            this.action = action
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, action.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    private fun getUpdatedRemainingTime(): Long {
        val elapsedTime = SystemClock.elapsedRealtime() - startTime
        return maxOf(0L, savedRemainingTime - elapsedTime / 1000)
    }

}
