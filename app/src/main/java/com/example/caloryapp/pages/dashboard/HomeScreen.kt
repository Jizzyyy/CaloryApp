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
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.R
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.ui.theme.primarygrey
import com.example.caloryapp.widget.FilterBar
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.caloryapp.model.UserModel
import com.example.caloryapp.pages.camera.FoodClassifier
import com.example.caloryapp.viewmodel.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun HomeScreen(
    navController: NavController,
    drawerState: DrawerState,
    scope: kotlinx.coroutines.CoroutineScope,
    viewModel: UserViewModel,
) {
    var selectedFilter by remember { mutableStateOf("Semua") }
    val user = viewModel.user.value

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
                    modifier = Modifier.clickable {   },
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
        }
    }
}