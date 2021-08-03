package com.prem.alarmapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.prem.alarmapp.MainCoroutineRule
import com.prem.alarmapp.data.entities.Alarms
import com.prem.alarmapp.data.repository.AlarmRepository
import com.prem.alarmapp.getOrAwaitValueTest
import com.prem.alarmapp.repository.FakeAlarmRepository
import com.prem.alarmapp.utils.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
class AlarmViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: AlarmViewModel

    @Before
    fun setup(){
        viewModel = AlarmViewModel(FakeAlarmRepository())
    }

    @Test
    fun `insert alarm item with empty time, return error`(){
        viewModel.insertAlarmItem("", "Mon", true)

        val value = viewModel.insertAlarmItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert alarm item with empty repeatDays, return error`(){
        viewModel.insertAlarmItem("3:15 PM", "", true)

        val value = viewModel.insertAlarmItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }


    @Test
    fun `insert alarm item with valid input, return success`(){
        viewModel.insertAlarmItem("3:15 PM", "Mon", true)

        val value = viewModel.insertAlarmItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `update alarm item with empty time, return error`(){
        viewModel.updateAlarmItem(1,"", "Mon", true)

        val value = viewModel.updateAlarmItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `update alarm item with empty repeatDays, return error`(){
        viewModel.updateAlarmItem(1, "3:15 PM", "", true)

        val value = viewModel.updateAlarmItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }


    @Test
    fun `update alarm item with valid input, return success`(){
        viewModel.updateAlarmItem(1,"3:15 PM", "Mon", true)

        val value = viewModel.updateAlarmItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `delete alarm, return success`(){
        val alarms = Alarms("3:15 PM", "Mon", true)
        viewModel.insertAlarmItem(alarms.time, alarms.repeatDays, alarms.AlarmIsEnabled)

        viewModel.delete(alarms)

        val value = viewModel.deleteAlarmItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

}