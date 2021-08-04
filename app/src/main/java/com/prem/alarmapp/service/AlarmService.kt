package com.prem.alarmapp.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.prem.alarmapp.notification.ACTION_SNOOZE_ALARM
import com.prem.alarmapp.notification.ACTION_STOP_ALARM
import com.prem.alarmapp.receiver.AlarmReceiver
import java.util.*

class AlarmService : Service() {
    private val notificationId = System.currentTimeMillis().toInt()
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action=intent!!.action

        //stop alarm sound
        if(action == ACTION_STOP_ALARM){
            if(AlarmReceiver.taskRingtone!!.isPlaying){
                AlarmReceiver.taskRingtone!!.stop()
                AlarmReceiver.vibrator!!.cancel()
            }
        }
        //snooze
        else if(action== ACTION_SNOOZE_ALARM){
            snoozeAlarm()
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun snoozeAlarm() {
        //cancel previous alarm tone
        if(AlarmReceiver.taskRingtone!!.isPlaying){
            AlarmReceiver.taskRingtone!!.stop()
            AlarmReceiver.vibrator!!.cancel()
        }

        //remind the user in 2 minutes
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, notificationId, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,Calendar.getInstance().timeInMillis +5*6000,pendingIntent)
    }

}