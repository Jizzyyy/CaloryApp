package com.example.caloryapp.pages.calorydetail

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun CameraPreviewWithOverlay(
    foodClassifier: FoodClassifier,
    areaCoverage: Map<String, Int>
) {
    var classificationResult by remember { mutableStateOf("Memuat...") }
    Box {
//        CameraPreview(onImageCaptured = { byteBuffer ->
//            val result = foodClassifier.classify(byteBuffer)
//            classificationResult = result
//        })
//
//        // Menampilkan hasil klasifikasi dan validasi porsi makanan
//        ValidateFoodPlate(
//            classificationResult = classificationResult,
//            areaCoverage = areaCoverage
//        )
    }
}

@Composable
fun ValidateFoodPlate(
    classificationResult: String,
    areaCoverage: Map<String, Int>
) {
    val context = LocalContext.current
    val idealPercentage = mapOf(
        "Karbohidrat" to 25,
        "Protein" to 25,
        "Sayur" to 30,
        "Buah" to 15,
        "Lainnya" to 5
    )

    var isValid = true
    for ((category, ideal) in idealPercentage) {
        val actual = areaCoverage[category] ?: 0
        if (actual < ideal * 0.8 || actual > ideal * 1.2) {
            isValid = false
            break
        }
    }

    if (!isValid) {
        Toast.makeText(context, "Porsi makanan TIDAK sesuai!", Toast.LENGTH_LONG).show()
    } else {
        Toast.makeText(context, "Porsi makanan sesuai kebutuhan kalori harian!", Toast.LENGTH_LONG).show()
    }
}
