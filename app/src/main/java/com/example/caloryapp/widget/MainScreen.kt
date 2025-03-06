package com.example.caloryapp.widget

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.caloryapp.R
import com.example.caloryapp.pages.account.ProfileScreen
import com.example.caloryapp.pages.dashboard.HomeScreen
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.medium
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.semibold
import kotlinx.coroutines.launch

sealed class DrawerScreen(val title: String) {
    data object HomeScreen : DrawerScreen("Home")
    data object ProfileScreen : DrawerScreen("Akun")
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, drawerState, scope)
        }
    ) {
        NavHost(navController = navController, startDestination = DrawerScreen.HomeScreen.title) {
            composable(DrawerScreen.HomeScreen.title) {
                HomeScreen(navController = navController, drawerState = drawerState, scope = scope)
            }
            composable(DrawerScreen.ProfileScreen.title) {
                ProfileScreen(navController = navController, drawerState = drawerState, scope = scope)
            }
        }
    }
}

@Composable
fun DrawerContent(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: kotlinx.coroutines.CoroutineScope
) {
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
                        text = "Naufal Kadhafi",
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontFamily = semibold
                        )
                    )
                    Text(
                        text = "@kadhafiinl",
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
    }
}

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
