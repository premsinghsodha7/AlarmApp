package com.prem.alarmapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prem.alarmapp.data.dao.AlarmDao
import com.prem.alarmapp.data.entities.Alarms


@Database(
    entities = [Alarms::class],
    version = 1,
    exportSchema = false
)
abstract class AlarmDatabase : RoomDatabase() {
    //this method would be called in our alarm repos
    abstract fun getAlarmDao(): AlarmDao
}

