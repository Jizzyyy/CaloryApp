package com.example.caloryapp.utils

import com.example.caloryapp.model.CaloryModel

object CaloryCalculator {
    // Kebutuhan kalori harian berdasarkan gender
    const val MALE_DAILY_CALORY_NEEDS = 2650
    const val FEMALE_DAILY_CALORY_NEEDS = 2250

    /**
     * Menghitung kebutuhan kalori berdasarkan gender
     */
    fun getDailyCaloryNeeds(gender: String): Int {
        return when (gender.lowercase()) {
            "male", "pria", "laki-laki" -> MALE_DAILY_CALORY_NEEDS
            "female", "wanita", "perempuan" -> FEMALE_DAILY_CALORY_NEEDS
            else -> 2400 // Default jika gender tidak diketahui
        }
    }

    /**
     * Menghitung persentase konsumsi kalori terhadap kebutuhan harian
     */
    fun calculateDailyPercentage(consumedCalories: Int, gender: String): Float {
        val dailyNeeds = getDailyCaloryNeeds(gender)
        return (consumedCalories.toFloat() / dailyNeeds) * 100
    }

    /**
     * Menghitung total kalori yang dikonsumsi pada hari ini
     */
    fun calculateTodayTotalCalories(caloriesList: List<CaloryModel>): Int {
        // Filter kalori hari ini
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            .format(java.util.Date())

        // Jumlahkan semua kalori hari ini
        return caloriesList
            .filter { it.date == today }
            .sumOf { it.calories }
    }

    /**
     * Menghitung sisa kalori yang dapat dikonsumsi
     */
    fun calculateRemainingCalories(consumedCalories: Int, gender: String): Int {
        val dailyNeeds = getDailyCaloryNeeds(gender)
        val remaining = dailyNeeds - consumedCalories
        return if (remaining < 0) 0 else remaining
    }
}