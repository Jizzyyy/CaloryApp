package com.example.caloryapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.caloryapp.navigation.Navigation
//import com.example.caloryapp.pages.NavBarScreen
import com.example.caloryapp.ui.theme.CaloryAppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        val foodClassifier = FoodClassifier(this)

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                PERMISSION_REQUEST_CODE
            )
        }

        setContent {
            CaloryAppTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    val viewModel = viewModel<FoodDetectionViewModel>()
//                    ScreenTest(viewModel)
//                }
//                val context = LocalContext.current
//                var classificationResult by remember { mutableStateOf("Memuat...") }
//
//                Box {
//                    CameraPreview(onImageCaptured = { byteBuffer ->
//                        val result = foodClassifier.classify(byteBuffer)
//                        classificationResult = result
//
//                        Toast.makeText(context, "Hasil: $result", Toast.LENGTH_SHORT).show()
//                    })
//                }
                Navigation()

//                LoginScreen(navController = rememberNavController())
//                ProfileScreen(navController = rememberNavController())
//                MainScreen()
            }
        }
    }
    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }
}