package com.example.caloryapp.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


data class CaloryModel(
    val calories: Int = 0,
    val date: String = "",
    val username: String = "",
    val imagePath: String = "",
)

fun CaloryModel.isToday(): Boolean {
    val today = Calendar.getInstance()
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return format.format(today.time) == this.date
}

fun CaloryModel.isThisWeek(): Boolean {
    val cal = Calendar.getInstance()
    val currentWeek = cal.get(Calendar.WEEK_OF_YEAR)
    val currentYear = cal.get(Calendar.YEAR)

    val modelDate = Calendar.getInstance().apply {
        time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this@isThisWeek.date)!!
    }

    return modelDate.get(Calendar.WEEK_OF_YEAR) == currentWeek &&
            modelDate.get(Calendar.YEAR) == currentYear
}

fun CaloryModel.isThisMonth(): Boolean {
    val cal = Calendar.getInstance()
    val currentMonth = cal.get(Calendar.MONTH)
    val currentYear = cal.get(Calendar.YEAR)

    val modelDate = Calendar.getInstance().apply {
        time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this@isThisMonth.date)!!
    }

    return modelDate.get(Calendar.MONTH) == currentMonth &&
            modelDate.get(Calendar.YEAR) == currentYear
}