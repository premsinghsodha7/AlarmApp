package com.prem.alarmapp.ui

import androidx.lifecycle.ViewModel
import com.prem.alarmapp.data.entities.Alarms
import com.prem.alarmapp.data.repository.AlarmRepository
import com.prem.alarmapp.data.repository.DefaultAlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(private val repos: AlarmRepository) :ViewModel(){

    fun insert(alarm: Alarms)= CoroutineScope(Dispatchers.Main).launch {
        if (alarm.time == "" || alarm.repeatDays == "")
            return@launch

        repos.insert(alarm)
    }
    fun update(alarm: Alarms)= CoroutineScope(Dispatchers.Main).launch {
        if (alarm.time == "" || alarm.repeatDays == "")
            return@launch
        repos.update(alarm)
    }
    fun delete(alarm: Alarms)= CoroutineScope(Dispatchers.Main).launch {
        repos.delete(alarm)
    }

    //this method is not used nor called yet...
    fun deleteAll()= CoroutineScope(Dispatchers.Main).launch {
        repos.deleteAllAlarms()
    }

    fun getAllAlarms() = repos.getAllAlarms()
}