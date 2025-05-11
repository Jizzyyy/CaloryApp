package com.example.caloryapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caloryapp.model.CaloryHistoryModel
import com.example.caloryapp.repository.CaloryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel untuk mengelola riwayat kalori
 */
class CaloryHistoryViewModel : ViewModel() {

    private val caloryRepository = CaloryRepository()

    // State untuk UI
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // List riwayat kalori
    var historyList by mutableStateOf<List<CaloryHistoryModel>>(emptyList())

    // Detail riwayat kalori yang dipilih
    var selectedHistory by mutableStateOf<CaloryHistoryModel?>(null)

    /**
     * Memuat riwayat kalori berdasarkan username
     */
    fun loadHistoryByUsername(username: String, limit: Long = 10) {
        isLoading = true
        errorMessage = null

        // Menggunakan viewModelScope, yang merupakan CoroutineScope yang otomatis dibatalkan saat ViewModel dihancurkan
        viewModelScope.launch {
            try {
                // Membungkus fungsi callback-based dalam withContext untuk menjalankannya di thread IO
                withContext(Dispatchers.IO) {
                    caloryRepository.getCaloryHistoryByUsername(username, limit) { histories ->
                        // Kembali ke thread utama untuk memperbarui UI
                        viewModelScope.launch(Dispatchers.Main) {
                            historyList = histories
                            isLoading = false
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage = "Gagal memuat riwayat kalori: ${e.message}"
                    isLoading = false
                }
            }
        }
    }

    /**
     * Memuat detail riwayat kalori berdasarkan ID
     */
    fun loadHistoryDetail(id: String) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    caloryRepository.getCaloryHistoryById(id) { history ->
                        viewModelScope.launch(Dispatchers.Main) {
                            selectedHistory = history
                            isLoading = false

                            if (history == null) {
                                errorMessage = "Riwayat kalori tidak ditemukan"
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage = "Gagal memuat detail riwayat: ${e.message}"
                    isLoading = false
                }
            }
        }
    }

    /**
     * Menghapus riwayat kalori
     */
    fun deleteHistory(id: String, onComplete: (Boolean) -> Unit) {
        isLoading = true

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    caloryRepository.deleteCaloryHistory(id) { success ->
                        viewModelScope.launch(Dispatchers.Main) {
                            isLoading = false
                            if (success) {
                                // Hapus dari list lokal jika berhasil
                                historyList = historyList.filter { it.id != id }
                            }
                            onComplete(success)
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage = "Gagal menghapus riwayat: ${e.message}"
                    isLoading = false
                    onComplete(false)
                }
            }
        }
    }

    /**
     * Reset state
     */
    fun resetState() {
        historyList = emptyList()
        selectedHistory = null
        errorMessage = null
        isLoading = false
    }
}