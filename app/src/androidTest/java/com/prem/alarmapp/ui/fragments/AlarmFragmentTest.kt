package com.prem.alarmapp.ui.fragments

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import com.google.common.truth.Truth.assertThat
import com.prem.alarmapp.R
import com.prem.alarmapp.data.entities.Alarms
import com.prem.alarmapp.getOrAwaitValue
import com.prem.alarmapp.launchFragmentInHiltContainer
import com.prem.alarmapp.ui.adapter.AlarmAdapter
import com.prem.alarmapp.ui.viewmodels.AlarmViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AlarmFragmentTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var testFragmentFactory: TestAlarmFragmentFactory
    private lateinit var uiDevice: UiDevice

    @Before
    fun setup(){
        hiltRule.inject()
        testFragmentFactory = TestAlarmFragmentFactory()
        uiDevice = UiDevice.getInstance(getInstrumentation())
    }

    @Test
    fun emptyList_RecyclerviewShouldDisplayEmptyMessage(){
        launchFragmentInHiltContainer<AlarmFragment>(
            fragmentFactory = testFragmentFactory
        ) {

        }
        onView(withId(R.id.emptyRecView)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }


    @Test
    fun insertAlarmItem_ItemInDb(){
        var testViewModel : AlarmViewModel? = null
        val calendar = Calendar.getInstance()
        launchFragmentInHiltContainer<AlarmFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
        }

        onView(withId(R.id.fabAdd)).perform(click())
        // Sets a time in a view picker widget
        onView(isAssignableFrom(DatePicker::class.java)).perform(
            PickerActions.setDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        )
        onView(withId(android.R.id.button1)).perform(click())
        onView(isAssignableFrom(TimePicker::class.java)).perform(
            PickerActions.setTime(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
            )
        )
        onView(withText("OK")).perform(click())

        assertThat(testViewModel?.getAllAlarms()?.getOrAwaitValue()).isNotEmpty()
    }

    @Test
    fun swipeAlarmItem_deleteItemInDb(){
        val alarm = Alarms(1628167127846, true, 1)
        var testViewModel : AlarmViewModel? = null
        launchFragmentInHiltContainer<AlarmFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            viewModel?.insertAlarmItem(alarm.time, alarm.AlarmIsEnabled)
        }

        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<AlarmAdapter.AlarmViewHolder>(
                0,
                ViewActions.swipeLeft()
            )
        )

        assertThat(testViewModel?.getAllAlarms()?.getOrAwaitValue()).isEmpty()
    }

    @Test
    fun insertAlarmItem_InDb_AndObserve(){
        var testViewModel : AlarmViewModel? = null
        val calendar = Calendar.getInstance()
        launchFragmentInHiltContainer<AlarmFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
        }

        onView(withId(R.id.fabAdd)).perform(click())
        // Sets a time in a view picker widget
        onView(isAssignableFrom(DatePicker::class.java)).perform(
            PickerActions.setDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        )
        onView(withId(android.R.id.button1)).perform(click())
        onView(isAssignableFrom(TimePicker::class.java)).perform(
            PickerActions.setTime(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
            )
        )
        onView(withText("OK")).perform(click())
        // Confirm the time
        assertThat(testViewModel?.getAllAlarms()?.getOrAwaitValue()).isNotEmpty()
    }

    @Test
    fun insertAlarmItem_ItemInDb_Observe_AndDelete(){
        var testViewModel : AlarmViewModel? = null
        val calendar = Calendar.getInstance()
        launchFragmentInHiltContainer<AlarmFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
        }

        onView(withId(R.id.fabAdd)).perform(click())
        // Sets a time in a view picker widget
        onView(isAssignableFrom(DatePicker::class.java)).perform(
            PickerActions.setDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        )
        onView(withId(android.R.id.button1)).perform(click())
        onView(isAssignableFrom(TimePicker::class.java)).perform(
            PickerActions.setTime(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
            )
        )
        onView(withText("OK")).perform(click())
        // Confirm the time
        assertThat(testViewModel?.getAllAlarms()?.getOrAwaitValue()).isNotEmpty()

        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<AlarmAdapter.AlarmViewHolder>(
                0,
                ViewActions.swipeLeft()
            )
        )
        assertThat(testViewModel?.getAllAlarms()?.getOrAwaitValue()).isEmpty()
    }

    @Test
    fun addAlarmButtom_verifiyNotificationVisibilty(){
        var testViewModel : AlarmViewModel? = null
        val calendar = Calendar.getInstance()
        launchFragmentInHiltContainer<AlarmFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
        }

        onView(withId(R.id.fabAdd)).perform(click())
        // Sets a time in a view picker widget
        onView(isAssignableFrom(DatePicker::class.java)).perform(
            PickerActions.setDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        )
        onView(withId(android.R.id.button1)).perform(click())
        onView(isAssignableFrom(TimePicker::class.java)).perform(
            PickerActions.setTime(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
            )
        )
        onView(withText("OK")).perform(click())
        // Confirm the time
        assertThat(testViewModel?.getAllAlarms()?.getOrAwaitValue()).isNotEmpty()

        val expectedAppName = "AlarmApp"
        val expectedTitle = "Scheduled Reminder"
        uiDevice.openNotification()
        uiDevice.wait(Until.hasObject(By.textStartsWith(expectedAppName)), 2000)
        val title: UiObject2 = uiDevice.findObject(By.text(expectedTitle))
        assertEquals(expectedTitle, title.text)
    }
}