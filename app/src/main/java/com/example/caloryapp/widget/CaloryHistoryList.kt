package com.example.caloryapp.widget


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caloryapp.model.CaloryHistoryModel
import com.example.caloryapp.ui.theme.medium
import com.example.caloryapp.ui.theme.semibold
import com.example.caloryapp.viewmodel.CaloryHistoryViewModel

/**
 * Komponen list riwayat kalori
 */
@Composable
fun CaloryHistoryList(
    viewModel: CaloryHistoryViewModel,
    username: String,
    onItemClick: (CaloryHistoryModel) -> Unit = {}
) {
    // Muat data saat komponen pertama kali ditampilkan
    LaunchedEffect(username) {
        viewModel.loadHistoryByUsername(username)
    }

    // Bersihkan state saat komponen dihancurkan
    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        if (viewModel.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF4AB54A) // Hijau sesuai dengan tema
            )
        } else if (viewModel.historyList.isEmpty()) {
            // Pesan jika list kosong
            Text(
                text = "Belum ada riwayat makanan",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = medium,
                    color = Color.Gray
                ),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        } else {
            // List riwayat kalori
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(viewModel.historyList) { history ->
                    CaloryHistoryItem(
                        history = history,
                        onClick = { onItemClick(history) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Spacer di akhir list untuk padding bottom
                item {
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        }

        // Error message
        viewModel.errorMessage?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
    }
}

/**
 * Item riwayat kalori
 */
@Composable
fun CaloryHistoryItem(
    history: CaloryHistoryModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4AB54A).copy(alpha = 0.6f)) // Warna hijau semi-transparan sesuai gambar
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${history.totalCalories} Kalori",
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color.White,
                            fontFamily = semibold
                        )
                    )

                    Text(
                        text = "Lihat Detail",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.White,
                            fontFamily = medium,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }

                // Timestamp (opsional)
                Text(
                    text = history.getFormattedDate(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    }
}