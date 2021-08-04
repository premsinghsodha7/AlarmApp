package com.prem.alarmapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import com.prem.alarmapp.notification.AlarmNotificationHelper
import com.prem.alarmapp.service.AlarmService
import com.prem.alarmapp.service.RescheduleAlarmsService

import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (Intent.ACTION_BOOT_COMPLETED == intent!!.action) {
            startRescheduleAlarmsService(context)
        } else {
            val alarmNotificationHelper = AlarmNotificationHelper(context)
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator != null) {
                vibrator!!.vibrate(3000)
            }
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            taskRingtone = RingtoneManager.getRingtone(context, alert)
            if (taskRingtone != null) {
                taskRingtone!!.play()
            }
            val notification = alarmNotificationHelper.getNotificationBuilder().build()
            alarmNotificationHelper.getManager().notify(getID(), notification)
        }
    }

    private fun startRescheduleAlarmsService(context: Context) {
        val intentService = Intent(context, RescheduleAlarmsService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    companion object MyAlarmObjects {
        var taskRingtone: Ringtone? = null
        var alert: Uri? = null
        var vibrator: Vibrator? = null

        fun getID(): Int {
            return (Date().time / 1000L % Int.MAX_VALUE).toInt()
        }
    }
}