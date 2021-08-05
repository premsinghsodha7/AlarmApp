package com.prem.alarmapp.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.prem.alarmapp.repositories.FakeAlarmRepositoryAndroidTest
import com.prem.alarmapp.ui.AlarmViewModel
import javax.inject.Inject

class TestAlarmFragmentFactory : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            AlarmFragment::class.java.name -> AlarmFragment(
                AlarmViewModel(FakeAlarmRepositoryAndroidTest())
            )
            else -> super.instantiate(classLoader, className)
        }
    }
}