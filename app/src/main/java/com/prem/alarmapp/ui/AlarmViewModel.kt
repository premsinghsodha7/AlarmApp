package com.prem.alarmapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prem.alarmapp.data.entities.Alarms
import com.prem.alarmapp.data.repository.AlarmRepository
import com.prem.alarmapp.utils.Event
import com.prem.alarmapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val repos: AlarmRepository
) : ViewModel() {

    private val _insertAlarmItemStatus = MutableLiveData<Event<Resource<Alarms>>>()
    val  insertAlarmItemStatus: LiveData<Event<Resource<Alarms>>> = _insertAlarmItemStatus

    private val _updateAlarmItemStatus = MutableLiveData<Event<Resource<Alarms>>>()
    val  updateAlarmItemStatus: LiveData<Event<Resource<Alarms>>> = _updateAlarmItemStatus

    private val _deleteAlarmItemStatus = MutableLiveData<Event<Resource<String>>>()
    val  deleteAlarmItemStatus: LiveData<Event<Resource<String>>> = _deleteAlarmItemStatus

    fun insertAlarmItem(timeText: String, repeatDays: String, alarmIsOn: Boolean){
        if (timeText.isEmpty() && repeatDays.isEmpty()){
            _insertAlarmItemStatus.postValue(Event(Resource.error("The field must not be empty",null)))
            return
        }

        if (timeText.isEmpty()){
            _insertAlarmItemStatus.postValue(Event(Resource.error("Please select alarm time",null)))
            return
        }

        if (repeatDays.isEmpty()){
            _insertAlarmItemStatus.postValue(Event(Resource.error("Please select any one repeat day",null)))
            return
        }

        val alarm = Alarms(timeText, repeatDays, alarmIsOn)
        insert(alarm)
        _insertAlarmItemStatus.postValue(Event(Resource.success(alarm)))
    }

    fun insert(alarm: Alarms) = CoroutineScope(Dispatchers.Main).launch {
        repos.insert(alarm)
    }

    fun updateAlarmItem(id: Int, timeText: String, repeatDays: String, alarmIsOn: Boolean){
        if (timeText.isEmpty() && repeatDays.isEmpty()){
            _updateAlarmItemStatus.postValue(Event(Resource.error("The field must not be empty",null)))
            return
        }

        if (timeText.isEmpty()){
            _updateAlarmItemStatus.postValue(Event(Resource.error("Please select alarm time",null)))
            return
        }

        if (repeatDays.isEmpty()){
            _updateAlarmItemStatus.postValue(Event(Resource.error("Please select any one repeat day",null)))
            return
        }

        val alarm = Alarms(timeText, repeatDays, alarmIsOn, id)
        update(alarm)
        _updateAlarmItemStatus.postValue(Event(Resource.success(alarm)))
    }

    private fun update(alarm: Alarms) = CoroutineScope(Dispatchers.Main).launch {
        repos.update(alarm)
    }

    fun delete(alarm: Alarms) = CoroutineScope(Dispatchers.Main).launch {
        repos.delete(alarm)
        _deleteAlarmItemStatus.postValue(Event(Resource.success("Successfully delete alarm at ${alarm.time}")))
    }

    //this method is not used nor called yet...
    fun deleteAll() = CoroutineScope(Dispatchers.Main).launch {
        repos.deleteAllAlarms()
    }

    fun getAllAlarms() = repos.getAllAlarms()
}