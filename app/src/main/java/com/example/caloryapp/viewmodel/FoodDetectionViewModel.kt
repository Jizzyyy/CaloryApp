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
import com.example.caloryapp.foodmodel.FoodCategory
import com.example.caloryapp.foodmodel.FoodDetectionResult
import com.example.caloryapp.foodmodel.FoodDetector
import com.google.firebase.firestore.FirebaseFirestore
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

    var isSaving by mutableStateOf(false)
        private set

    var saveSuccess by mutableStateOf<Boolean?>(null)
        private set

    // Ambang batas confidence untuk kategori utama
    private val MAIN_CATEGORY_THRESHOLD = 0.35f

    // Rasio minimum antara kategori utama dan kategori kedua
    // Ini membantu memastikan bahwa model cukup yakin tentang kategori utama
    private val CONFIDENCE_RATIO_THRESHOLD = 1.5f

    fun detectFoodFromImage(context: Context, uri: Uri) {
        isLoading = true
        errorMessage = null
        detectionResult = null

        viewModelScope.launch {
            try {
                val bitmap = loadBitmapFromUri(context, uri)

                // Buat detector
                val detector = FoodDetector(context)

                val result = withContext(Dispatchers.IO) {
                    detector.detectFood(bitmap)
                }

                // Log hasil deteksi untuk debugging
                Log.d("FoodViewModel", "Detection result: ${result.mainCategory.name}, " +
                        "confidence: ${result.confidence}, " +
                        "all categories: ${result.allCategories.entries.joinToString { "${it.key.name}=${it.value}" }}")

                // Periksa dengan metode yang lebih canggih
                if (isFoodImageAdvanced(result)) {
                    detectionResult = result
                    errorMessage = null
                } else {
                    detectionResult = null
                    errorMessage = "Pindai Makanan Gagal!"
                    Log.d("FoodViewModel", "Image tidak lolos validasi makanan")
                }
            } catch (e: IOException) {
                Log.e("FoodViewModel", "Error IO: ${e.message}", e)
                errorMessage = "Gagal memuat gambar atau model: ${e.message}"
            } catch (e: OutOfMemoryError) {
                Log.e("FoodViewModel", "Out of memory: ${e.message}", e)
                errorMessage = "Gambar terlalu besar. Gunakan gambar dengan resolusi lebih rendah."
            } catch (e: Exception) {
                Log.e("FoodViewModel", "Error umum: ${e.message}", e)
                errorMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // Metode yang lebih canggih untuk memeriksa apakah gambar adalah makanan
    private fun isFoodImageAdvanced(result: FoodDetectionResult): Boolean {
        // Confidence untuk kategori utama harus di atas threshold
        if (result.confidence < MAIN_CATEGORY_THRESHOLD) {
            Log.d("FoodViewModel", "Main category confidence too low: ${result.confidence} < $MAIN_CATEGORY_THRESHOLD")
            return false
        }

        // Dapatkan nilai confidence untuk semua kategori dan urutkan dari tertinggi ke terendah
        val sortedConfidences = result.allCategories.values.sortedDescending()

        // Jika hanya ada satu kategori, gunakan threshold normal
        if (sortedConfidences.size <= 1) {
            return result.confidence >= MAIN_CATEGORY_THRESHOLD
        }

        // Dapatkan confidence untuk kategori utama dan kategori kedua tertinggi
        val topConfidence = sortedConfidences[0]
        val secondConfidence = sortedConfidences[1]

        // Hitung rasio antara kategori utama dan kategori kedua
        // Ini membantu menentukan apakah model benar-benar yakin tentang kategori utama
        // dibandingkan dengan kategori lainnya
        val confidenceRatio = if (secondConfidence > 0) topConfidence / secondConfidence else Float.MAX_VALUE

        // Log untuk debugging
        Log.d("FoodViewModel", "Top confidence: $topConfidence, Second confidence: $secondConfidence, Ratio: $confidenceRatio")

        // Kategori utama harus memiliki confidence yang cukup tinggi
        // dan rasio confidence harus di atas threshold
        val isFood = topConfidence >= MAIN_CATEGORY_THRESHOLD && confidenceRatio >= CONFIDENCE_RATIO_THRESHOLD

        Log.d("FoodViewModel", "Is food: $isFood")
        return isFood
    }

    private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap {
        val contentResolver = context.contentResolver
        return contentResolver.openInputStream(uri).use { inputStream ->
            android.graphics.BitmapFactory.decodeStream(inputStream)
        } ?: throw IOException("Gagal membuka gambar")
    }

    // Fungsi untuk menyimpan hasil deteksi (kode tidak berubah)
    fun saveDetectionResult(username: String) {
        // ... kode yang sama seperti sebelumnya
    }

    // Fungsi untuk mereset state
    fun resetState() {
        isLoading = false
        isSaving = false
        detectionResult = null
        errorMessage = null
        saveSuccess = null
    }
}

// kode benar
//package com.example.caloryapp.viewmodel
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.net.Uri
//import android.util.Log
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.caloryapp.foodmodel.FoodDetectionResult
//import com.example.caloryapp.foodmodel.FoodDetector
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.io.IOException
//
//class FoodDetectionViewModel : ViewModel() {
//    var detectionResult by mutableStateOf<FoodDetectionResult?>(null)
//        private set
//
//    var isLoading by mutableStateOf(false)
//        private set
//
//    var errorMessage by mutableStateOf<String?>(null)
//        private set
//
//    fun detectFoodFromImage(context: Context, uri: Uri) {
//        isLoading = true
//        errorMessage = null
//
//        viewModelScope.launch {
//            try {
//                val bitmap = loadBitmapFromUri(context, uri)
//
//                // Buat detector di sini, tidak dalam withContext
//                val detector = FoodDetector(context)
//
//                val result = withContext(Dispatchers.IO) {
//                    detector.detectFood(bitmap)
//                }
//                detectionResult = result
//            } catch (e: IOException) {
//                Log.e("FoodViewModel", "Error IO: ${e.message}", e)
//                errorMessage = "Gagal memuat gambar atau model: ${e.message}"
//            } catch (e: Exception) {
//                Log.e("FoodViewModel", "Error umum: ${e.message}", e)
//                errorMessage = "Terjadi kesalahan: ${e.message}"
//            } finally {
//                isLoading = false
//            }
//        }
//    }
//
//    private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap {
//        val contentResolver = context.contentResolver
//        return contentResolver.openInputStream(uri).use { inputStream ->
//            android.graphics.BitmapFactory.decodeStream(inputStream)
//        } ?: throw IOException("Gagal membuka gambar")
//    }
//}

//    // Fungsi untuk menyimpan data kalori
//    fun saveCaloryData(calories: Int, onComplete: (Boolean) -> Unit) {
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        if (currentUser == null) {
//            Log.e("CaloryApp", "Pengguna belum login")
//            onComplete(false)
//            return
//        }
//
//        val username = currentUser.displayName ?: "Unknown"
//        val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
//
//        val caloryData = CaloryModel(calories, currentDate, username)
//
//        viewModelScope.launch {
//            try {
//                db.collection("users")
//                    .whereEqualTo("username", username)
//                    .get()
//                    .addOnSuccessListener { result ->
//                        if (!result.isEmpty) {
//                            val userDocument = result.documents[0]
//                            val calorieId = db.collection("users")
//                                .document(userDocument.id)
//                                .collection("calorieData")
//                                .document().id
//
//                            userDocument.reference.collection("calorieData")
//                                .document(calorieId)
//                                .set(caloryData)
//                                .addOnSuccessListener {
//                                    Log.d("CaloryApp", "Data kalori berhasil disimpan: $caloryData")
//                                    onComplete(true)
//                                }
//                                .addOnFailureListener { e ->
//                                    Log.e("CaloryApp", "Gagal menyimpan data: ${e.message}")
//                                    onComplete(false)
//                                }
//                        } else {
//                            Log.e("CaloryApp", "Pengguna tidak ditemukan: $username")
//                            onComplete(false)
//                        }
//                    }
//                    .addOnFailureListener { e ->
//                        Log.e("CaloryApp", "Gagal mencari pengguna: ${e.message}")
//                        onComplete(false)
//                    }
//            } catch (e: Exception) {
//                Log.e("CaloryApp", "Error: ${e.message}")
//                onComplete(false)
//            }
//        }
//    }
//}