package com.example.caloryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.caloryapp.navigation.Navigation
import com.example.caloryapp.pages.account.ProfileScreen
import com.example.caloryapp.pages.dashboard.HomeScreen
//import com.example.caloryapp.pages.NavBarScreen
import com.example.caloryapp.pages.onboard.LoginScreen
import com.example.caloryapp.pages.onboard.OnBoardingScreen
import com.example.caloryapp.ui.theme.CaloryAppTheme
import com.example.caloryapp.widget.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaloryAppTheme {
//                Navigation()
//                ProfileScreen(navController = rememberNavController())
                MainScreen()
            }
        }
    }
}