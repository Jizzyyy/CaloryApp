package com.example.caloryapp.pages.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

// Model ViewModel
class FoodCalorieViewModel2 : ViewModel() {
    // Kategori makanan dan informasi kalori
    private val categoryNames = listOf("Karbohidrat", "Protein", "Sayur", "Buah", "Lainnya")
    private val caloriesPerCategory = mapOf(
        0 to 140,  // Karbohidrat (nilai rata-rata 130-150)
        1 to 225,  // Protein (nilai rata-rata 200-250)
        2 to 35,   // Sayur (nilai rata-rata 25-50)
        3 to 65,   // Buah (nilai rata-rata 50-80)
        4 to 350   // Lainnya (nilai rata-rata 300-400)
    )

    // State untuk UI
    private val _selectedImageUri = mutableStateOf<Uri?>(null)
    val selectedImageUri: State<Uri?> = _selectedImageUri

    private val _predictionResult = mutableStateOf<PredictionResult?>(null)
    val predictionResult: State<PredictionResult?> = _predictionResult

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // Fungsi untuk memuat model TFLite
    private fun loadModelFile(context: Context): MappedByteBuffer {
        return try {
            val assetManager = context.assets
            val modelPath = MODEL_PATH
            val fileDescriptor = assetManager.openFd(modelPath)

            FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
                val fileChannel = inputStream.channel
                val startOffset = fileDescriptor.startOffset
                val declaredLength = fileDescriptor.declaredLength
                fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Error loading model: " + e.message)
        }
    }
    // Fungsi untuk memproses gambar
    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true)
        val byteBuffer = ByteBuffer.allocateDirect(4 * 128 * 128 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(128 * 128)
        resizedBitmap.getPixels(pixels, 0, 128, 0, 0, 128, 128)

        for (pixel in pixels) {
            // Normalisasi nilai RGB (0-255) ke (0-1)
            byteBuffer.putFloat(((pixel shr 16) and 0xFF) / 255.0f)  // R
            byteBuffer.putFloat(((pixel shr 8) and 0xFF) / 255.0f)   // G
            byteBuffer.putFloat((pixel and 0xFF) / 255.0f)           // B
        }

        return byteBuffer
    }

    // Fungsi untuk melakukan prediksi
    fun predictImage(context: Context, uri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedImageUri.value = uri

            try {
                withContext(Dispatchers.IO) {
                    // Load gambar
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    // Preprocess gambar
                    val modelInput = preprocessImage(bitmap)

                    // Load model TFLite
                    val modelBuffer = loadModelFile(context)
                    val interpreter = Interpreter(modelBuffer)

                    // Persiapkan output
                    val outputBuffer = Array(1) { FloatArray(5) }

                    // Jalankan inferensi
                    interpreter.run(modelInput, outputBuffer)

                    // Interpretasi hasil
                    val outputArray = outputBuffer[0]
                    val maxIndex = outputArray.indices.maxByOrNull { outputArray[it] } ?: 0
                    val confidence = outputArray[maxIndex]

                    // Dapatkan kategori dan kalori
                    val category = categoryNames[maxIndex]
                    val calories = caloriesPerCategory[maxIndex] ?: 0

                    // Perbarui UI
                    withContext(Dispatchers.Main) {
                        _predictionResult.value = PredictionResult(
                            category = category,
                            calories = calories,
                            confidence = confidence
                        )
                    }

                    // Tutup interpreter
                    interpreter.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    companion object {
        private const val MODEL_PATH = "model_food_plate_densenet.tflite"
//        "model_food_calories.tflite"
    }

    // Kelas untuk hasil prediksi
    data class PredictionResult(
        val category: String,
        val calories: Int,
        val confidence: Float
    )
}


// Layar utama
@Composable
fun FoodCalorieScreen(viewModel: FoodCalorieViewModel2) {
    val context = LocalContext.current
    val selectedImageUri by viewModel.selectedImageUri
    val predictionResult by viewModel.predictionResult
    val isLoading by viewModel.isLoading

    // Create an ActivityResultLauncher for picking images from gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Handle the returned Uri
        uri?.let {
            viewModel.predictImage(context, it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Food Calorie Detector",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Tampilkan gambar yang dipilih
        selectedImageUri?.let { uri ->
            val bitmap = remember(uri) {
                val inputStream = context.contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(inputStream)
            }

            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Selected Food Image",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 16.dp)
                )
            }
        }

        // Tombol pilih gambar (updated)
        Button(
            onClick = {
                // Launch gallery to pick an image
                galleryLauncher.launch("image/*")
            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Select Food Image")
        }

        // Tampilkan hasil prediksi
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            predictionResult?.let { result ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Detected Food: ${result.category}",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Estimated Calories: ${result.calories} kcal (per 100g)",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Confidence: ${(result.confidence * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Daily value: ${calculateDailyValue(result.category, result.calories)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

// Fungsi untuk menghitung persentase dari kebutuhan harian
fun calculateDailyValue(category: String, calories: Int): String {
    // Asumsi kebutuhan harian rata-rata adalah 2000 kalori
    val dailyCalorieNeed = 2000

    // Asumsi porsi standar (dalam gram)
    val standardPortion = when (category) {
        "Karbohidrat" -> 150 // Misalnya 150g nasi
        "Protein" -> 100     // Misalnya 100g daging/ikan
        "Sayur" -> 200       // Misalnya 200g sayuran
        "Buah" -> 150        // Misalnya 150g buah
        "Lainnya" -> 50      // Misalnya 50g camilan
        else -> 100
    }

    // Kalori per porsi standar
    val caloriesPerPortion = calories * standardPortion / 100

    // Persentase dari kebutuhan harian
    val percentage = (caloriesPerPortion * 100 / dailyCalorieNeed.toFloat()).toInt()

    return "A standard portion ($standardPortion g) provides $caloriesPerPortion kcal, which is $percentage% of daily needs"
}