package com.example.caloryapp.foodmodel

enum class FoodCategory(
    val displayName: String,
    val caloriesPer100g: Int,
    val colorHex: String,
    val icon: String
) {
    CARBS("Karbohidrat", 150, "#FECD45", "🍚"),      // Tetap 150 kal/100g
    PROTEIN("Protein", 250, "#FC8369", "🍗"),        // Ubah ke 250 kal/100g
    VEGETABLES("Sayuran", 30, "#4AB54A", "🥦"),      // Tetap 30 kal/100g
    FRUITS("Buah", 60, "#FF6B6B", "🍎"),            // Tetap 60 kal/100g
    OTHER("Lainnya", 200, "#A0A0A0", "🍬")          // Tetap 200 kal/100g
}

data class FoodDetectionResult(
    val mainCategory: FoodCategory,
    val confidence: Float,
    val allCategories: Map<FoodCategory, Float>
)

// Extension function untuk menghitung total kalori berdasarkan persentase makanan
fun Map<FoodCategory, Float>.calculateTotalCalories(plateWeightGrams: Int = 500): Int {
    return this.entries.sumOf { (category, percentage) ->
        val weightGrams = plateWeightGrams * percentage
        (weightGrams * category.caloriesPer100g / 100).toInt()
    }
}