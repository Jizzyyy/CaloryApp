package com.example.caloryapp.pages

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.caloryapp.R
import com.example.caloryapp.viewmodel.FoodDetectionViewModel
import com.example.caloryapp.navigation.NavigationScreen
import com.example.caloryapp.pages.account.ProfileChangePasswordScreen
import com.example.caloryapp.pages.account.ProfileDetailScreen
import com.example.caloryapp.pages.account.ProfileScreen
import com.example.caloryapp.pages.calorydetail.ScreenTest
import com.example.caloryapp.pages.dashboard.HomeScreen
import com.example.caloryapp.pages.onboard.LoginScreen
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.medium
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.semibold
import com.example.caloryapp.viewmodel.CaloryHistoryViewModel
import com.example.caloryapp.viewmodel.LoginState
import com.example.caloryapp.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.util.Log
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.caloryapp.pages.calorydetail.CaloryDetailScreen
import java.nio.charset.StandardCharsets

sealed class DrawerScreen(val title: String) {
    data object HomeScreen : DrawerScreen("Home")
    data object ProfileScreen : DrawerScreen("Akun")
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    userViewModel: UserViewModel,
    foodDetectionViewModel: FoodDetectionViewModel,
    caloryHistoryViewModel: CaloryHistoryViewModel
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Observer untuk status login
    val loginState by userViewModel.loginState

