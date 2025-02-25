package com.example.caloryapp.navigation

enum class NavigationScreen {
    OnBoardingScreen,
    LoginScreen,
    NavBarScreen,
    HomeScreen,
    ForgotPasswordScreen,
    ChangePasswordScreen,
    OTPVerificationScreen,
    SuccessChangePassword,
    SuccessRegister,
    ProfileScreen;

    fun fromRoute(route: String): NavigationScreen =
        when (route.substringBefore("/")) {
            OnBoardingScreen.name -> OnBoardingScreen
            LoginScreen.name -> LoginScreen
            NavBarScreen.name -> NavBarScreen
            HomeScreen.name -> HomeScreen
            ProfileScreen.name -> ProfileScreen
            ForgotPasswordScreen.name -> ForgotPasswordScreen
            ChangePasswordScreen.name -> ChangePasswordScreen
            OTPVerificationScreen.name -> OTPVerificationScreen
            SuccessChangePassword.name -> SuccessChangePassword
            SuccessRegister.name -> SuccessRegister

            else -> throw IllegalArgumentException("$route gagal bji")
        }
}