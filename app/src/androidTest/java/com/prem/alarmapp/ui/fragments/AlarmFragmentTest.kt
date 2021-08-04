package com.prem.alarmapp.ui.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.prem.alarmapp.R
import com.prem.alarmapp.data.entities.Alarms
import com.prem.alarmapp.getOrAwaitValue
import com.prem.alarmapp.launchFragmentInHiltContainer
import com.prem.alarmapp.ui.AlarmViewModel
import com.prem.alarmapp.ui.adapter.AlarmAdapter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AlarmFragmentTest{

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

    @Test
    fun swipeAlarmItem_deleteItemInDb(){
        val alarm = Alarms("3:15 PM", "Mon", true, 1)
        var testViewModel : AlarmViewModel? = null
        launchFragmentInHiltContainer<AlarmFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            viewModel?.insert(alarm)
        }

        Espresso.onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<AlarmAdapter.AlarmViewHolder>(
                0,
                ViewActions.swipeLeft()
            )
        )
        assertThat(testViewModel?.getAllAlarms()?.getOrAwaitValue()).isEmpty()
    }

    @Test
    fun clickAddAlarmButtom_navigateToCreateAlarmFragment(){
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<AlarmFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.onView(withId(R.id.fabAdd)).perform(ViewActions.click())
        Mockito.verify(navController).navigate(R.id.action_alarmFragment_to_create_new_alarm)
    }
}