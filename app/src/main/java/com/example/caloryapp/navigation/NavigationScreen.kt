package com.example.caloryapp.navigation

enum class NavigationScreen {
    OnBoardingScreen,
    LoginScreen,
    RegisterScreen,
    NavBarScreen,
    HomeScreen,
    MainScreen,
    ForgotPasswordScreen,
    ChangePasswordScreen,
    OTPVerificationScreen,
    SuccessChangePassword,
    SuccessRegister,
    ProfileScreen,
    ProfileDetailScreen,
    ProfileChangePasswordScreen,
    CaloryDetailScreen,
    ScreenTest;

    fun fromRoute(route: String): NavigationScreen =
        when (route.substringBefore("/")) {
            OnBoardingScreen.name -> OnBoardingScreen
            LoginScreen.name -> LoginScreen
            RegisterScreen.name -> RegisterScreen
            NavBarScreen.name -> NavBarScreen
            MainScreen.name -> MainScreen
            HomeScreen.name -> HomeScreen
            ProfileScreen.name -> ProfileScreen
            ForgotPasswordScreen.name -> ForgotPasswordScreen
            ChangePasswordScreen.name -> ChangePasswordScreen
            OTPVerificationScreen.name -> OTPVerificationScreen
            SuccessChangePassword.name -> SuccessChangePassword
            SuccessRegister.name -> SuccessRegister
            ProfileDetailScreen.name -> ProfileDetailScreen
            ProfileChangePasswordScreen.name -> ProfileChangePasswordScreen
            CaloryDetailScreen.name -> CaloryDetailScreen
            ScreenTest.name -> ScreenTest

            else -> throw IllegalArgumentException("$route gagal bji")
        }
}