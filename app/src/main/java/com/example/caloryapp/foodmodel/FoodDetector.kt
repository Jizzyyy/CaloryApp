package com.example.caloryapp.foodmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class FoodDetector(private val context: Context) {
    private var interpreter: Interpreter? = null
    private val categories = listOf(
        FoodCategory.CARBS,
        FoodCategory.PROTEIN,
        FoodCategory.VEGETABLES,
        FoodCategory.FRUITS,
        FoodCategory.OTHER
    )

    init {
        loadModel()
    }

    private fun loadModel() {
        val assetFileDescriptor = context.assets.openFd("model_food_plate_densenet.tflite")
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        val mappedByteBuffer = fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            startOffset, declaredLength
        )
        interpreter = Interpreter(mappedByteBuffer)
    }

    fun detectFood(bitmap: Bitmap): FoodDetectionResult {
        // Resize bitmap to 128x128 (model input size)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true)

        // Convert bitmap to ByteBuffer - Perbaikan disini
        val modelInput = ByteBuffer.allocateDirect(128 * 128 * 3 * 4) // 4 bytes per float
        modelInput.order(ByteOrder.nativeOrder())

        // Citra harus dinormalisasi ke [0, 1] seperti di Python
        val pixels = IntArray(128 * 128)
        resizedBitmap.getPixels(pixels, 0, 128, 0, 0, 128, 128)

        for (pixel in pixels) {
            // Extract RGB values and normalize to [0, 1]
            val r = (pixel shr 16 and 0xFF) / 255.0f
            val g = (pixel shr 8 and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f

            // Memastikan urutan RGB sesuai dengan model
            modelInput.putFloat(r)
            modelInput.putFloat(g)
            modelInput.putFloat(b)
        }

        modelInput.rewind() // Penting: reset posisi buffer ke awal

        // Run model - pastikan outputBuffer memiliki dimensi yang benar
        val outputBuffer = Array(1) { FloatArray(5) } // 5 categories
        interpreter?.run(modelInput, outputBuffer)

        // Get predicted category
        val confidences = outputBuffer[0]
        val maxConfidenceIndex = confidences.indices.maxByOrNull { confidences[it] } ?: 0

        // Membuat hasil yang lebih baik dengan threshold confidence
        val minConfidenceThreshold = 0.4f // Atur sesuai kebutuhan

        // Create detection result with confidence map
        val detectedCategories = mutableMapOf<FoodCategory, Float>()
        categories.forEachIndexed { index, category ->
            detectedCategories[category] = confidences[index]
        }

        return FoodDetectionResult(
            mainCategory = categories[maxConfidenceIndex],
            confidence = confidences[maxConfidenceIndex],
            allCategories = detectedCategories
        )
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }
}