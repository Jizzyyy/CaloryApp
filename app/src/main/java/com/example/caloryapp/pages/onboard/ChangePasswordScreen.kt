package com.example.caloryapp.pages.onboard

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.R
import com.example.caloryapp.navigation.NavigationScreen
import com.example.caloryapp.repository.UserRepository
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.widget.CustomPasswordTextField
import com.example.caloryapp.widget.CustomTextField

@Composable
fun ChangePasswordScreen(modifier: Modifier = Modifier, navController: NavController) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // Untuk menampilkan status loading
    val context = LocalContext.current
    val userRepository = UserRepository()

    Box(
        Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(Modifier.padding(horizontal = 25.dp, vertical = 50.dp)) {
            Spacer(Modifier.height(45.dp))
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    Modifier
                        .size(28.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    stringResource(R.string.buat_kata_sandi_baru),
                    style = TextStyle(
                        fontSize = 32.sp,
                        color = primaryblack,
                        fontFamily = bold
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Spacer(modifier.height(42.dp))
            androidx.compose.material.Text(
                text = stringResource(R.string.username),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(16.dp))
            CustomTextField(
                value = username,
                onValueChange = { username = it },
                input = true,
                placeholderText = "Masukkan Username Anda"
            )
            Spacer(modifier.height(20.dp))
            androidx.compose.material.Text(
                text = "Kata Sandi Baru",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(16.dp))
            CustomPasswordTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                placeholderText = "Masukkan Kata Sandi Baru",
                input = true
            )
            Spacer(modifier.height(20.dp))
            androidx.compose.material.Text(
                text = "Konfirmasi Kata Sandi Baru",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(16.dp))
            CustomPasswordTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholderText = "Konfirmasi Kata Sandi Baru",
                input = true
            )

            Spacer(modifier.height(45.dp))
            Button(
                onClick = {
                    if (username.isEmpty()) {
                        Toast.makeText(context, "Username Tidak Boleh Kosong!", Toast.LENGTH_SHORT)
                            .show()
                    } else if (newPassword.isEmpty()) {
                        Toast.makeText(context, "Masukkan Kata Sandi Terlebih Dahulu!", Toast.LENGTH_SHORT)
                            .show()
                    } else if (confirmPassword.isEmpty()) {
                        Toast.makeText(context, "Konfirmasi Kata Sandi Belum Diisi!", Toast.LENGTH_SHORT)
                            .show()
                    } else if (username.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()) {
                        if (newPassword == confirmPassword) {
                            isLoading = true // Menampilkan loading
                            userRepository.updatePasswordByUsername2(
                                username,
                                newPassword,
                                confirmPassword
                            ) { success ->
                                isLoading = false // Menyembunyikan loading setelah selesai
                                if (success) {
                                    // Navigasi ke layar sukses jika berhasil
                                    navController.navigate(NavigationScreen.SuccessChangePassword.name)
                                } else {
                                    // Tampilkan pesan kesalahan jika gagal
                                    Log.e("ChangePassword", "Username Tidak Terdapat atau Tidak Sesuai")
                                    Toast.makeText(context, "Username Tidak Terdapat atau Tidak Sesuai", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        } else {
                            // Tampilkan pesan kesalahan jika kata sandi tidak cocok
                            Log.e("ChangePassword", "Kata sandi tidak cocok")
                            Toast.makeText(
                                context,
                                "Konfirmasi Kata sandi tidak cocok",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                },
                modifier
                    .width(360.dp)
                    .height(50.dp),
                colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = primary),
                shape = RoundedCornerShape(20.dp)
            ) {
                if (isLoading) {
                    androidx.compose.material3.CircularProgressIndicator(color = Color.White) // Loading indicator
                } else {
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
}
