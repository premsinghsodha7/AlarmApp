package com.prem.alarmapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prem.alarmapp.data.entities.Alarms
import com.prem.alarmapp.data.repository.AlarmRepository
import com.prem.alarmapp.utils.Event
import com.prem.alarmapp.utils.Resource
import com.prem.alarmapp.utils.Util
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

    fun insertAlarmItem(timeText: Long, alarmIsOn: Boolean)= CoroutineScope(Dispatchers.Main).launch{

        val alarm = Alarms(timeText, alarmIsOn)
        repos.insert(alarm)
        _insertAlarmItemStatus.postValue(Event(Resource.success(alarm)))
    }

    fun updateAlarmItem(id: Int, timeText: Long, alarmIsOn: Boolean)= CoroutineScope(Dispatchers.Main).launch{
        val alarm = Alarms(timeText, alarmIsOn, id)
        repos.update(alarm)
        _updateAlarmItemStatus.postValue(Event(Resource.success(alarm)))
    }

    fun delete(alarm: Alarms) = CoroutineScope(Dispatchers.Main).launch {
        repos.delete(alarm)
        _deleteAlarmItemStatus.postValue(Event(Resource.success("Successfully delete alarm at ${Util.convertTime(alarm.time)}")))
    }

    //this method is not used nor called yet...
    fun deleteAll() = CoroutineScope(Dispatchers.Main).launch {
        repos.deleteAllAlarms()
    }

    fun getAllAlarms() = repos.getAllAlarms()
}