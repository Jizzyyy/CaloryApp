package com.example.caloryapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.caloryapp.navigation.Navigation
import com.example.caloryapp.navigation.NavigationScreen
//import com.example.caloryapp.pages.NavBarScreen
import com.example.caloryapp.ui.theme.CaloryAppTheme
import com.example.caloryapp.viewmodel.UserViewModel

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
                Navigation(context = applicationContext)
            }
        }
    }
    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }
}