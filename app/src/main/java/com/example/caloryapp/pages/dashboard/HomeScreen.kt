package com.example.caloryapp.pages.dashboard

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.R
import com.example.caloryapp.navigation.NavigationScreen
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.medium
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.semibold
import com.example.caloryapp.viewmodel.CaloryHistoryViewModel
import com.example.caloryapp.viewmodel.UserViewModel
import com.example.caloryapp.widget.FilterBar


@Composable
fun HomeScreen(
    navController: NavController,
    drawerState: DrawerState,
    scope: kotlinx.coroutines.CoroutineScope,
    caloryHistoryViewModel: CaloryHistoryViewModel,
    viewModel: UserViewModel,
) {
    var selectedFilter by remember { mutableStateOf("Semua") }
    val user = viewModel.user.value

    LaunchedEffect(user) {
        user?.let {
            // Memuat 4 riwayat terbaru untuk ditampilkan di home
            caloryHistoryViewModel.loadHistoryByUsername(it.username, 2)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(modifier = Modifier.padding(horizontal = 25.dp, vertical = 45.dp)) {
            Spacer(modifier = Modifier.height(50.dp))

            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.clickable { },
                    painter = painterResource(id = R.drawable.ic_home_acc),
                    contentDescription = null
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = user!!.fullName,
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontFamily = bold
                            )
                        )
                        Text(
                            text = "@${user.username}",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Black,
                                fontFamily = bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_profile_women),
                        contentDescription = null,
                        Modifier.size(45.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(35.dp))

            // Teks sapaan
            Row(Modifier.width(215.dp)) {
                Text(
                    text = "Hai ${user!!.fullName}, Bagaimana kabar kamu hari ini?",
                    style = TextStyle(
                        fontSize = 22.sp,
                        color = Color.Black,
                        fontFamily = bold
                    )
                )
            }

//            Spacer(modifier = Modifier.height(15.dp))

            // Tombol untuk membuka Navigation Drawer
//            Button(onClick = {  }) {
//                Text(text = "Buka Menu")
//            }

            Spacer(modifier = Modifier.height(15.dp))
            androidx.compose.material3.Divider(color = primary.copy(alpha = 0.2f), thickness = 3.dp)
            Spacer(modifier = Modifier.height(15.dp))

            // Filter Bar
            FilterBar(selectedFilter = selectedFilter, onFilterSelected = { selectedFilter = it })
            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                if (caloryHistoryViewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = primary
                    )
                } else if (caloryHistoryViewModel.historyList.isEmpty()) {
                    Text(
                        text = "Belum ada riwayat makanan",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = medium,
                            color = Color.Gray
                        ),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(vertical = 24.dp)
                    )
                } else {
                    // List riwayat kalori menggunakan LazyColumn khusus
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(caloryHistoryViewModel.historyList) { history ->
                            // Card item untuk riwayat kalori
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
//                                        navController.navigate("${NavigationScreen.DetailHistory.name}/${history.id}")
                                    },
                                elevation = 4.dp,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF4AB54A).copy(alpha = 0.6f)) // Warna hijau sesuai gambar
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "${history.totalCalories} Kalori",
                                                style = TextStyle(
                                                    fontSize = 20.sp,
                                                    color = Color.White,
                                                    fontFamily = semibold
                                                )
                                            )

                                            Text(
                                                text = "Lihat Detail",
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    color = Color.White,
                                                    fontFamily = medium,
                                                    textDecoration = TextDecoration.Underline
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Spacer di akhir list
                        item {
                            Spacer(modifier = Modifier.height(70.dp))
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 50.dp, bottom = 70.dp),
            contentColor = Color.White,
            backgroundColor = primary,
            onClick = { navController.navigate(NavigationScreen.ScreenTest.name) }) {
            Icon(painter = painterResource(id = R.drawable.scan), contentDescription = null)
        }
    }
}