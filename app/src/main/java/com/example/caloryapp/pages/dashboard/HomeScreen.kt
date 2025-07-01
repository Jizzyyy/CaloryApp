package com.example.caloryapp.pages.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.R
import com.example.caloryapp.model.CaloryModel
import com.example.caloryapp.model.isThisMonth
import com.example.caloryapp.model.isThisWeek
import com.example.caloryapp.model.isToday
import com.example.caloryapp.navigation.NavigationScreen
import com.example.caloryapp.repository.CaloryRepository
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.medium
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primary2
import com.example.caloryapp.ui.theme.primarygrey
import com.example.caloryapp.ui.theme.semibold
import com.example.caloryapp.viewmodel.CaloryHistoryViewModel
import com.example.caloryapp.viewmodel.LoginState
import com.example.caloryapp.viewmodel.UserViewModel
import com.example.caloryapp.widget.FilterBar
import kotlinx.coroutines.launch
import android.util.Log
import android.widget.Toast
import androidx.compose.material.ExperimentalMaterialApi
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    drawerState: DrawerState,
    scope: kotlinx.coroutines.CoroutineScope,
    caloryHistoryViewModel: CaloryHistoryViewModel,
    viewModel: UserViewModel,
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("Semua") }
    val user = viewModel.user.value
    val caloryRepository = CaloryRepository()
    var calorieList by remember { mutableStateOf(listOf<CaloryModel>()) }

    // Cek apakah user null, jika ya, arahkan ke login
    if (user == null) {
        LaunchedEffect(Unit) {
            Log.d("HomeScreen", "User adalah null, mengarahkan ke halaman login")
            Toast.makeText(context, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show()
            navController.navigate(NavigationScreen.LoginScreen.name) {
                popUpTo(0) { inclusive = true }
            }
        }

        // Tampilkan loading sampai navigasi selesai
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = primary)
        }
        return
    }

    // Jika user tidak null, lanjutkan dengan logika normal
    val filteredList = when (selectedFilter) {
        "Hari ini" -> calorieList.filter { it.isToday() }
        "Minggu Ini" -> calorieList.filter { it.isThisWeek() }
        "Bulan Ini" -> calorieList.filter { it.isThisMonth() }
        else -> calorieList
    }

    LaunchedEffect(user) {
        try {
            // Memuat 4 riwayat terbaru untuk ditampilkan di home
            caloryHistoryViewModel.loadHistoryByUsername(user.username, 2)

            // Gunakan try-catch untuk menangkap error saat mengambil data
            caloryRepository.getCalorieData(user.username) { data ->
                calorieList = data
            }
        } catch (e: Exception) {
            Log.e("HomeScreen", "Error loading data: ${e.message}")
            // Tangani error dengan menampilkan pesan
            Toast.makeText(context, "Gagal memuat data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(modifier = Modifier.padding(horizontal = 25.dp, vertical = 45.dp)) {
            Spacer(modifier = Modifier.height(50.dp))

            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.clickable { },
                    painter = painterResource(id = R.drawable.ic_home_acc),
                    contentDescription = null
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = user.fullName,  // Aman karena user sudah dipastikan tidak null di atas
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontFamily = bold
                            )
                        )
                        Text(
                            text = "@${user.username}",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Black,
                                fontFamily = bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_profile_women),
                        contentDescription = null,
                        Modifier.size(45.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(35.dp))

            // Teks sapaan - Sudah aman karena user dipastikan tidak null
            Row(Modifier.width(215.dp)) {
                Text(
                    text = "Hai ${user.fullName}, Bagaimana kabar kamu hari ini?",
                    style = TextStyle(
                        fontSize = 22.sp,
                        color = Color.Black,
                        fontFamily = bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            androidx.compose.material3.Divider(color = primary.copy(alpha = 0.2f), thickness = 3.dp)
            Spacer(modifier = Modifier.height(15.dp))

            // Filter Bar
            FilterBar(selectedFilter = selectedFilter, onFilterSelected = { selectedFilter = it })
            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                if (filteredList.isEmpty()) {
                    Text(
                        text = "Belum ada riwayat makanan",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = medium,
                            color = Color.Gray
                        ),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(vertical = 24.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(filteredList) { calory ->
                            Card(
                                modifier = Modifier
                                    .size(width = 365.dp, height = 100.dp)
                                    .padding(vertical = 8.dp)
                                    .fillMaxSize(),
                                backgroundColor = primary2,
                                shape = RoundedCornerShape(20.dp),
                                onClick = {
                                    val encodedImagePath = URLEncoder.encode(calory.imagePath, StandardCharsets.UTF_8.name())

                                    // Navigasi ke halaman detail
                                    navController.navigate(
                                        "${NavigationScreen.CaloryDetailScreen.name}/${calory.date}/${calory.calories}/${encodedImagePath}"
                                    )
                                }
                            ) {
                                Column(modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                    Arrangement.Center,) {
                                    Text(
                                        text = "${calory.calories} Kalori",
                                        style = TextStyle(
                                            fontSize = 26.sp,
                                            color = Color.White,
                                            fontFamily = semibold
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    androidx.compose.material3.Divider(modifier = Modifier.width(180.dp), color = Color.White, thickness = 3.dp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Lihat Detail",
                                        style = TextStyle(
                                            fontSize = 13.sp,
                                            color = Color.White,
                                            fontFamily = semibold,
                                            textDecoration = TextDecoration.Underline
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 50.dp, bottom = 70.dp),
            contentColor = Color.White,
            backgroundColor = primary,
            onClick = { navController.navigate(NavigationScreen.ScreenTest.name) }) {
            Icon(painter = painterResource(id = R.drawable.scan), contentDescription = null)
        }
    }
}