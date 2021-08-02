package com.prem.alarmapp.data.repository

import androidx.lifecycle.LiveData
import com.prem.alarmapp.data.entities.Alarms

interface AlarmRepository {

    suspend fun insert(alarm: Alarms)
    suspend fun update(alarm: Alarms)
    suspend fun delete(alarm: Alarms)
    suspend fun deleteAllAlarms()
    fun getAllAlarms(): LiveData<List<Alarms>>

}