package com.example.caloryapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("CaloryAppSession", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"
        private const val KEY_FULL_NAME = "fullName"
        private const val KEY_EMAIL = "email"
        private const val KEY_GENDER = "gender"
        private const val KEY_WEIGHT = "weight"
        private const val KEY_HEIGHT = "height"
    }

    // Menyimpan sesi login dengan penanganan null
    fun saveLoginSession(user: com.example.caloryapp.model.UserModel) {
        try {
            editor.putBoolean(KEY_IS_LOGGED_IN, true)
            editor.putString(KEY_USERNAME, user.username ?: "")
            editor.putString(KEY_FULL_NAME, user.fullName ?: "")
            editor.putString(KEY_EMAIL, user.email ?: "")
            editor.putString(KEY_GENDER, user.gender ?: "")
            editor.putString(KEY_WEIGHT, user.weight ?: "")
            editor.putString(KEY_HEIGHT, user.height ?: "")
            editor.apply()
            Log.d("SessionManager", "Session saved successfully")
        } catch (e: Exception) {
            Log.e("SessionManager", "Error saving session: ${e.message}")
        }
    }

    // Mengecek apakah user sudah login
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Mendapatkan data user yang tersimpan
    fun getUserData(): com.example.caloryapp.model.UserModel {
        return com.example.caloryapp.model.UserModel(
            username = sharedPreferences.getString(KEY_USERNAME, "") ?: "",
            fullName = sharedPreferences.getString(KEY_FULL_NAME, "") ?: "",
            email = sharedPreferences.getString(KEY_EMAIL, "") ?: "",
            password = "", // Password tidak disimpan di SharedPreferences untuk keamanan
            gender = sharedPreferences.getString(KEY_GENDER, "") ?: "",
            weight = sharedPreferences.getString(KEY_WEIGHT, "") ?: "",
            height = sharedPreferences.getString(KEY_HEIGHT, "") ?: ""
        )
    }

    // Menghapus sesi saat logout dengan try-catch
    fun clearSession() {
        try {
            editor.clear()
            editor.apply()
            Log.d("SessionManager", "Session cleared successfully")
        } catch (e: Exception) {
            Log.e("SessionManager", "Error clearing session: ${e.message}")
        }
    }
}