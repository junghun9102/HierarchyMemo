package com.yangdroid.hierarchymemo.utils

import android.content.Context
import com.yangdroid.hierarchymemo.R
import java.text.SimpleDateFormat
import java.util.*

object StringUtils {

    fun getYearMonthDayStr(context: Context, date: Date) = date.format(getYearMonthDay(context))

    fun getTodayTodo(context: Context): String {
        val dateString = Date().format(getYearMonthDay(context))
        val todo = getTodo(context)
        return "<$dateString> $todo"
    }

    fun getThisWeekTodo(context: Context): String {
        val weekStartAndEndDate = DateUtils.getWeekStartAndEndDate(Date())
        val startDate = weekStartAndEndDate.first
        val endDate = weekStartAndEndDate.second

        val startDateString = startDate.format(getYearMonthDay(context))
        var endDateString = endDate.format(getYearMonthDay(context))
        if (Locale.getDefault() == Locale.KOREA) {
            endDateString = endDateString.split(" ").toMutableList()
                .apply { removeAll(startDateString.split(" ")) }
                .reduce { acc, s -> "$acc$s" }
        }
        val todo = getTodo(context)

        return "<$startDateString ~ $endDateString> $todo"
    }

    fun getThisMonthTodo(context: Context): String {
        val dateString = Date().format(getYearMonth(context))
        val todo = getTodo(context)
        return "<$dateString> $todo"
    }

    private fun getYearMonthDay(context: Context) = context.getString(R.string.common_date_year_month_day)
    private fun getYearMonth(context: Context) = context.getString(R.string.common_date_year_month)
    private fun getTodo(context: Context) = context.getString(R.string.main_template_todo)

}

fun Context.getTodayTodoString() = StringUtils.getTodayTodo(this)
fun Context.getThisWeekTodoString() = StringUtils.getThisWeekTodo(this)
fun Context.getThisMonthTodoString() = StringUtils.getThisMonthTodo(this)

fun Date.format(pattern: String) = SimpleDateFormat(pattern, Locale.getDefault()).format(this)