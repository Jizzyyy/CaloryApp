package com.example.caloryapp.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object LocalImageStorage {
    private const val TAG = "LocalImageStorage"
    private const val IMAGE_DIRECTORY = "calory_images"

    // Menyimpan gambar ke penyimpanan internal
    fun saveImageToInternalStorage(context: Context, bitmap: Bitmap, fileName: String? = null): String {
        // Buat nama file unik jika tidak disediakan
        val imageName = fileName ?: "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.jpg"

        // Buat direktori jika belum ada
        val directory = File(context.filesDir, IMAGE_DIRECTORY)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        // Path file lengkap
        val file = File(directory, imageName)

        try {
            // Simpan bitmap ke file
            FileOutputStream(file).use { stream ->
                // Kompres gambar dengan kualitas 85% untuk menghemat ruang
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                stream.flush()
            }
            Log.d(TAG, "Gambar berhasil disimpan: ${file.absolutePath}")
            return file.absolutePath
        } catch (e: IOException) {
            Log.e(TAG, "Gagal menyimpan gambar: ${e.message}")
            e.printStackTrace()
            return ""
        }
    }

    // Mengambil gambar dari penyimpanan internal
    fun getImageFromInternalStorage(context: Context, imagePath: String): Bitmap? {
        return try {
            val file = File(imagePath)
            if (file.exists()) {
                BitmapFactory.decodeFile(file.absolutePath)
            } else {
                Log.w(TAG, "File tidak ditemukan: $imagePath")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gagal mengambil gambar: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    // Hapus gambar dari penyimpanan internal
    fun deleteImageFromInternalStorage(imagePath: String): Boolean {
        return try {
            val file = File(imagePath)
            if (file.exists()) {
                val result = file.delete()
                Log.d(TAG, if (result) "Gambar berhasil dihapus: $imagePath" else "Gagal menghapus gambar: $imagePath")
                result
            } else {
                Log.w(TAG, "File tidak ditemukan untuk dihapus: $imagePath")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saat menghapus gambar: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    // Mendapatkan URI untuk dibagikan (FileProvider)
    fun getUriForImage(context: Context, imagePath: String): Uri? {
        val file = File(imagePath)
        return if (file.exists()) {
            try {
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error saat mendapatkan URI: ${e.message}")
                e.printStackTrace()
                null
            }
        } else {
            Log.w(TAG, "File tidak ditemukan untuk URI: $imagePath")
            null
        }
    }

    // Bersihkan gambar yang sudah tidak digunakan
    fun cleanupUnusedImages(context: Context, usedImagePaths: List<String>) {
        try {
            val directory = File(context.filesDir, IMAGE_DIRECTORY)
            if (directory.exists()) {
                val files = directory.listFiles()
                files?.forEach { file ->
                    if (!usedImagePaths.contains(file.absolutePath)) {
                        val isDeleted = file.delete()
                        Log.d(TAG, "Membersihkan gambar tidak terpakai: ${file.name}, berhasil: $isDeleted")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saat membersihkan gambar: ${e.message}")
        }
    }
}