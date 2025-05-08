package com.example.caloryapp.repository

import android.util.Log
import com.example.caloryapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseFirestore.getInstance()

    fun registerUser(user: UserModel, onComplete: (Boolean) -> Unit) {
        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }


    fun registerUserWithCustomID(user: UserModel, userID: String, onComplete: (Boolean) -> Unit) {
        db.collection("users")
            .document(userID)
            .set(user)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun getUserByUsername(username: String, onComplete: (UserModel?) -> Unit) {
        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val user = result.documents[0].toObject(UserModel::class.java)
                    onComplete(user)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
    }

    fun updatePasswordByUsername(username: String, oldPassword: String, newPassword: String, onComplete: (Boolean) -> Unit) {
        db.collection("users")
            .whereEqualTo("username", username)  // Mencari pengguna berdasarkan username
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val userDocument = result.documents[0]
                    val currentPassword = userDocument.getString("password")

                    if (currentPassword == oldPassword) {
                        userDocument.reference.update("password", newPassword)
                            .addOnSuccessListener {
                                onComplete(true)  // Update berhasil
                            }
                            .addOnFailureListener {
                                onComplete(false)  // Update gagal
                            }
                    } else {
                        // Jika password lama tidak cocok
                        onComplete(false)  // Password lama salah
                    }
                } else {
                    // Username tidak ditemukan
                    onComplete(false)
                }
            }
            .addOnFailureListener {
                onComplete(false)  // Terjadi error dalam pencarian data
            }
    }

    fun updateUserData(username: String, fullName: String, email: String, gender: String, weight: String, height: String, onComplete: (Boolean) -> Unit) {
        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val userDocument = result.documents[0]
                    // Memperbarui data pengguna
                    userDocument.reference.update(
                        "fullName", fullName,
                        "email", email,
                        "gender", gender,
                        "weight", weight,
                        "height", height
                    )
                        .addOnSuccessListener {
                            onComplete(true) // Update berhasil
                        }
                        .addOnFailureListener {
                            onComplete(false) // Update gagal
                        }
                } else {
                    onComplete(false) // Username tidak ditemukan
                }
            }
            .addOnFailureListener {
                onComplete(false) // Terjadi error dalam pencarian data
            }
    }


    fun updatePasswordByUsername2(username: String, newPassword: String, confirmPassword: String, onComplete: (Boolean) -> Unit) {
        // Memeriksa apakah kata sandi baru dan konfirmasi cocok
        if (newPassword != confirmPassword) {
            onComplete(false) // Kata sandi tidak cocok
            return
        }

        // Mencari pengguna berdasarkan username
        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val userDocument = result.documents[0]
                    // Memperbarui kata sandi pengguna
                    userDocument.reference.update("password", newPassword)
                        .addOnSuccessListener {
                            onComplete(true) // Update berhasil
                        }
                        .addOnFailureListener {
                            onComplete(false) // Update gagal
                        }
                } else {
                    onComplete(false) // Username tidak ditemukan
                }
            }
            .addOnFailureListener {
                onComplete(false) // Terjadi error dalam pencarian data
            }
    }

//    private val db = FirebaseFirestore.getInstance()
//    private val auth = FirebaseAuth.getInstance()
//
//    suspend fun registerUser(email: String, password: String, userModel: UserModel): Boolean {
//        val userId = auth.currentUser?.uid ?: return false
//        return try {
//            db.collection("users").document(userId).set(userModel).await()
//            true
//        } catch (e: Exception) {
//            Log.e("Firestore", "Error saving user: ", e)
//            false
//        }
//    }
//
//    suspend fun getUser(username: String): UserModel? {
//        return try {
//            val document = db.collection("users").document(username).get().await()
//            document.toObject(UserModel::class.java)
//        } catch (e: Exception) {
//            null
//        }
//    }
}