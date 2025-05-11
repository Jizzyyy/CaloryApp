package com.example.caloryapp.repository

import android.util.Log
import com.example.caloryapp.foodmodel.FoodCategory
import com.example.caloryapp.foodmodel.FoodDetectionResult
import com.example.caloryapp.model.CaloryHistoryModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.UUID

class CaloryRepository {
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "CaloryRepository"

    /**
     * Menyimpan hasil deteksi makanan ke Firestore (tanpa gambar)
     * @param username Username pengguna
     * @param result Hasil deteksi makanan
     * @param totalCalories Total kalori yang dihitung
     * @param onComplete Callback setelah selesai
     */
    fun saveCaloryHistory(
        username: String,
        result: FoodDetectionResult,
        totalCalories: Int,
        onComplete: (Boolean, String?) -> Unit
    ) {
        // Konversi map FoodCategory ke String untuk Firestore
        val foodComposition = result.allCategories.mapKeys { it.key.displayName }

        // Hitung kalori per kategori
        val caloriesPerCategory = result.allCategories.mapKeys { it.key.displayName }
            .mapValues { (category, percentage) ->
                // Asumsi berat porsi 500 gram
                val weightGrams = 500 * percentage
                val categoryEnum = FoodCategory.values().find { it.displayName == category }
                val calories = categoryEnum?.let {
                    (weightGrams * it.caloriesPer100g / 100).toInt()
                } ?: 0
                calories
            }

        // Buat objek history
        val docId = UUID.randomUUID().toString()
        val caloryHistory = CaloryHistoryModel(
            id = docId,
            username = username,
            timestamp = Timestamp.now(),
            totalCalories = totalCalories,
            foodComposition = foodComposition,
            caloriesPerCategory = caloriesPerCategory
        )

        // Simpan ke Firestore
        db.collection("calory_history")
            .document(docId)
            .set(caloryHistory)
            .addOnSuccessListener {
                Log.d(TAG, "Calory history saved successfully")
                onComplete(true, docId)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error saving calory history", e)
                onComplete(false, null)
            }
    }

    /**
     * Mengambil riwayat kalori berdasarkan username
     */
    fun getCaloryHistoryByUsername(
        username: String,
        limit: Long = 10,
        onComplete: (List<CaloryHistoryModel>) -> Unit
    ) {
        db.collection("calory_history")
            .whereEqualTo("username", username)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .addOnSuccessListener { documents ->
                val historyList = documents.mapNotNull { doc ->
                    doc.toObject(CaloryHistoryModel::class.java)
                }
                onComplete(historyList)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error getting calory history", e)
                onComplete(emptyList())
            }
    }

    /**
     * Mendapatkan detail riwayat kalori berdasarkan ID
     */
    fun getCaloryHistoryById(id: String, onComplete: (CaloryHistoryModel?) -> Unit) {
        db.collection("calory_history")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val history = document.toObject(CaloryHistoryModel::class.java)
                    onComplete(history)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error getting calory history detail", e)
                onComplete(null)
            }
    }

    /**
     * Menghapus riwayat kalori berdasarkan ID
     */
    fun deleteCaloryHistory(id: String, onComplete: (Boolean) -> Unit) {
        db.collection("calory_history")
            .document(id)
            .delete()
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }
}