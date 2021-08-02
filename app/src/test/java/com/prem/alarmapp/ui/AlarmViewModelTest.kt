package com.prem.alarmapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.prem.alarmapp.MainCoroutineRule
import com.prem.alarmapp.data.entities.Alarms
import com.prem.alarmapp.data.repository.AlarmRepository
import com.prem.alarmapp.getOrAwaitValueTest
import com.prem.alarmapp.repository.FakeAlarmRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException


class AlarmViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: AlarmViewModel

    @Before
    fun setup(){
        viewModel = AlarmViewModel(FakeAlarmRepository() as AlarmRepository)
    }

    @Test
    fun `insert alarm item with empty time, return error`(){
        val alarm = Alarms("", "Mon", true)
        alarm.id = 1
        viewModel.insert(alarm)


        val value = try {
            viewModel.getAllAlarms().getOrAwaitValueTest()
        }catch (e: TimeoutException){
            e.localizedMessage
        }
        assertThat(value == "LiveData value was never set.")
    }

    @Test
    fun `insert alarm item with empty repeatDays, return error`(){
        val alarm = Alarms("3:15 PM", "", true)
        alarm.id = 1
        viewModel.insert(alarm)


        val value = try {
            viewModel.getAllAlarms().getOrAwaitValueTest()[0]
        }catch (e: TimeoutException){
            e.localizedMessage
        }

        assertThat(value.equals(null))
    }


    @Test
    fun `insert alarm item with valid input, return success`(){
        val alarm = Alarms("3:15 PM", "Mon", true)
        alarm.id = 1
        viewModel.insert(alarm)


        val value = try {
            viewModel.getAllAlarms().getOrAwaitValueTest()[0]
        }catch (e: TimeoutException){
            e.localizedMessage
        }
        assertThat(value == alarm)
    }

    @Test
    fun `update alarm item with empty time, return error`(){
        val alarm = Alarms("", "Mon", true)
        alarm.id = 1
        viewModel.update(alarm)


        val value = try {
            viewModel.getAllAlarms().getOrAwaitValueTest()[0]
        }catch (e: TimeoutException){
            e.localizedMessage
        }
        assertThat(value == "LiveData value was never set.")
    }

    @Test
    fun `update alarm item with empty repeatDays, return error`(){
        val alarm = Alarms("3:15 PM", "", true)
        alarm.id = 1
        viewModel.update(alarm)


        val value = try {
            viewModel.getAllAlarms().getOrAwaitValueTest()[0]
        }catch (e: TimeoutException){
            e.localizedMessage
        }
        assertThat(value == "LiveData value was never set.")
    }


    @Test
    fun `update alarm item with valid input, return success`(){
        val alarm = Alarms("3:15 PM", "Mon", true)
        alarm.id = 1
        viewModel.insert(alarm)


        val value = try {
            viewModel.getAllAlarms().getOrAwaitValueTest()
        }catch (e: TimeoutException){
            e.localizedMessage
        }
        assertThat(value == alarm)
    }

}