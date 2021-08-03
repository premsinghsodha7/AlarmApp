package com.prem.alarmapp.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class AlarmFragmentFactory : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            AlarmFragment::class.java.name -> AlarmFragment()
            CreateAlarmFragment::class.java.name -> CreateAlarmFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}