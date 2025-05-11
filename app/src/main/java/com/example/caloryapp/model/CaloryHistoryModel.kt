package com.example.caloryapp.model

import com.example.caloryapp.foodmodel.FoodCategory
import com.google.firebase.Timestamp
import java.util.Date

/**
 * Model untuk menyimpan riwayat kalori makanan
 */
data class CaloryHistoryModel(
    val id: String = "", // ID dokumen
    val username: String = "", // Username pengguna
    val timestamp: Timestamp = Timestamp.now(), // Waktu pencatatan
    val totalCalories: Int = 0, // Total kalori
    // Daftar persentase kategori makanan yang terdeteksi
    val foodComposition: Map<String, Float> = mapOf(),
    // Daftar kalori per kategori yang dihitung
    val caloriesPerCategory: Map<String, Int> = mapOf()
) {
    // Constructor kosong untuk Firestore
    constructor() : this("", "", Timestamp.now(), 0, mapOf(), mapOf())

    // Fungsi untuk mengkonversi timestamp ke Date
    fun getDate(): Date {
        return timestamp.toDate()
    }

    // Fungsi untuk mendapatkan tanggal dalam format string
    fun getFormattedDate(): String {
        val date = timestamp.toDate()
        val day = date.date
        val month = date.month + 1 // Months are 0-based
        val year = date.year + 1900 // Years start from 1900

        return "$day/$month/$year"
    }
}