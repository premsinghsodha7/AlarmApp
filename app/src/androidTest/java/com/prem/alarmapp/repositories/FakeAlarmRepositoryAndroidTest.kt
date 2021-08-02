package com.prem.alarmapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prem.alarmapp.data.entities.Alarms
import com.prem.alarmapp.data.repository.AlarmRepository

class FakeAlarmRepositoryAndroidTest: AlarmRepository {

    private val alarmItems = mutableListOf<Alarms>()
    private val observableAllAlarmItems = MutableLiveData<List<Alarms>>()

    override suspend fun insert(alarm: Alarms) {
        alarmItems.add(alarm)
        refreshLiveData()
    }

    override suspend fun update(alarm: Alarms) {
        alarmItems.add(alarm)
        refreshLiveData()
    }

    override suspend fun delete(alarm: Alarms) {
        alarmItems.remove(alarm)
        refreshLiveData()
    }

    override suspend fun deleteAllAlarms() {

    }

    override fun getAllAlarms(): LiveData<List<Alarms>> {
        return observableAllAlarmItems
    }

    private fun refreshLiveData(){
        observableAllAlarmItems.postValue(alarmItems)
    }

}