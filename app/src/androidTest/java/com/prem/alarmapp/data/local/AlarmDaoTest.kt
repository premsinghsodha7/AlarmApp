package com.prem.alarmapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.prem.alarmapp.data.dao.AlarmDao
import com.prem.alarmapp.data.database.AlarmDatabase
import com.prem.alarmapp.data.entities.Alarms
import com.prem.alarmapp.getOrAwaitValue
import com.prem.alarmapp.launchFragmentInHiltContainer
import com.prem.alarmapp.ui.fragments.AlarmFragment
import com.prem.alarmapp.ui.fragments.TestAlarmFragmentFactory
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class AlarmDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AlarmDatabase
    private lateinit var dao: AlarmDao

    private lateinit var testFragmentFactory: TestAlarmFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
        testFragmentFactory = TestAlarmFragmentFactory()
        dao = database.getAlarmDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun testLaunchFragmentInHiltContainer(){
        launchFragmentInHiltContainer<AlarmFragment> (
            fragmentFactory = testFragmentFactory
        ) {

        }
    }

    @Test
    fun insertAlarmItem() = runBlockingTest {
        val alarm = Alarms(1628167127846, true, 1)
        dao.insert(alarm)
        val allAlarmItem = dao.getAllAlarms().getOrAwaitValue()
        assertThat(allAlarmItem).contains(alarm)
    }

    @Test
    fun updateAlarmItem() = runBlockingTest {
        var alarm = Alarms(1628167127846, true, 1)
        dao.insert(alarm)
        alarm = Alarms(1628167127846, false, 1)
        dao.update(alarm)

        val allAlarmItem = dao.getAllAlarms().getOrAwaitValue()
        assertThat(allAlarmItem).contains(alarm)
    }

    @Test
    fun deleteAlarmItem() = runBlockingTest {
        val alarm = Alarms(1628167127846, true, 1)
        dao.insert(alarm)
        dao.delete(alarm)

        val allAlarmsItem = dao.getAllAlarms().getOrAwaitValue()

        assertThat(allAlarmsItem).doesNotContain(alarm)
    }
}