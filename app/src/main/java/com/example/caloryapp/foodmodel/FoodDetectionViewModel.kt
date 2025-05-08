package com.example.caloryapp.foodmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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