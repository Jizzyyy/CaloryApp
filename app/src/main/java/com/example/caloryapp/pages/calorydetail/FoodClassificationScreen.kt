package com.example.caloryapp.pages.calorydetail
//
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.Button
//import androidx.compose.material.ButtonDefaults
//import androidx.compose.material.CircularProgressIndicator
//import androidx.compose.material.SnackbarHost
//import androidx.compose.material.SnackbarHostState
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import coil3.compose.rememberAsyncImagePainter
//import com.example.caloryapp.viewmodel.FoodDetectionViewModel
//import com.example.caloryapp.foodmodel.PlateDiagram
//import com.example.caloryapp.foodmodel.calculateTotalCalories
//import com.example.caloryapp.ui.theme.bold
//import com.example.caloryapp.ui.theme.medium
//import com.example.caloryapp.ui.theme.primary
//import com.example.caloryapp.ui.theme.semibold
//import kotlinx.coroutines.launch
//
//@Composable
//fun ScreenTest(
//    navController: NavController,
//    viewModel: FoodDetectionViewModel,
//    currentUsername: String? = null
//) {
//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//    val snackbarHostState = remember { SnackbarHostState() }
//    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
//    // state untuk menyimpan username
//    var username by remember { mutableStateOf(currentUsername ?: "") }
//    var showUsernameInput by remember { mutableStateOf(currentUsername == null) }
//
//    // launcher untuk memilih gambar
//    val getContent = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let {
//            selectedImageUri = it
//            viewModel.detectFoodFromImage(context, it)
//        }
//    }
//
//    LaunchedEffect(viewModel.saveSuccess) {
//        viewModel.saveSuccess?.let { success ->
//            if (success) {
//                snackbarHostState.showSnackbar("Berhasil menyimpan data kalori!")
//            } else if (success == false) {
//                snackbarHostState.showSnackbar("Gagal menyimpan data: ${viewModel.errorMessage ?: "Unknown error"}")
//            }
//        }
//    }
//
//    // UI Layout
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 25.dp, vertical = 50.dp)
//                .verticalScroll(rememberScrollState()),
//        ) {
//            Spacer(modifier = Modifier.height(60.dp))
//
//            Text(
//                text = "Pindai Makanan Kamu",
//                style = TextStyle(
//                    fontSize = 38.sp,
//                    color = primary,
//                    fontFamily = bold
//                )
//            )
//            Spacer(modifier = Modifier.height(18.dp))
//
//            Text(
//                text = "Penuhi Kebutuhan Kalori Kamu Hari ini Yuk!",
//                style = TextStyle(
//                    fontSize = 21.sp,
//                    color = primary,
//                    fontFamily = bold
//                )
//            )
//
//            // Input username jika belum login
//            if (showUsernameInput) {
//                Spacer(modifier = Modifier.height(16.dp))
//                androidx.compose.material.TextField(
//                    value = username,
//                    onValueChange = { username = it },
//                    label = { Text("Username") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//
//            // Selected image preview
//            if (selectedImageUri != null) {
//                Spacer(modifier = Modifier.height(40.dp))
//                Row(Modifier.fillMaxWidth(), Arrangement.Center) {
//                    Image(
//                        painter = rememberAsyncImagePainter(selectedImageUri),
//                        contentDescription = "Selected image",
//                        modifier = Modifier
//                            .size(250.dp)
//                            .clip(RoundedCornerShape(8.dp)),
//                        contentScale = ContentScale.Crop
//                    )
//                }
//                Spacer(modifier = Modifier.height(40.dp))
//            } else {
//                // Placeholder
//                Spacer(modifier = Modifier.height(40.dp))
//                Row(Modifier.fillMaxWidth(), Arrangement.Center) {
//                    Box(
//                        modifier = Modifier
//                            .size(250.dp)
//                            .clip(RoundedCornerShape(8.dp))
//                            .padding(16.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "Pilih Gambar Makanan",
//                            style = TextStyle(
//                                fontSize = 18.sp,
//                                color = primary,
//                                letterSpacing = 1.sp,
//                                fontFamily = medium,
//                                textAlign = TextAlign.Center
//                            )
//                        )
//                    }
//                }
//                Spacer(modifier = Modifier.height(40.dp))
//            }
//
//            // Button to select image
//            Button(
//                onClick = {
//                    getContent.launch("image/*")
//                },
//                modifier = Modifier
//                    .width(360.dp)
//                    .height(50.dp),
//                colors = ButtonDefaults.buttonColors(backgroundColor = primary),
//                shape = RoundedCornerShape(20.dp)
//            ) {
//                Text(
//                    text = "Pilih",
//                    style = TextStyle(
//                        fontSize = 18.sp,
//                        color = Color.White,
//                        fontFamily = semibold,
//                        textAlign = TextAlign.Center
//                    )
//                )
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // Loading indicator
//            if (viewModel.isLoading) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    CircularProgressIndicator(color = primary)
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            // Error message
//            viewModel.errorMessage?.let { error ->
//                Text(
//                    text = error,
//                    color = Color.Red,
//                    modifier = Modifier.padding(vertical = 8.dp)
//                )
//            }
//
//            // Detection results
//            viewModel.detectionResult?.let { result ->
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    // Food plate diagram
//                    PlateDiagram(
//                        categories = result.allCategories,
//                        modifier = Modifier
//                            .size(200.dp)
//                            .padding(8.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Total calories
//                    val totalCalories = result.allCategories.calculateTotalCalories()
//
//                    Text(
//                        text = "$totalCalories",
//                        style = TextStyle(
//                            fontSize = 24.sp,
//                            color = primary,
//                            fontFamily = bold,
//                            textDecoration = TextDecoration.Underline
//                        )
//                    )
//                    Text(
//                        text = "Kalori di Makanan Kamu Hari ini",
//                        style = TextStyle(
//                            fontSize = 18.sp,
//                            color = primary,
//                            fontFamily = semibold
//                        )
//                    )
//
//                    Spacer(modifier = Modifier.height(20.dp))
//
//                    // Categories breakdown
//                    result.allCategories.forEach { (category, confidence) ->
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 4.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text(
//                                text = "${category.icon} ${category.displayName}",
//                                color = Color(android.graphics.Color.parseColor(category.colorHex))
//                            )
//                            Text(
//                                text = "%.0f%%".format(confidence * 100),
//                                fontWeight = FontWeight.Medium
//                            )
//                        }
//                    }
//
//                    // Button to save results
//                    Spacer(modifier = Modifier.height(24.dp))
//
//                    if (viewModel.isSaving) {
//                        CircularProgressIndicator(color = primary)
//                    } else {
//                        Button(
//                            onClick = {
//                                if (username.isNotBlank()) {
//                                    viewModel.saveDetectionResult(username)
//                                } else {
//                                    coroutineScope.launch {
//                                        snackbarHostState.showSnackbar("Masukkan username terlebih dahulu")
//                                    }
//                                }
//                            },
//                            modifier = Modifier
//                                .width(360.dp)
//                                .height(50.dp),
//                            colors = ButtonDefaults.buttonColors(backgroundColor = primary),
//                            shape = RoundedCornerShape(20.dp),
//                            enabled = !viewModel.isSaving && username.isNotBlank()
//                        ) {
//                            Text(
//                                text = "Simpan Hasil Deteksi",
//                                style = TextStyle(
//                                    fontSize = 18.sp,
//                                    color = Color.White,
//                                    fontFamily = semibold,
//                                    textAlign = TextAlign.Center
//                                )
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//        // Snackbar untuk notifikasi
//        SnackbarHost(
//            hostState = snackbarHostState,
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(16.dp)
//        )
//    }
//}

//package com.example.caloryapp.pages.camera

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.caloryapp.viewmodel.FoodDetectionViewModel
import com.example.caloryapp.foodmodel.PlateDiagram
import com.example.caloryapp.foodmodel.calculateTotalCalories
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.medium
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.ui.theme.semibold

@Composable
fun ScreenTest(navController: NavController, viewModel: FoodDetectionViewModel) {
    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val getContent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            viewModel.detectFoodFromImage(context, it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp, vertical = 50.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Pindai Makanan Kamu",
            style = TextStyle(
                fontSize = 38.sp,
                color = primary,
                fontFamily = bold
            )
        )
        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Penuhi Kebutuhan Kalori Kamu Hari ini Yuk!",
            style = TextStyle(
                fontSize = 21.sp,
                color = primary,
                fontFamily = bold
            )
        )

        // Selected image preview
        if (selectedImageUri != null) {
            Spacer(modifier = Modifier.height(40.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Selected image",
                    modifier = Modifier
                        .size(250.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Spacer(modifier = Modifier.height(40.dp))
        } else {
            // Placeholder
            Spacer(modifier = Modifier.height(40.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material.Text(
                        text = "Pilih Gambar Makanan",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = primary,
                            letterSpacing = 1.sp,
                            fontFamily = medium,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            Spacer(modifier = Modifier.height(40.dp))
        }

        // Button to select image
        androidx.compose.material.Button(
            onClick = {
                getContent.launch("image/*")
            },
            modifier = Modifier
                .width(360.dp)
                .height(50.dp),
            colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = primary),
            shape = RoundedCornerShape(20.dp)
        ) {
            androidx.compose.material.Text(
                text = "Pilih",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = semibold,
                    textAlign = TextAlign.Center
                )
            )
        }
//        Button(
//            onClick = { },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Pilih Gambar dari Galeri")
//        }

        Spacer(modifier = Modifier.height(32.dp))

        // Loading indicator
        if (viewModel.isLoading) {
            CircularProgressIndicator()
        }

        // Error message
        viewModel.errorMessage?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Detection results
        viewModel.detectionResult?.let { result ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Text(
//                    text = "Hasil Deteksi",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )

                // Food plate diagram
                PlateDiagram(
                    categories = result.allCategories,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Main category and caloriesd
                val totalCalories = result.allCategories.calculateTotalCalories()

//                Text(
//                    text = "Kategori Utama: ${result.mainCategory.displayName}",
//                    fontWeight = FontWeight.SemiBold
//                )

                Text(
                    text = "$totalCalories",
                    style = TextStyle(
                        fontSize = 24.sp,
                        color = primary,
                        fontFamily = bold,
                        textDecoration = TextDecoration.Underline
                    )
                )
                Text(
                    text = "Kalori di Makanan Kamu Hari ini",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = primary,
                        fontFamily = semibold
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))
                result.allCategories.forEach { (category, confidence) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${category.icon} ${category.displayName}",
                            color = Color(android.graphics.Color.parseColor(category.colorHex))
                        )
                        Text(
                            text = "%.0f%%".format(confidence * 100),
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = primaryblack,
                                fontFamily = medium
                            )
                        )
                    }
                }
            }
        }
    }
}