    // Navigasi otomatis ketika logout
    LaunchedEffect(loginState) {
        if (loginState is LoginState.LoggedOut) {
            // Navigasi ke login screen saat logout
            navController.navigate(NavigationScreen.LoginScreen.name) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    BackHandler {
        // Mencegah navigasi kembali ke login
        Toast.makeText(context, "Gunakan menu logout untuk keluar", Toast.LENGTH_SHORT).show()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Menggunakan fungsi DrawerContent yang dimodifikasi dengan penanganan null
            SafeDrawerContent(navController, drawerState, scope, userViewModel)
        }
    ) {
        NavHost(navController = navController, startDestination = DrawerScreen.HomeScreen.title) {
            composable(DrawerScreen.HomeScreen.title) {
                HomeScreen(navController = navController, drawerState = drawerState, scope = scope, caloryHistoryViewModel, userViewModel)
            }
            composable(DrawerScreen.ProfileScreen.title) {
                ProfileScreen(navController = navController, drawerState = drawerState, scope = scope, viewModel = userViewModel)
            }
            composable(NavigationScreen.ProfileDetailScreen.name) {
                ProfileDetailScreen(navController = navController,  viewModel = userViewModel)
            }
            composable(NavigationScreen.ProfileChangePasswordScreen.name) {
                ProfileChangePasswordScreen(navController = navController, viewModel = userViewModel)
            }
            composable(NavigationScreen.ScreenTest.name) {
                ScreenTest(navController = navController, viewModel = foodDetectionViewModel, userViewModel = userViewModel)
            }
            composable(NavigationScreen.LoginScreen.name) {
                LoginScreen(navController = navController, viewModel = userViewModel)
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
        }
    }
}

// Fungsi Drawer Content yang aman dari NullPointerException
@Composable
fun SafeDrawerContent(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: kotlinx.coroutines.CoroutineScope,
    viewModel: UserViewModel
) {
    val user = viewModel.user.value
    val context = LocalContext.current

    // Jika user null, tampilkan drawer dengan konten minimal
    if (user == null) {
        ModalDrawerSheet(modifier = Modifier.background(primary)) {
            Spacer(modifier = Modifier.height(30.dp))

            // Header untuk user yang belum login
            Row(
                modifier = Modifier.padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_women),
                    contentDescription = null,
                    Modifier.size(70.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Belum Login",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontFamily = semibold
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Divider(
                modifier = Modifier.padding(horizontal = 12.dp),
                color = primary.copy(alpha = 0.1f),
                thickness = 3.dp
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Menu navigasi standar
            DrawerItem(
                "Home",
                R.drawable.ic_home_filled,
                navController,
                DrawerScreen.HomeScreen.title,
                drawerState,
                scope
            )

            // Tombol login
            TextButton(onClick = {
                scope.launch {
                    drawerState.close()
                    navController.navigate(NavigationScreen.LoginScreen.name) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_profile_filled),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = primary,
                )
                Text(
                    "Login",
                    modifier = Modifier.padding(16.dp),
                    color = primary,
                    fontSize = 18.sp,
                    fontFamily = bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
        return
    }

    // Tampilkan drawer normal dengan informasi user
    ModalDrawerSheet(modifier = Modifier.background(primary)) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier.padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_women),
                    contentDescription = null,
                    Modifier.size(70.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        // Gunakan safe operator untuk menghindari null
                        text = user.fullName,
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontFamily = semibold
                        )
                    )
                    Text(
                        text = "@${user.username}",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontFamily = medium
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Divider(
            modifier = Modifier.padding(horizontal = 12.dp),
            color = primary.copy(alpha = 0.1f),
            thickness = 3.dp
        )
        Spacer(modifier = Modifier.height(10.dp))
        DrawerItem(
            "Home",
            R.drawable.ic_home_filled,
            navController,
            DrawerScreen.HomeScreen.title,
            drawerState,
            scope
        )
        DrawerItem(
            "Akun",
            R.drawable.ic_profile_filled,
            navController,
            DrawerScreen.ProfileScreen.title,
            drawerState,
            scope
        )

        // Tambahkan separator dan tombol logout
        Spacer(modifier = Modifier.height(20.dp))
        Divider(
            modifier = Modifier.padding(horizontal = 12.dp),
            color = primary.copy(alpha = 0.1f),
            thickness = 1.dp
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Tombol logout
//        LogoutDrawerItem(
//            title = "Logout",
//            icon = R.drawable.ic_profile_filled, // Gunakan icon yang sudah ada jika ic_logout tidak tersedia
//            onLogout = {
//                try {
//                    // Navigasi ke login screen terlebih dahulu
//                    scope.launch {
//                        drawerState.close()
//
//                        // Navigasi ke login screen
//                        navController.navigate(NavigationScreen.LoginScreen.name) {
//                            popUpTo(0) { inclusive = true }
//                        }
//
//                        // Delay sedikit untuk memastikan navigasi sudah berjalan
//                        delay(100)
//
//                        // Lakukan logout
//                        viewModel.logout()
//                    }
//                } catch (e: Exception) {
//                    Log.e("MainScreen", "Error during logout: ${e.message}")
//                    Toast.makeText(context, "Gagal logout: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        )
    }
}

// Fungsi untuk item menu drawer
@Composable
fun DrawerItem(
    title: String,
    icon: Int,
    navController: NavHostController,
    route: String,
    drawerState: DrawerState,
    scope: kotlinx.coroutines.CoroutineScope
) {
    TextButton(onClick = {
        if (navController.currentDestination?.route != route) {
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = false }
            }
        }
        scope.launch { drawerState.close() }
    }) {
        Icon(
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(30.dp),
            tint = primary,
        )
        Text(
            title,
            modifier = Modifier.padding(16.dp),
            color = primary,
            fontSize = 18.sp,
            fontFamily = bold,
            letterSpacing = 0.5.sp
        )
    }
}

// Tambahkan fungsi baru untuk tombol logout
@Composable
fun LogoutDrawerItem(
    title: String,
    icon: Int,
    onLogout: () -> Unit
) {
    TextButton(onClick = onLogout) {
        Icon(
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(30.dp),
            tint = Color.Red, // Warna merah untuk menandakan logout
        )
        Text(
            title,
            modifier = Modifier.padding(16.dp),
            color = Color.Red,
            fontSize = 18.sp,
            fontFamily = bold,
            letterSpacing = 0.5.sp
        )
    }
}