package com.prem.alarmapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prem.alarmapp.data.repository.AlarmRepository
import com.prem.alarmapp.data.repository.DefaultAlarmRepository


@Suppress("UNCHECKED_CAST")
class AlarmViewModelFactory(private val repositoryDefault: DefaultAlarmRepository):
    ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlarmViewModel(repositoryDefault as AlarmRepository) as T
    }
}