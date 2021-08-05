package com.prem.alarmapp.ui.fragments

import android.widget.TimePicker
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.filters.MediumTest
import com.prem.alarmapp.R
import com.prem.alarmapp.launchFragmentInHiltContainer
import com.prem.alarmapp.ui.AlarmViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions.setTime
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.common.truth.Truth.assertThat
import com.prem.alarmapp.data.entities.Alarms
import com.prem.alarmapp.getOrAwaitValue


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class CreateAlarmFragmentTest{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var testFragmentFactory: TestAlarmFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
        testFragmentFactory = TestAlarmFragmentFactory()
    }

//    @Test
//    fun chooseTime_verifyValueInTextView(){
//        val navController = Mockito.mock(NavController::class.java)
//        var testViewModel : AlarmViewModel? = null
//        launchFragmentInHiltContainer<CreateAlarmFragment>(
//            fragmentFactory = testFragmentFactory
//        ) {
//            androidx.navigation.Navigation.setViewNavController(requireView(), navController)
//            testViewModel = viewModel
//        }
//
//        onView(withId(R.id.btn_choose_time)).perform(click())
//        // Sets a time in a view picker widget
//        onView(isAssignableFrom(TimePicker::class.java)).perform(setTime(10, 0))
//        // Confirm the time
//        onView(withId(android.R.id.button1)).perform(click())
//        onView(withId(R.id.timeTV)).check(matches(withText("10:00 AM")))
//        onView(withId(R.id.cg_days_chips)).check(matches(hasChildCount(7)))
//    }
//
//    @Test
//    fun countChipViewChild(){
//        val navController = Mockito.mock(NavController::class.java)
//        var testViewModel : AlarmViewModel? = null
//        launchFragmentInHiltContainer<CreateAlarmFragment>(
//            fragmentFactory = testFragmentFactory
//        ) {
//            Navigation.setViewNavController(requireView(), navController)
//            testViewModel = viewModel
//        }
//        onView(withId(R.id.cg_days_chips)).check(matches(hasChildCount(7)))
//    }
//
//    @Test
//    fun checkTimeNotSelected_verifyErrorSnackbar(){
//        val navController = Mockito.mock(NavController::class.java)
//        var testViewModel : AlarmViewModel? = null
//        launchFragmentInHiltContainer<CreateAlarmFragment>(
//            fragmentFactory = testFragmentFactory
//        ) {
//            Navigation.setViewNavController(requireView(), navController)
//            testViewModel = viewModel
//        }
//        onView(withText("Every Monday")).perform(click())
//        onView(withText("Every Tuesday")).perform(click())
//
//        onView(withId(R.id.fab_save)).perform(click())
//        onView(withId(com.google.android.material.R.id.snackbar_text))
//            .check(matches(withText("please select a time")))
//    }
//
//    @Test
//    fun clickInsertIntoDB_AlarmInsertedIntoDB(){
//        val navController = Mockito.mock(NavController::class.java)
//        var testViewModel : AlarmViewModel? = null
//        launchFragmentInHiltContainer<CreateAlarmFragment>(
//            fragmentFactory = testFragmentFactory
//        ) {
//            androidx.navigation.Navigation.setViewNavController(requireView(), navController)
//            testViewModel = viewModel
//        }
//
//        onView(withText("Every Monday")).perform(click())
//        onView(withText("Every Tuesday")).perform(click())
//
//        onView(withId(R.id.btn_choose_time)).perform(click())
//        // Sets a time in a view picker widget
//        onView(isAssignableFrom(TimePicker::class.java)).perform(setTime(10, 0))
//        // Confirm the time
//        onView(withId(android.R.id.button1)).perform(click())
//        onView(withId(R.id.fab_save)).perform(click())
//
//        assertThat(testViewModel?.getAllAlarms()?.getOrAwaitValue()).contains(Alarms("10:00 AM","Mon Tue",true, 1))
//    }

//    @Test
//    fun pressBackButton_popBackStack() {
//        val navController = Mockito.mock(NavController::class.java)
//        launchFragmentInHiltContainer<CreateAlarmFragment>(
//            fragmentFactory = testFragmentFactory
//        ) {
//            Navigation.setViewNavController(requireView(), navController)
//        }
//
//        Espresso.pressBack()
//        Mockito.verify(navController).popBackStack()
//    }
}