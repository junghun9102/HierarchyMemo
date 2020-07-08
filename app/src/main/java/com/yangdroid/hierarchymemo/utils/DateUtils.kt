package com.yangdroid.hierarchymemo.utils

import java.util.*

object DateUtils {
    fun getWeekStartAndEndDate(date: Date): Pair<Date, Date> {
        val calendar = Calendar.getInstance().apply { time = date }
        val indexOfWeek = (calendar.get(Calendar.DAY_OF_WEEK)-1)%7

        calendar.add(Calendar.DATE, -indexOfWeek)
        val startDate = calendar.time
        calendar.add(Calendar.DATE, 6)
        val endDate = calendar.time

        return Pair(startDate, endDate)
    }
}