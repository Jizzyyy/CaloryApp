package com.example.caloryapp.repository

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.caloryapp.foodmodel.FoodCategory
import com.example.caloryapp.foodmodel.FoodDetectionResult
import com.example.caloryapp.model.CaloryHistoryModel
import com.example.caloryapp.model.CaloryModel
import com.example.caloryapp.utils.LocalImageStorage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


class CaloryRepository {
    private val TAG = "CaloryRepository"
    private val db = FirebaseFirestore.getInstance()

    // Mendapatkan data kalori dari database
    fun getCalorieData(username: String, onComplete: (List<CaloryModel>) -> Unit) {
        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val userDocument = result.documents[0]
                    userDocument.reference.collection("calorieData")
                        .orderBy("date", Query.Direction.DESCENDING)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            val calorieList = mutableListOf<CaloryModel>()
                            for (document in snapshot.documents) {
                                val calories = document.getLong("calories")?.toInt() ?: 0
                                val date = document.getString("date") ?: "Unknown"
                                val imagePath = document.getString("imagePath") ?: ""

                                val caloryData = CaloryModel(
                                    calories = calories,
                                    date = date,
                                    username = username,
                                    imagePath = imagePath
                                )
                                calorieList.add(caloryData)
                                Log.d(TAG, "Kalori: $calories, Tanggal: $date, Image: $imagePath")
                            }
                            onComplete(calorieList)
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Gagal mengambil data kalori: ${e.message}")
                            onComplete(emptyList())
                        }
                } else {
                    Log.e(TAG, "Pengguna tidak ditemukan: $username")
                    onComplete(emptyList())
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Gagal mencari pengguna: ${e.message}")
                onComplete(emptyList())
            }
    }

    // Menyimpan data kalori dengan gambar
    fun saveCaloryData(
        context: Context,
        bitmap: Bitmap,
        calories: Int,
        username: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        // 1. Simpan gambar ke penyimpanan lokal
        val imagePath = LocalImageStorage.saveImageToInternalStorage(context, bitmap)

        if (imagePath.isEmpty()) {
            onComplete(false, "Gagal menyimpan gambar")
            return
        }

        // 2. Format tanggal saat ini (hanya tanggal, tanpa waktu)
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())

        // 3. Siapkan data kalori untuk disimpan
        val caloryData = hashMapOf(
            "calories" to calories,
            "date" to currentDate,
            "username" to username,
            "imagePath" to imagePath
        )

        // 4. Simpan ke Firestore sesuai struktur database Anda
        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val userDocument = result.documents[0]
                    val calorieId = db.collection("users")
                        .document(userDocument.id)
                        .collection("calorieData")
                        .document().id

                    userDocument.reference.collection("calorieData")
                        .document(calorieId)
                        .set(caloryData)
                        .addOnSuccessListener {
                            Log.d(TAG, "Data kalori berhasil disimpan dengan ID: $calorieId")
                            onComplete(true, calorieId)
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Gagal menyimpan data: ${e.message}")
                            // Jika gagal menyimpan ke Firestore, hapus gambar lokal
                            LocalImageStorage.deleteImageFromInternalStorage(imagePath)
                            onComplete(false, e.message)
                        }
                } else {
                    Log.e(TAG, "Pengguna tidak ditemukan: $username")
                    // Hapus gambar jika user tidak ditemukan
                    LocalImageStorage.deleteImageFromInternalStorage(imagePath)
                    onComplete(false, "Pengguna tidak ditemukan")
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Gagal mencari pengguna: ${e.message}")
                // Hapus gambar jika terjadi error
                LocalImageStorage.deleteImageFromInternalStorage(imagePath)
                onComplete(false, e.message)
            }
    }

    // Menghapus data kalori berdasarkan tanggal dan kalori
    // Karena tidak ada ID dalam model, kita gunakan kombinasi tanggal+kalori untuk identifikasi
    fun deleteCaloryData(date: String, calories: Int, imagePath: String, username: String, onComplete: (Boolean) -> Unit) {
        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val userDocument = result.documents[0]

                    // Cari dokumen dengan tanggal dan kalori yang sesuai
                    userDocument.reference.collection("calorieData")
                        .whereEqualTo("date", date)
                        .whereEqualTo("calories", calories)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            if (snapshot.documents.isNotEmpty()) {
                                // Ambil dokumen pertama yang cocok
                                val document = snapshot.documents[0]

                                // Hapus dokumen tersebut
                                document.reference.delete()
                                    .addOnSuccessListener {
                                        Log.d(TAG, "Data kalori berhasil dihapus: $date, $calories")
                                        // Hapus gambar dari penyimpanan lokal
                                        val isDeleted = LocalImageStorage.deleteImageFromInternalStorage(imagePath)
                                        Log.d(TAG, "Gambar berhasil dihapus: $isDeleted")
                                        onComplete(true)
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e(TAG, "Gagal menghapus data: ${e.message}")
                                        onComplete(false)
                                    }
                            } else {
                                Log.e(TAG, "Data kalori tidak ditemukan: $date, $calories")
                                onComplete(false)
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Gagal mencari data kalori: ${e.message}")
                            onComplete(false)
                        }
                } else {
                    Log.e(TAG, "Pengguna tidak ditemukan: $username")
                    onComplete(false)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Gagal mencari pengguna: ${e.message}")
                onComplete(false)
            }
    }

    // Membersihkan gambar yang tidak digunakan
    fun cleanupUnusedImages(context: Context, username: String) {
        // Dapatkan semua gambar yang digunakan
        getCalorieData(username) { allCalories ->
            val usedImagePaths = allCalories.map { it.imagePath }.filter { it.isNotEmpty() }
            // Bersihkan gambar yang tidak digunakan
            LocalImageStorage.cleanupUnusedImages(context, usedImagePaths)
        }
    }

//    fun saveCalorieData(username: String, calories: Int, onComplete: (Boolean) -> Unit) {
//        val currentDate =
//            java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
//                .format(java.util.Date())
//        val calorieData = hashMapOf(
//            "calories" to calories,
//            "date" to currentDate
//        )
//        val caloryData = CaloryModel(calories, currentDate, username)
//        db.collection("users")
//            .whereEqualTo("username", username)
//            .get()
//            .addOnSuccessListener { result ->
//                if (!result.isEmpty) {
//                    val userDocument = result.documents[0]
//                    val calorieId = db.collection("users")
//                        .document(userDocument.id)
//                        .collection("calorieData")
//                        .document().id
//
//                    userDocument.reference.collection("calorieData")
//                        .document(calorieId)
//                        .set(caloryData)
//                        .addOnSuccessListener {
//                            Log.d("CaloryApp", "Data kalori berhasil disimpan: $caloryData")
//                            onComplete(true)
//                        }
//                        .addOnFailureListener { e ->
//                            Log.e("CaloryApp", "Gagal menyimpan data: ${e.message}")
//                            onComplete(false)
//                        }
//                } else {
//                    Log.e("CaloryApp", "Pengguna tidak ditemukan: $username")
//                    onComplete(false)
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.e("CaloryApp", "Gagal mencari pengguna: ${e.message}")
//                onComplete(false)
//            }
//    }


    //    private val db = FirebaseFirestore.getInstance()
//    private val TAG = "CaloryRepository"

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
                    (weightGrams * it.caloriesPerPorsi / 100).toInt()
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