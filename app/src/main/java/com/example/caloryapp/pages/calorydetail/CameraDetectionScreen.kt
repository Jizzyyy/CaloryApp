package com.example.caloryapp.pages.calorydetail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun CameraDetectionScreen(foodClassifier: FoodClassifier, areaCoverage: Map<String, Int>,modifier: Modifier = Modifier, navController: NavController) {

}


//@Composable
//fun ValidateFoodPlate(foodClassifier: FoodClassifier, areaCoverage: Map<String, Int>) {
//    val context = LocalContext.current
//
//    val isPortionValid = foodClassifier.validatePortion(areaCoverage)
//    if (!isPortionValid) {
//        Toast.makeText(context, "Porsi makanan TIDAK sesuai! Kebutuhan kalori GAGAL terpenuhi!", Toast.LENGTH_LONG).show()
//    } else {
//        Toast.makeText(context, "Porsi makanan sesuai! Kebutuhan kalori TERPENUHI!", Toast.LENGTH_LONG).show()
//    }
//}

@Composable
fun FoodPlateOverlay(isValid: Boolean) {
    val color = if (isValid) Color.Green else Color.Red

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Visualisasi Food Plate dengan warna berdasarkan validasi porsi
        drawRect(color, Offset(0f, 0f), Size(width * 0.6f, height * 0.25f))
        drawRect(Color.Yellow, Offset(0f, height * 0.25f), Size(width * 0.5f, height * 0.5f)) // Karbohidrat
        drawRect(Color.Red, Offset(width * 0.5f, height * 0.25f), Size(width * 0.5f, height * 0.5f)) // Protein
        drawRect(Color.Green, Offset(0f, 0f), Size(width * 0.6f, height * 0.25f)) // Sayur
        drawRect(Color(0xFFFFA500), Offset(width * 0.6f, 0f), Size(width * 0.4f, height * 0.25f)) // Buah
        drawRect(Color.Blue, Offset(width * 0.75f, height * 0.75f), Size(width * 0.25f, height * 0.25f)) // Lainnya
    }
}