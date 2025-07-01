package com.example.caloryapp.navigation

import android.content.Context
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.caloryapp.viewmodel.FoodDetectionViewModel
import com.example.caloryapp.pages.MainScreen
import com.example.caloryapp.pages.account.ProfileChangePasswordScreen
import com.example.caloryapp.pages.account.ProfileDetailScreen
import com.example.caloryapp.pages.account.ProfileScreen
import com.example.caloryapp.pages.calorydetail.CaloryDetailScreen
import com.example.caloryapp.pages.calorydetail.ScreenTest
import com.example.caloryapp.pages.onboard.ChangePasswordScreen
import com.example.caloryapp.pages.onboard.ForgotPasswordScreen
import com.example.caloryapp.pages.onboard.LoginScreen
import com.example.caloryapp.pages.onboard.OTPVerificationScreen
import com.example.caloryapp.pages.onboard.OnBoardingScreen
import com.example.caloryapp.pages.onboard.RegisterScreen
import com.example.caloryapp.pages.onboard.SuccessChangePassword
import com.example.caloryapp.pages.onboard.SuccessRegister
import com.example.caloryapp.repository.UserRepository
import com.example.caloryapp.viewmodel.CaloryHistoryViewModel
import com.example.caloryapp.viewmodel.LoginState
import com.example.caloryapp.viewmodel.UserViewModel
import java.nio.charset.StandardCharsets

@Composable
fun Navigation(modifier: Modifier = Modifier, context: Context) {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()
    val foodViewModel: FoodDetectionViewModel = viewModel()
    val caloryHistoryViewModel: CaloryHistoryViewModel = viewModel()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        userViewModel.initSessionManager(context)
    }

    // Menentukan startDestination berdasarkan status login
    val startDestination = if (userViewModel.checkLoginStatus()) {
        NavigationScreen.MainScreen.name
    } else {
        NavigationScreen.LoginScreen.name
    }

    // Observer untuk status login
    val loginState by userViewModel.loginState

    // Navigasi otomatis ketika login berhasil
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                navController.navigate(NavigationScreen.MainScreen.name) {
                    popUpTo(NavigationScreen.LoginScreen.name) { inclusive = true }
                }
            }
            is LoginState.AlreadyLoggedIn -> {
                if (navController.currentDestination?.route != NavigationScreen.MainScreen.name) {
                    navController.navigate(NavigationScreen.MainScreen.name) {
                        popUpTo(NavigationScreen.LoginScreen.name) { inclusive = true }
                    }
                }
            }
            is LoginState.LoggedOut -> {
                navController.navigate(NavigationScreen.LoginScreen.name) {
                    popUpTo(0) { inclusive = true }
                }
            }
            else -> {}
        }
    }

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
            ScreenTest(navController = navController, viewModel = foodViewModel, userViewModel = userViewModel)
        }
        composable(NavigationScreen.ProfileScreen.name) {
            ProfileScreen(
                navController = navController,
                viewModel = userViewModel,
                scope = scope,
                drawerState = drawerState
            )
        }
        composable(
            route = "${NavigationScreen.CaloryDetailScreen.name}/{date}/{calories}/{imagePath}",
            arguments = listOf(
                navArgument("date") { type = NavType.StringType },
                navArgument("calories") { type = NavType.IntType },
                navArgument("imagePath") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val calories = backStackEntry.arguments?.getInt("calories") ?: 0
            val imagePath = backStackEntry.arguments?.getString("imagePath") ?: ""

            // Decode imagePath yang sudah diencoding
            val decodedImagePath = java.net.URLDecoder.decode(imagePath, StandardCharsets.UTF_8.name())

            CaloryDetailScreen(
                navController = navController,
                caloryDate = date,
                caloryCalories = calories,
                caloryImagePath = decodedImagePath,
                userViewModel = userViewModel,
                caloryHistoryViewModel = caloryHistoryViewModel
            )
        }
//        composable(NavigationScreen.ScreenTest.name) {
//            ScreenTest(navController = navController, viewModel = userViewModel)
//        }
//        composable(NavigationScreen.HomeScreen.name) {
//            HomeScreen(navController = navController, userViewModel)
//        }
        composable(NavigationScreen.MainScreen.name) {
            MainScreen(userViewModel, foodViewModel, caloryHistoryViewModel)
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