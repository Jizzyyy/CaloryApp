package com.example.caloryapp.foodmodel

enum class FoodCategory(
    val displayName: String,
    val caloriesPerPorsi: Int,
    val colorHex: String,
    val icon: String
) {
    CARBS("Karbohidrat", 85, "#FECD45", "🍚"), // 1/3 piring
    PROTEIN("Protein", 25, "#FC8369", "🍗"), // 1/6 piring
    VEGETABLES("Sayuran", 25, "#4AB54A", "🥦"), // 1/3 piring
    FRUITS("Buah", 25, "#FF6B6B", "🍎"), // 1/6 piring
//    OTHER("Lainnya", 200, "#A0A0A0", "🍬")          // Tetap 200 kal/100g
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
        (weightGrams * category.caloriesPerPorsi / 100).toInt()
    }
}