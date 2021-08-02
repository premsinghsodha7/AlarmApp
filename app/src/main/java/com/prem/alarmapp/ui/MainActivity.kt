package com.prem.alarmapp.ui


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.prem.alarmapp.R
import com.prem.alarmapp.notification.ACTION_STOP_ALARM
import com.prem.alarmapp.receiver.AlarmReceiver

@SuppressLint("ResourceType")
class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        //val sharedprefs = getSharedPreferences("pref", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init Navigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val action = intent!!.action

        //stop alarm sound on notification click
        if(action == ACTION_STOP_ALARM){
            AlarmReceiver.taskRingtone!!.stop()
            AlarmReceiver.vibrator!!.cancel()
        }
    }
}

