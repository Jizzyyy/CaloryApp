package com.example.caloryapp.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.caloryapp.foodmodel.FoodDetectionViewModel
import com.example.caloryapp.pages.MainScreen
import com.example.caloryapp.pages.account.ProfileChangePasswordScreen
import com.example.caloryapp.pages.account.ProfileDetailScreen
import com.example.caloryapp.pages.account.ProfileScreen
import com.example.caloryapp.pages.camera.ScreenTest
import com.example.caloryapp.pages.onboard.ChangePasswordScreen
import com.example.caloryapp.pages.onboard.ForgotPasswordScreen
import com.example.caloryapp.pages.onboard.LoginScreen
import com.example.caloryapp.pages.onboard.OTPVerificationScreen
import com.example.caloryapp.pages.onboard.OnBoardingScreen
import com.example.caloryapp.pages.onboard.RegisterScreen
import com.example.caloryapp.pages.onboard.SuccessChangePassword
import com.example.caloryapp.pages.onboard.SuccessRegister
import com.example.caloryapp.viewmodel.UserViewModel

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()
    val foodViewModel: FoodDetectionViewModel = viewModel()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
        composable(NavigationScreen.ScreenTest.name) {
            ScreenTest(navController = navController, viewModel = foodViewModel)
        }
        composable(NavigationScreen.ProfileScreen.name) {
            ProfileScreen(
                navController = navController,
                viewModel = userViewModel,
                scope = scope,
                drawerState = drawerState
            )
        }
//        composable(NavigationScreen.ScreenTest.name) {
//            ScreenTest(navController = navController, viewModel = userViewModel)
//        }
//        composable(NavigationScreen.HomeScreen.name) {
//            HomeScreen(navController = navController, userViewModel)
//        }
        composable(NavigationScreen.MainScreen.name) {
            MainScreen(userViewModel, foodViewModel)
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