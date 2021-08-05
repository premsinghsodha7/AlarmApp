package com.prem.alarmapp.utils

import android.text.format.DateFormat
import java.util.concurrent.atomic.AtomicInteger

object Util {
    private val seed = AtomicInteger()
    fun getRandomInt() = seed.getAndIncrement() + System.currentTimeMillis().toInt()

    fun convertDateTime(timeInMillis: Long): String =
        DateFormat.format("E MMMM dd,yyyy hh:mm a", timeInMillis).toString()

    fun convertDate(timeInMillis: Long): String =
        DateFormat.format("EEEE", timeInMillis).toString()

    fun convertTime(timeInMillis: Long): String =
        DateFormat.format("hh:mm a", timeInMillis).toString()
}