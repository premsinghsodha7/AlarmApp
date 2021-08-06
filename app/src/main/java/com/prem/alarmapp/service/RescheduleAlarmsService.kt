package com.prem.alarmapp.service

import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import com.prem.alarmapp.data.repository.DefaultAlarmRepository
import javax.inject.Inject

class RescheduleAlarmsService @Inject constructor(
    private val alarmRepository: DefaultAlarmRepository,
    private val alarmService: AlarmService
) : LifecycleService() {

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

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

}