package com.example.caloryapp.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caloryapp.model.CaloryModel
import com.example.caloryapp.repository.CaloryRepository
import com.example.caloryapp.utils.LocalImageStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CaloryHistoryViewModel : ViewModel() {
    private val TAG = "CaloryHistoryViewModel"
    private val repository = CaloryRepository()

    private val _calorieList = mutableStateOf<List<CaloryModel>>(emptyList())
    val calorieList: State<List<CaloryModel>> = _calorieList

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    // Menyimpan data kalori baru
    fun saveCaloryData(
        context: Context,
        bitmap: Bitmap,
        calories: Int,
        username: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.saveCaloryData(
                        context,
                        bitmap,
                        calories,
                        username
                    ) { success, message ->
                        _isLoading.value = false
                        if (!success) {
                            _error.value = message ?: "Gagal menyimpan data"
                        } else {
                            // Refresh list setelah berhasil menambahkan
                            loadHistoryByUsername(username)
                        }
                        onComplete(success, message)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error saat menyimpan data: ${e.message}")
                _isLoading.value = false
                _error.value = e.message
                onComplete(false, e.message)
            }
        }
    }

    // Memuat data kalori untuk user
    fun loadHistoryByUsername(username: String, limit: Int = 0) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.getCalorieData(username) { data ->
                        _calorieList.value = if (limit > 0 && data.size > limit) {
                            data.take(limit)
                        } else {
                            data
                        }
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error saat memuat data: ${e.message}")
                _isLoading.value = false
                _error.value = e.message
            }
        }
    }

    // Hapus data kalori
    fun deleteCaloryData(date: String, calories: Int, imagePath: String, username: String, onComplete: (Boolean) -> Unit) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.deleteCaloryData(date, calories, imagePath, username) { success ->
                        _isLoading.value = false
                        if (success) {
                            // Update list setelah penghapusan
                            _calorieList.value = _calorieList.value.filter {
                                it.date != date || it.calories != calories
                            }
                        }
                        onComplete(success)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error saat menghapus data: ${e.message}")
                _isLoading.value = false
                onComplete(false)
            }
        }
    }

    // Mendapatkan bitmap dari path gambar
    fun getImageBitmap(context: Context, imagePath: String): Bitmap? {
        if (imagePath.isEmpty()) return null
        return LocalImageStorage.getImageFromInternalStorage(context, imagePath)
    }

    // Membersihkan cache gambar yang tidak digunakan
    fun cleanupUnusedImages(context: Context, username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.cleanupUnusedImages(context, username)
        }
    }
}

//package com.example.caloryapp.viewmodel
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.caloryapp.model.CaloryHistoryModel
//import com.example.caloryapp.repository.CaloryRepository
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
///**
// * ViewModel untuk mengelola riwayat kalori
// */
//class CaloryHistoryViewModel : ViewModel() {
//
//    private val caloryRepository = CaloryRepository()
//
//    // State untuk UI
//    var isLoading by mutableStateOf(false)
//    var errorMessage by mutableStateOf<String?>(null)
//
//    // List riwayat kalori
//    var historyList by mutableStateOf<List<CaloryHistoryModel>>(emptyList())
//
//    // Detail riwayat kalori yang dipilih
//    var selectedHistory by mutableStateOf<CaloryHistoryModel?>(null)
//
//    /**
//     * Memuat riwayat kalori berdasarkan username
//     */
//    fun loadHistoryByUsername(username: String, limit: Long = 10) {
//        isLoading = true
//        errorMessage = null
//
//        // Menggunakan viewModelScope, yang merupakan CoroutineScope yang otomatis dibatalkan saat ViewModel dihancurkan
//        viewModelScope.launch {
//            try {
//                // Membungkus fungsi callback-based dalam withContext untuk menjalankannya di thread IO
//                withContext(Dispatchers.IO) {
//                    caloryRepository.getCaloryHistoryByUsername(username, limit) { histories ->
//                        // Kembali ke thread utama untuk memperbarui UI
//                        viewModelScope.launch(Dispatchers.Main) {
//                            historyList = histories
//                            isLoading = false
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    errorMessage = "Gagal memuat riwayat kalori: ${e.message}"
//                    isLoading = false
//                }
//            }
//        }
//    }
//
//    /**
//     * Memuat detail riwayat kalori berdasarkan ID
//     */
//    fun loadHistoryDetail(id: String) {
//        isLoading = true
//        errorMessage = null
//
//        viewModelScope.launch {
//            try {
//                withContext(Dispatchers.IO) {
//                    caloryRepository.getCaloryHistoryById(id) { history ->
//                        viewModelScope.launch(Dispatchers.Main) {
//                            selectedHistory = history
//                            isLoading = false
//
//                            if (history == null) {
//                                errorMessage = "Riwayat kalori tidak ditemukan"
//                            }
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    errorMessage = "Gagal memuat detail riwayat: ${e.message}"
//                    isLoading = false
//                }
//            }
//        }
//    }
//
//    /**
//     * Menghapus riwayat kalori
//     */
//    fun deleteHistory(id: String, onComplete: (Boolean) -> Unit) {
//        isLoading = true
//
//        viewModelScope.launch {
//            try {
//                withContext(Dispatchers.IO) {
//                    caloryRepository.deleteCaloryHistory(id) { success ->
//                        viewModelScope.launch(Dispatchers.Main) {
//                            isLoading = false
//                            if (success) {
//                                // Hapus dari list lokal jika berhasil
//                                historyList = historyList.filter { it.id != id }
//                            }
//                            onComplete(success)
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    errorMessage = "Gagal menghapus riwayat: ${e.message}"
//                    isLoading = false
//                    onComplete(false)
//                }
//            }
//        }
//    }
//
//    /**
//     * Reset state
//     */
//    fun resetState() {
//        historyList = emptyList()
//        selectedHistory = null
//        errorMessage = null
//        isLoading = false
//    }
//}