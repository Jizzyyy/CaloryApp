package com.example.caloryapp.widget


//package com.example.caloryapp.widget

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caloryapp.model.CaloryModel
import com.example.caloryapp.ui.theme.medium
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primary2
import com.example.caloryapp.ui.theme.semibold
import com.example.caloryapp.utils.LocalImageStorage
import com.example.caloryapp.viewmodel.CaloryHistoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Komponen list riwayat kalori
 */
@Composable
fun CaloryHistoryList(
    viewModel: CaloryHistoryViewModel,
    username: String,
    onItemClick: (CaloryModel) -> Unit = {},
    onItemDelete: (CaloryModel) -> Unit = {}
) {
    val context = LocalContext.current

    // Muat data saat komponen pertama kali ditampilkan
    LaunchedEffect(username) {
        viewModel.loadHistoryByUsername(username)
    }

    // Bersihkan state saat komponen dihancurkan
    DisposableEffect(Unit) {
        onDispose {
            // Bersihkan gambar yang tidak digunakan saat meninggalkan layar
            viewModel.cleanupUnusedImages(context, username)
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        if (viewModel.isLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = primary
            )
        } else if (viewModel.calorieList.value.isEmpty()) {
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
                items(viewModel.calorieList.value) { calory ->
                    CaloryHistoryItem(
                        calory = calory,
                        viewModel = viewModel,
                        onClick = { onItemClick(calory) },
                        onDelete = { onItemDelete(calory) }
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
        viewModel.error.value?.let { error ->
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
 * Item riwayat kalori dengan gambar
 */
@Composable
fun CaloryHistoryItem(
    calory: CaloryModel,
    viewModel: CaloryHistoryViewModel,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current

    // State untuk menyimpan bitmap dari penyimpanan lokal
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Load bitmap saat komponen dibuat
    LaunchedEffect(calory.imagePath) {
        if (calory.imagePath.isNotEmpty()) {
            bitmap = viewModel.getImageBitmap(context, calory.imagePath)
        }
    }

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
                .background(primary2)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tampilkan gambar jika tersedia
                bitmap?.let { bmp ->
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Food Image",
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }

                // Detail kalori
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${calory.calories} Kalori",
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

                // Tanggal
                Text(
                    text = formatDate(calory.date),
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

/**
 * Format tanggal untuk tampilan
 */
fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString // Kembalikan string asli jika format tidak sesuai
    }
}

/**
 * Item riwayat kalori dengan opsi hapus
 */
@Composable
fun CaloryHistoryItemWithDelete(
    calory: CaloryModel,
    viewModel: CaloryHistoryViewModel,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current

    // State untuk menyimpan bitmap dari penyimpanan lokal
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Load bitmap saat komponen dibuat
    LaunchedEffect(calory.imagePath) {
        if (calory.imagePath.isNotEmpty()) {
            bitmap = viewModel.getImageBitmap(context, calory.imagePath)
        }
    }

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
                .background(primary2)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tampilkan gambar jika tersedia
                bitmap?.let { bmp ->
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Food Image",
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }

                // Detail kalori
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${calory.calories} Kalori",
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

                // Tanggal
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatDate(calory.date),
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tombol hapus
                    Text(
                        text = "Hapus",
                        modifier = Modifier.clickable(onClick = onDelete),
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.White,
                            fontFamily = medium,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }
            }
        }
    }
}