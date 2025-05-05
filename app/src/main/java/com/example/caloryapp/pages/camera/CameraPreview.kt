package com.example.caloryapp.pages.camera

import android.Manifest
import android.content.Context
import android.util.Size
import android.view.Surface
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.nio.ByteBuffer
import java.util.concurrent.Executors


//@Composable
//fun CameraPreview(
//    onImageCaptured: (ByteBuffer) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val context = LocalContext.current
//    val lifecycleOwner = LocalContext.current as LifecycleOwner
//    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
//    val previewView = remember { PreviewView(context) }
//
//    DisposableEffect(context) {
////        val cameraProviderFuture = ProcessCameraProvider.getInstance()
////        cameraProviderFuture.addListener({
////            val cameraProvider = cameraProviderFuture.get()
////            val preview = Preview.Builder().build().also {
////                it.setSurfaceProvider(previewView.surfaceProvider)
////            }
//
////            val imageAnalysis = ImageAnalysis.Builder()
////                .setTargetResolution(Size(128, 128))
////                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
////                .build()
////
////            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
////                val buffer = imageProxy.planes[0].buffer
////                onImageCaptured(buffer)
////                imageProxy.close()
////            }
////
////            try {
////                cameraProvider.unbindAll()
////                cameraProvider.bindToLifecycle(
////                    lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageAnalysis
////                )
////            } catch (e: Exception) {
////                e.printStackTrace()
////            }
////        }, ContextCompat.getMainExecutor(context))
////
////        onDispose { cameraExecutor.shutdown() }
////    }
////
////    AndroidView(
////        factory = { previewView },
////        modifier = modifier.fillMaxSize()
////    )
//}


@OptIn(ExperimentalGetImage::class)
private fun processImageProxy(imageProxy: ImageProxy, onImageCaptured: (ByteBuffer) -> Unit) {
    val image = imageProxy.image ?: return
    val planes = image.planes
    val buffer: ByteBuffer = planes[0].buffer
    val data = ByteArray(buffer.remaining())
    buffer.get(data)

    onImageCaptured(buffer) // Mengirim data gambar untuk klasifikasi

    imageProxy.close()
}
