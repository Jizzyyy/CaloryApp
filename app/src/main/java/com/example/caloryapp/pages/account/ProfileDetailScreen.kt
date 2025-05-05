package com.example.caloryapp.pages.account

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.R
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.viewmodel.UserViewModel
import com.example.caloryapp.widget.CustomTextField

@Composable
fun ProfileDetailScreen(
    modifier: Modifier = Modifier,
//    drawerState: DrawerState,
//    scope: kotlinx.coroutines.CoroutineScope,
    navController: NavController,
    viewModel: UserViewModel
) {
    val user = viewModel.user.value

    var username by remember { mutableStateOf("") }
    var gmail by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    Box(
        Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(
            Modifier
                .padding(horizontal = 25.dp, vertical = 50.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Spacer(modifier.height(50.dp))
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.Start,
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    Modifier
                        .size(28.dp)
                        .clickable { navController.popBackStack()}
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(
                    text = "Profil Saya",
                    style = TextStyle(
                        fontSize = 25.sp,
                        color = primaryblack,
                        fontFamily = bold
                    )
                )
            }
            Spacer(modifier = Modifier.height(64.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_men),
                    contentDescription = null,
                    Modifier.size(100.dp)
                )
            }

            Spacer(modifier.height(45.dp))
            Text(
                text = "Nama Lengkap",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(12.dp))
            CustomTextField(
                value = user!!.fullName,
                onValueChange = { fullName = it },
                placeholderText = "Masukkan Nama Lengkap",
                input = false
            )

            Spacer(modifier.height(20.dp))
            Text(
                text = "Username",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(12.dp))
            CustomTextField(
                value = user.username,
                onValueChange = {  },
                placeholderText = "Masukkan Nama Lengkap",
                input = false
            )

            Spacer(modifier.height(20.dp))
            Text(
                text = "Email",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(12.dp))
            CustomTextField(
                value = user.email,
                onValueChange = {  },
                placeholderText = "Masukkan Nama Lengkap",
                input = false
            )

            Spacer(modifier.height(20.dp))
            Text(
                text = "Gender",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(12.dp))
            CustomTextField(
                value = user.gender,
                onValueChange = {  },
                placeholderText = "Masukkan Nama Lengkap",
                input = false
            )

            Spacer(modifier.height(20.dp))
            Text(
                text = "Berat Badan",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(12.dp))
            CustomTextField(
                value = "${user.weight} kg",
                onValueChange = {  },
                placeholderText = "Masukkan Nama Lengkap",
                input = false
            )

            Spacer(modifier.height(20.dp))
            Text(
                text = "Tinggi Badan",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(12.dp))
            CustomTextField(
                value = "${user.height} cm",
                onValueChange = {  },
                placeholderText = "Masukkan Nama Lengkap",
                input = false
            )
        }
    }
}