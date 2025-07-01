package com.example.caloryapp.pages.calorydetail

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.model.CaloryModel
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.medium
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.ui.theme.semibold
import com.example.caloryapp.viewmodel.CaloryHistoryViewModel
import com.example.caloryapp.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CaloryDetailScreen(
    navController: NavController,
    caloryDate: String,
    caloryCalories: Int,
    caloryImagePath: String,
    userViewModel: UserViewModel,
    caloryHistoryViewModel: CaloryHistoryViewModel
) {
    val context = LocalContext.current
    val user = userViewModel.user.value
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isDeleting by remember { mutableStateOf(false) }

    // Muat gambar jika ada path
    LaunchedEffect(caloryImagePath) {
        if (caloryImagePath.isNotEmpty()) {
            bitmap = caloryHistoryViewModel.getImageBitmap(context, caloryImagePath)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Profil Saya",
                    style = TextStyle(
                        fontSize = 25.sp,
                        color = primaryblack,
                        fontFamily = bold
                    )
                ) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = null,
                            Modifier
                                .size(28.dp)
                        )
                    }
                },
                actions = {
                    // Tombol hapus
                    IconButton(onClick = {
                        if (user != null) {
                            isDeleting = true
                            caloryHistoryViewModel.deleteCaloryData(
                                date = caloryDate,
                                calories = caloryCalories,
                                imagePath = caloryImagePath,
                                username = user.username
                            ) { success ->
                                isDeleting = false
                                if (success) {
                                    navController.navigateUp()
                                }
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = background
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(background),
            contentAlignment = Alignment.Center
        ) {
            if (isDeleting) {
                CircularProgressIndicator(color = primary)
            } else {
                // Detail kalori
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Gambar makanan
                    Box(
                        modifier = Modifier
                            .size(250.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        bitmap?.let { bmp ->
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = "Food Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } ?: Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "No Image",
                            modifier = Modifier.size(80.dp),
                            tint = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Informasi kalori
                    Text(
                        text = "$caloryCalories Kalori",
                        style = TextStyle(
                            fontSize = 32.sp,
                            color = primary,
                            fontFamily = bold
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tanggal: ${formatDate(caloryDate)}",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.Gray,
                            fontFamily = semibold
                        )
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // Tombol hapus
                    Button(
                        onClick = {
                            if (user != null) {
                                isDeleting = true
                                caloryHistoryViewModel.deleteCaloryData(
                                    date = caloryDate,
                                    calories = caloryCalories,
                                    imagePath = caloryImagePath,
                                    username = user.username
                                ) { success ->
                                    isDeleting = false
                                    if (success) {
                                        navController.navigateUp()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isDeleting
                    ) {
                        Text(
                            text = "Hapus Data Kalori",
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

// Fungsi untuk memformat tanggal
fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString // Kembalikan string asli jika format tidak sesuai
    }
}