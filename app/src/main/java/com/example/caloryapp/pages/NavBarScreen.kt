package com.example.caloryapp.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.caloryapp.R
import com.example.caloryapp.pages.account.ProfileScreen
import com.example.caloryapp.pages.dashboard.HomeScreen
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.primary

sealed class BottomNavigationScreen(
    val title: String,
    val filledIcon: Int,
    val outlinedIcon: Int
) {
    data object HomeScreen :
        BottomNavigationScreen("Home", R.drawable.ic_home_filled, R.drawable.ic_home_outlined)

    data object ProfileScreen :
        BottomNavigationScreen("Akun", R.drawable.ic_profile_filled, R.drawable.ic_profile_outlined)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NavBarScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavigationScreen.HomeScreen,
        BottomNavigationScreen.ProfileScreen
    )

    Scaffold(
        bottomBar = { CustomBottomNavigation(navController, items) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Tambahkan aksi tombol scan */ },
                backgroundColor = Color(0xFF28A745),
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                modifier = Modifier.size(64.dp)
            ) {
                Icon(painterResource(id = R.drawable.scan), contentDescription = "Scan")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        content = {
            NavHost(
                navController = navController,
                startDestination = BottomNavigationScreen.HomeScreen.title
            ) {
            }
        }
                                                                                                                                                             )
}

@Composable
fun CustomBottomNavigation(navController: NavHostController, items: List<BottomNavigationScreen>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                items.forEach { screen ->
                    val isSelected = currentRoute == screen.title
                    Column(
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    navController.navigate(screen.title) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val iconId = if (isSelected) screen.filledIcon else screen.outlinedIcon
                        Icon(
                            painter = painterResource(id = iconId),
                            contentDescription = screen.title,
                            tint = if (isSelected) primary else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = screen.title,
                            color = if (isSelected) primary else Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = bold
                        )
                    }
                }
            }
        }
    }
}
