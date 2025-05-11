package com.example.caloryapp.pages.calorydetail

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer

class FoodClassifier(context: Context) {
    private val interpreter: Interpreter
    private val categories = arrayOf("Karbohidrat", "Protein", "Sayur", "Buah", "Lainnya")

    init {
        val model = context.assets.open("model_food_plate_densenet.tflite").use { it.readBytes() }
        interpreter = Interpreter(ByteBuffer.wrap(model))
    }

    fun classify(imageData: ByteBuffer): String {
        val output = Array(1) { FloatArray(categories.size) }
        interpreter.run(imageData, output)
        val maxIndex = output[0].indices.maxByOrNull { output[0][it] } ?: -1
        return if (maxIndex >= 0) categories[maxIndex] else "Tidak Terdeteksi"
    }
}