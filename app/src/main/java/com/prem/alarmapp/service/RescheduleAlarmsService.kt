package com.prem.alarmapp.service

import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import com.prem.alarmapp.data.repository.DefaultAlarmRepository
import javax.inject.Inject

class RescheduleAlarmsService : LifecycleService() {

    private var alarmService: AlarmService? = null
    @Inject
    lateinit var alarmRepository: DefaultAlarmRepository

    override fun onCreate() {
        super.onCreate()
        alarmService = AlarmService(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        alarmRepository.getAllAlarms().observe(this, { alarms ->
            for (a in alarms) {
                if (a.AlarmIsEnabled)
                    alarmService?.setRepetitiveAlarm(a.time)
            }
        })
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmService = null
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

}