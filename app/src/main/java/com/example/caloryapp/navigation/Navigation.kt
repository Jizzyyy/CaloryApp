package com.example.caloryapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.caloryapp.pages.onboard.LoginScreen
import com.example.caloryapp.pages.NavBarScreen
import com.example.caloryapp.pages.onboard.ChangePasswordScreen
import com.example.caloryapp.pages.onboard.ForgotPasswordScreen
import com.example.caloryapp.pages.onboard.OTPVerificationScreen
import com.example.caloryapp.pages.onboard.OnBoardingScreen
import com.example.caloryapp.pages.onboard.SuccessChangePassword
import com.example.caloryapp.pages.onboard.SuccessRegister

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationScreen.OnBoardingScreen.name
    ) {
        composable(NavigationScreen.OnBoardingScreen.name) {
            OnBoardingScreen(navController = navController)
        }
        composable(NavigationScreen.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(NavigationScreen.NavBarScreen.name) {
            NavBarScreen()
        }
        composable(NavigationScreen.ForgotPasswordScreen.name) {
            ForgotPasswordScreen(navController = navController)
        }
        composable(NavigationScreen.ChangePasswordScreen.name) {
            ChangePasswordScreen(navController = navController)
        }
        composable(NavigationScreen.OTPVerificationScreen.name) {
            OTPVerificationScreen(navController = navController)
        }
        composable(NavigationScreen.SuccessRegister.name) {
            SuccessRegister(navController = navController)
        }
        composable(NavigationScreen.SuccessChangePassword.name) {
            SuccessChangePassword(navController = navController)
        }
    }
}