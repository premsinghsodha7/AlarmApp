package com.prem.alarmapp.service

import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import com.prem.alarmapp.data.repository.DefaultAlarmRepository
import com.prem.alarmapp.ui.fragments.CreateAlarmFragment
import javax.inject.Inject

class RescheduleAlarmsService : LifecycleService() {
    @Inject
    lateinit var alarmRepository: DefaultAlarmRepository
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        alarmRepository.getAllAlarms().observe(this, { alarms ->
            for (a in alarms) {
                CreateAlarmFragment.startAlarm(a.id!!, this)
            }
        })
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

}