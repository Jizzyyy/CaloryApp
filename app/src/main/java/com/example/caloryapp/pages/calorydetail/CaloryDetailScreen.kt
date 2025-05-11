package com.example.caloryapp.pages.calorydetail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.foodmodel.FoodCategory
import com.example.caloryapp.foodmodel.PlateDiagram
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.semibold
import com.example.caloryapp.viewmodel.CaloryHistoryViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Layar detail riwayat kalori
 */
@Composable
fun CaloryHistoryDetailScreen(
    navController: NavController,
    historyId: String,
    viewModel: CaloryHistoryViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    // Muat data detail saat komponen ditampilkan
    LaunchedEffect(historyId) {
        viewModel.loadHistoryDetail(historyId)
    }

    // Bersihkan state detail saat komponen dihancurkan
    DisposableEffect(Unit) {
        onDispose {
            viewModel.selectedHistory = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp, vertical = 45.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header dengan tombol kembali
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = primary
                    )
                }

                Text(
                    text = "Detail Kalori",
                    style = TextStyle(
                        fontSize = 24.sp,
                        color = primary,
                        fontFamily = bold
                    ),
                    modifier = Modifier.weight(1f)
                )

                // Tombol hapus
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.deleteHistory(historyId) { success ->
                                if (success) {
                                    navController.popBackStack()
                                }
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus",
                        tint = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Tampilkan loading atau konten
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = primary
                )
            } else if (viewModel.selectedHistory != null) {
                val history = viewModel.selectedHistory!!

                // Total kalori
                Text(
                    text = "${history.totalCalories}",
                    style = TextStyle(
                        fontSize = 48.sp,
                        color = primary,
                        fontFamily = bold
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Kalori",
                    style = TextStyle(
                        fontSize = 24.sp,
                        color = primary,
                        fontFamily = semibold
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tanggal dan waktu
                val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
                Text(
                    text = "Tercatat pada ${dateFormat.format(history.getDate())}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Diagram komposisi makanan
                // Konversi kembali Map<String, Float> ke Map<FoodCategory, Float>
                val foodCategories = history.foodComposition.mapKeys { entry ->
                    FoodCategory.values().find { it.displayName == entry.key } ?: FoodCategory.OTHER
                }

                Box(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    PlateDiagram(
                        categories = foodCategories,
                        modifier = Modifier.size(200.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Breakdown kalori per kategori
                Text(
                    text = "Komposisi Kalori:",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = primary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tampilkan breakdown per kategori
                history.foodComposition.forEach { (categoryName, percentage) ->
                    val category = FoodCategory.values().find { it.displayName == categoryName }
                    val colorHex = category?.colorHex ?: "#A0A0A0"
                    val icon = category?.icon ?: "🍽️"
                    val calories = history.caloriesPerCategory[categoryName] ?: 0

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$icon $categoryName",
                            color = Color(android.graphics.Color.parseColor(colorHex))
                        )

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "$calories kal",
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "(%.0f%%)".format(percentage * 100),
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Tombol kembali ke riwayat
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = primary)
                ) {
                    Text(
                        text = "Kembali ke Riwayat",
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            } else if (viewModel.errorMessage != null) {
                // Error message
                Text(
                    text = viewModel.errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                // Jika data tidak ditemukan
                Text(
                    text = "Data tidak ditemukan",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}