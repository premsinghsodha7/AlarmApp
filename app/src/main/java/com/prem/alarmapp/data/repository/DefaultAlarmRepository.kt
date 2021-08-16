package com.prem.alarmapp.data.repository

import com.prem.alarmapp.data.dao.AlarmDao
import com.prem.alarmapp.data.entities.Alarms
import javax.inject.Inject


class DefaultAlarmRepository @Inject constructor(private val dao: AlarmDao) : AlarmRepository {
    override suspend fun insert(alarm: Alarms) = dao.insert(alarm)
    override suspend fun update(alarm: Alarms) = dao.update(alarm)
    override suspend fun delete(alarm: Alarms) = dao.delete(alarm)
    override suspend fun deleteAllAlarms() = dao.deleteAllAlarms()
    override fun getAllAlarms() = dao.getAllAlarms()
}