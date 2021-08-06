package com.prem.alarmapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prem.alarmapp.data.repository.AlarmRepository
import com.prem.alarmapp.data.repository.DefaultAlarmRepository

/*
* if we use multiple fragment and we also have Android test the its very helpful to passing params in viewmodel
* since we have using hilt so there is no use for that but without hilt or Dagger its very helpful to parametrize viewmodel
* */
class AlarmViewModelFactory(private val repositoryDefault: DefaultAlarmRepository):
    ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlarmViewModel(repositoryDefault as AlarmRepository) as T
    }
}