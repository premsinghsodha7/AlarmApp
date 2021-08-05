package com.prem.alarmapp.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.prem.alarmapp.R
import com.prem.alarmapp.service.AlarmService
import com.prem.alarmapp.service.RescheduleAlarmsService
import com.prem.alarmapp.ui.MainActivity
import com.prem.alarmapp.utils.Constants
import com.prem.alarmapp.utils.Util.convertDateTime
import io.karn.notify.Notify
import timber.log.Timber

import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            startRescheduleAlarmsService(context)
        } else {
            val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)

            when (intent.action) {
                Constants.ACTION_SET_EXACT -> {
                    buildNotification(context, "Reminder", timeInMillis)
                    Timber.d("Reciever: ACTION_SET_EXACT")
                }
                Constants.ACTION_SET_REPETITIVE_EXACT -> {
                    setRepetitiveAlarm(AlarmService(context), timeInMillis)
                    buildNotification(context, "Scheduled Reminder", timeInMillis)
                    Timber.d("Reciever: ACTION_SET_REPETITIVE_EXACT")
                }
                Constants.ACTION_SNOOZE -> {
                    stopAlert(context)
                    //Snooze for 5 Minutes
                    val snoozeTimeMillis = System.currentTimeMillis() + 5 * DateUtils.MINUTE_IN_MILLIS
                    AlarmService(context).setExactAlarm(snoozeTimeMillis)
                    Timber.d("Reciever: ACTION_SNOOZE")
                }
                Constants.ACTION_STOP -> {
                    stopAlert(context)
                    Timber.d("Reciever: ACTION_STOP")

                }
            }
        }
    }

    //Start notification alert when we have broadcast
    private fun startAlert(context: Context){
        alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        taskRingtone = RingtoneManager.getRingtone(context, alert)
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                it.vibrate(5000)
            }
        }
        taskRingtone?.play()
    }

    //when user tap top to stop we have to stop alert
    private fun stopAlert(context: Context){
        if(taskRingtone!!.isPlaying){
            taskRingtone!!.stop()
            vibrator!!.cancel()
        }
        //to clear all notificaitons
        Notify.cancelNotification(22255)
    }

    private fun startRescheduleAlarmsService(context: Context) {
        val intentService = Intent(context, RescheduleAlarmsService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    private fun buildNotification(context: Context, title: String, timeInMillis: Long) {

        startAlert(context)

        Notify
            .with(context)
            .meta { // this: Payload.Meta
                // Launch the MainActivity once the notification is clicked.
                clickIntent = PendingIntent.getActivity(context,
                    0,
                    Intent(context, MainActivity::class.java),
                    0)
            }
            .content {
                this.title = title
                text = convertDateTime(timeInMillis)
            }
            .actions {
                //Action snooze alarm for 5 Minutes
                add(
                    NotificationCompat.Action(
                        // The icon corresponding to the action.
                        R.drawable.ic_snooze,
                        // The text corresponding to the action -- this is what shows below the
                        // notification.
                        "Snooze",
                        // Swap this PendingIntent for whatever Intent is to be processed when the action
                        // is clicked.
                        PendingIntent.getBroadcast(
                            context,
                            timeInMillis.toInt(),
                            Intent(context, AlarmReceiver::class.java).apply {
                                action = Constants.ACTION_SNOOZE
                                putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                            },
                            0
                        )
                    )
                )
                //Action stop alarm
                add(
                    NotificationCompat.Action(
                        // The icon corresponding to the action.
                        R.drawable.ic_alarm_stop,
                        // The text corresponding to the action -- this is what shows below the
                        // notification.
                        "Stop",
                        // Swap this PendingIntent for whatever Intent is to be processed when the action
                        // is clicked.
                        PendingIntent.getBroadcast(
                            context,
                            timeInMillis.toInt(),
                            Intent(context, AlarmReceiver::class.java).apply {
                                action = Constants.ACTION_STOP
                                putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                            },
                            0
                        )
                    )
                )
            }
            .show(id = 22255)
    }

    private fun setRepetitiveAlarm(alarmService: AlarmService, timeInMillis: Long) {
        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(7)
            Timber.d("Set alarm for next week same time - ${convertDateTime(this.timeInMillis)}")
        }
        alarmService.setRepetitiveAlarm(cal.timeInMillis)
    }

    companion object MyAlarmObjects {
        var taskRingtone: Ringtone? = null
        var alert: Uri? = null
        var vibrator: Vibrator? = null
    }
}