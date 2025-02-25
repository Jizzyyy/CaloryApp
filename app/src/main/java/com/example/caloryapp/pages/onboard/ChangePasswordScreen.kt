package com.example.caloryapp.pages.onboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.R
import com.example.caloryapp.navigation.NavigationScreen
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.widget.CustomTextField

@Composable
fun ChangePasswordScreen(modifier: Modifier = Modifier, navController: NavController) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        Modifier
            .fillMaxSize()
            .background(background)) {
        Column(Modifier.padding(horizontal = 25.dp, vertical = 50.dp)) {
            Spacer(Modifier.height(45.dp))
            Row(Modifier.width(260.dp)) {
                Text(
                    stringResource(R.string.buat_kata_sandi_baru),
                    style = TextStyle(
                        fontSize = 35.sp,
                        color = primaryblack,
                        fontFamily = bold
                    )
                )
            }
            Spacer(modifier.height(42.dp))
            androidx.compose.material.Text(
                text = stringResource(R.string.masukkan_kata_sandi),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(16.dp))
            CustomTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                placeholderText = "Masukkan Kata Sandi"
            )
            Spacer(modifier.height(20.dp))
            androidx.compose.material.Text(
                text = stringResource(R.string.konfirmasi_kata_sandi),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(16.dp))
            CustomTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholderText = "Konfirmasi Kata Sandi"
            )
            Spacer(modifier.height(45.dp))
            Button(
                onClick = { navController.navigate(NavigationScreen.SuccessChangePassword.name) },
                modifier
                    .width(360.dp)
                    .height(50.dp),
                colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = primary),
                shape = RoundedCornerShape(20.dp)
            ) {
                androidx.compose.material.Text(
                    text = "Simpan",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.White,
                        fontFamily = bold,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}