package com.example.caloryapp.pages.calorydetail

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer


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
