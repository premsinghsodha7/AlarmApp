package com.prem.alarmapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class AlarmApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        //Timber for logging
        Timber.plant(Timber.DebugTree())
    }
}