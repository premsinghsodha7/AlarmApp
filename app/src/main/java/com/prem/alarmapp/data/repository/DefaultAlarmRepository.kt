package com.prem.alarmapp.data.repository

import com.prem.alarmapp.data.database.AlarmDatabase
import com.prem.alarmapp.data.entities.Alarms


class DefaultAlarmRepository(private val db: AlarmDatabase): AlarmRepository {
    override suspend fun insert(alarm: Alarms) = db.getAlarmDao().insert(alarm)
    override suspend fun update(alarm: Alarms) = db.getAlarmDao().update(alarm)
    override suspend fun delete(alarm: Alarms) = db.getAlarmDao().delete(alarm)
    override suspend fun deleteAllAlarms() = db.getAlarmDao().deleteAllAlarms()
    override fun getAllAlarms() = db.getAlarmDao().getAllAlarms()
}