package com.example.caloryapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caloryapp.model.CaloryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class CaloryViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // Fungsi untuk menyimpan data kalori
    fun saveCaloryData(calories: Int, onComplete: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Log.e("CaloryApp", "Pengguna belum login")
            onComplete(false)
            return
        }

        val username = currentUser.displayName ?: "Unknown"
        val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())

        val caloryData = CaloryModel(calories, currentDate, username)

        viewModelScope.launch {
            try {
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
                                    Log.d("CaloryApp", "Data kalori berhasil disimpan: $caloryData")
                                    onComplete(true)
                                }
                                .addOnFailureListener { e ->
                                    Log.e("CaloryApp", "Gagal menyimpan data: ${e.message}")
                                    onComplete(false)
                                }
                        } else {
                            Log.e("CaloryApp", "Pengguna tidak ditemukan: $username")
                            onComplete(false)
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("CaloryApp", "Gagal mencari pengguna: ${e.message}")
                        onComplete(false)
                    }
            } catch (e: Exception) {
                Log.e("CaloryApp", "Error: ${e.message}")
                onComplete(false)
            }
        }
    }
}
