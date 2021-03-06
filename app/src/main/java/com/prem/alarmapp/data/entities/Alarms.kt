package com.prem.alarmapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms_items")
data class Alarms(
    @ColumnInfo(name = "alarm_time")
    var time: Long,
    @ColumnInfo(name = "AlarmIsEnabled")
    var AlarmIsEnabled: Boolean = true,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)


