//package com.example.caloryapp.viewmodel
//
//import android.content.Context
//import android.net.Uri
//import android.util.Log
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import com.example.caloryapp.foodmodel.FoodCategory
//import com.example.caloryapp.foodmodel.FoodDetectionResult
//import com.example.caloryapp.foodmodel.calculateTotalCalories
//import com.example.caloryapp.repository.CaloryRepository
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class FoodDetectionViewModel : ViewModel() {
//
//    // Repository untuk menyimpan data kalori
//    private val caloryRepository = CaloryRepository()
//
//    // State untuk UI
//    var isLoading by mutableStateOf(false)
//    var detectionResult by mutableStateOf<FoodDetectionResult?>(null)
//    var errorMessage by mutableStateOf<String?>(null)
//
//    // State untuk tracking penyimpanan
//    var isSaving by mutableStateOf(false)
//    var saveSuccess by mutableStateOf<Boolean?>(null)
//    var lastSavedId by mutableStateOf<String?>(null)
//
//    /**
//     * Mendeteksi makanan dari gambar
//     * Note: Implementasi sebenarnya akan menggunakan ML model
//     */
//    fun detectFoodFromImage(context: Context, imageUri: Uri) {
//        isLoading = true
//        errorMessage = null
//
//        // Simulasi proses deteksi (ganti dengan implementasi ML yang sebenarnya)
//        // Dalam kasus nyata, ini akan menggunakan TensorFlow Lite atau ML Kit
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                // Lakukan deteksi (simulasi delay)
//                withContext(Dispatchers.IO) {
//                    Thread.sleep(1500) // Simulasi delay proses ML
//                }
//
//                // Demo hasil - dalam aplikasi nyata ini akan datang dari model ML
//                // Contoh hasil deteksi: makanan dengan karbohidrat dominan
//                val detectedCategories = mapOf(
//                    FoodCategory.CARBS to 0.5f,       // 50% karbohidrat
//                    FoodCategory.PROTEIN to 0.25f,    // 25% protein
//                    FoodCategory.VEGETABLES to 0.15f, // 15% sayuran
//                    FoodCategory.FRUITS to 0.05f,     // 5% buah
//                    FoodCategory.OTHER to 0.05f       // 5% lainnya
//                )
//
//                // Tentukan kategori utama (yang memiliki persentase tertinggi)
//                val mainEntry = detectedCategories.maxByOrNull { it.value }
//                    ?: throw Exception("No categories detected")
//
//                val result = FoodDetectionResult(
//                    mainCategory = mainEntry.key,
//                    confidence = mainEntry.value,
//                    allCategories = detectedCategories
//                )
//
//                withContext(Dispatchers.Main) {
//                    detectionResult = result
//                    isLoading = false
//                }
//            } catch (e: Exception) {
//                Log.e("FoodDetectionViewModel", "Error detecting food", e)
//                withContext(Dispatchers.Main) {
//                    errorMessage = "Terjadi kesalahan: ${e.message}"
//                    isLoading = false
//                }
//            }
//        }
//    }
//
//    /**
//     * Menyimpan hasil deteksi makanan ke Firebase untuk pengguna yang sedang login
//     */
//    fun saveDetectionResult(username: String) {
//        // Validasi input
//        val result = detectionResult ?: run {
//            errorMessage = "Tidak ada hasil deteksi untuk disimpan"
//            return
//        }
//
//        isSaving = true
//        saveSuccess = null
//
//        // Hitung total kalori
//        val totalCalories = result.allCategories.calculateTotalCalories()
//
//        // Simpan ke Firestore (tanpa gambar)
//        caloryRepository.saveCaloryHistory(
//            username = username,
//            result = result,
//            totalCalories = totalCalories
//        ) { success, id ->
//            isSaving = false
//            saveSuccess = success
//            lastSavedId = id
//
//            if (!success) {
//                errorMessage = "Gagal menyimpan data ke database"
//            }
//        }
//    }
//
//    /**
//     * Reset state setelah navigasi atau selesai
//     */
//    fun resetState() {
//        detectionResult = null
//        errorMessage = null
//        isLoading = false
//        isSaving = false
//        saveSuccess = null
//        lastSavedId = null
//    }
//}

package com.example.caloryapp.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caloryapp.foodmodel.FoodDetectionResult
import com.example.caloryapp.foodmodel.FoodDetector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class FoodDetectionViewModel : ViewModel() {
    var detectionResult by mutableStateOf<FoodDetectionResult?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun detectFoodFromImage(context: Context, uri: Uri) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                val bitmap = loadBitmapFromUri(context, uri)

                // Buat detector di sini, tidak dalam withContext
                val detector = FoodDetector(context)

                val result = withContext(Dispatchers.IO) {
                    detector.detectFood(bitmap)
                }
                detectionResult = result
            } catch (e: IOException) {
                Log.e("FoodViewModel", "Error IO: ${e.message}", e)
                errorMessage = "Gagal memuat gambar atau model: ${e.message}"
            } catch (e: Exception) {
                Log.e("FoodViewModel", "Error umum: ${e.message}", e)
                errorMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap {
        val contentResolver = context.contentResolver
        return contentResolver.openInputStream(uri).use { inputStream ->
            android.graphics.BitmapFactory.decodeStream(inputStream)
        } ?: throw IOException("Gagal membuka gambar")
    }
}