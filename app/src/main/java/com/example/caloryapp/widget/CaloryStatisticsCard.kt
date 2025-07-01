package com.example.caloryapp.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caloryapp.model.CaloryModel
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.medium
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.semibold
import com.example.caloryapp.utils.CaloryCalculator
import com.example.caloryapp.viewmodel.CaloryHistoryViewModel
import com.example.caloryapp.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CaloryStatisticsCard(
    userViewModel: UserViewModel,
    caloryHistoryViewModel: CaloryHistoryViewModel
) {
    val user = userViewModel.user.value ?: return
    val caloriesList = caloryHistoryViewModel.calorieList.value

    // Hitung statistik
    val weeklyTotal = calculateWeeklyTotal(caloriesList)
    val monthlyTotal = calculateMonthlyTotal(caloriesList)

    // Kebutuhan kalori mingguan dan bulanan
    val dailyNeeds = CaloryCalculator.getDailyCaloryNeeds(user.gender)
    val weeklyNeeds = dailyNeeds * 7
    val monthlyNeeds = dailyNeeds * 30

    // Persentase konsumsi
    val weeklyPercentage = (weeklyTotal.toFloat() / weeklyNeeds) * 100
    val monthlyPercentage = (monthlyTotal.toFloat() / monthlyNeeds) * 100

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Statistik Konsumsi Kalori",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontFamily = bold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Statistik mingguan
            Text(
                text = "Minggu Ini",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = semibold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = (weeklyPercentage / 100).coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                backgroundColor = Color.LightGray,
                color = primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$weeklyTotal kkal",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = primary,
                        fontFamily = medium
                    )
                )

                Text(
                    text = "$weeklyNeeds kkal",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = medium
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Statistik bulanan
            Text(
                text = "Bulan Ini",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = semibold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = (monthlyPercentage / 100).coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                backgroundColor = Color.LightGray,
                color = primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$monthlyTotal kkal",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = primary,
                        fontFamily = medium
                    )
                )

                Text(
                    text = "$monthlyNeeds kkal",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = medium
                    )
                )
            }
        }
    }
}

// Fungsi untuk menghitung total kalori mingguan
fun calculateWeeklyTotal(caloriesList: List<CaloryModel>): Int {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    val startOfWeek = calendar.timeInMillis

    calendar.add(Calendar.DAY_OF_WEEK, 6)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)

    val endOfWeek = calendar.timeInMillis

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    return caloriesList
        .filter {
            try {
                val date = dateFormat.parse(it.date)
                date != null && date.time >= startOfWeek && date.time <= endOfWeek
            } catch (e: Exception) {
                false
            }
        }
        .sumOf { it.calories }
}

// Fungsi untuk menghitung total kalori bulanan
fun calculateMonthlyTotal(caloriesList: List<CaloryModel>): Int {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    val startOfMonth = calendar.timeInMillis

    calendar.add(Calendar.MONTH, 1)
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)

    val endOfMonth = calendar.timeInMillis

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    return caloriesList
        .filter {
            try {
                val date = dateFormat.parse(it.date)
                date != null && date.time >= startOfMonth && date.time <= endOfMonth
            } catch (e: Exception) {
                false
            }
        }
        .sumOf { it.calories }
}