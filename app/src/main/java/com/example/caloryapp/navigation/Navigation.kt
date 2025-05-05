package com.example.caloryapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.caloryapp.pages.onboard.LoginScreen
import com.example.caloryapp.pages.account.ProfileDetailScreen
import com.example.caloryapp.pages.onboard.ChangePasswordScreen
import com.example.caloryapp.pages.onboard.ForgotPasswordScreen
import com.example.caloryapp.pages.onboard.OTPVerificationScreen
import com.example.caloryapp.pages.onboard.OnBoardingScreen
import com.example.caloryapp.pages.onboard.RegisterScreen
import com.example.caloryapp.pages.onboard.SuccessChangePassword
import com.example.caloryapp.pages.onboard.SuccessRegister
import com.example.caloryapp.viewmodel.UserViewModel
import com.example.caloryapp.pages.MainScreen
import com.example.caloryapp.pages.account.ProfileChangePasswordScreen

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = NavigationScreen.LoginScreen.name
    ) {
        composable(NavigationScreen.OnBoardingScreen.name) {
            OnBoardingScreen(navController = navController)
        }
        composable(NavigationScreen.LoginScreen.name) {
            LoginScreen(navController = navController, viewModel = userViewModel)
        }
        composable(NavigationScreen.RegisterScreen.name) {
            RegisterScreen(navController = navController, viewModel = userViewModel)
        }
        composable(NavigationScreen.ProfileDetailScreen.name) {
            ProfileDetailScreen(navController = navController, viewModel = userViewModel)
        }
        composable(NavigationScreen.ProfileChangePasswordScreen.name) {
            ProfileChangePasswordScreen(navController = navController, viewModel = userViewModel)
        }
//        composable(NavigationScreen.HomeScreen.name) {
//            HomeScreen(navController = navController, userViewModel)
//        }
        composable(NavigationScreen.MainScreen.name) {
            MainScreen(userViewModel)
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