package com.example.caloryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.caloryapp.navigation.Navigation
//import com.example.caloryapp.pages.NavBarScreen
import com.example.caloryapp.ui.theme.CaloryAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        val foodClassifier = FoodClassifier(this)
        setContent {
            CaloryAppTheme {
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
}