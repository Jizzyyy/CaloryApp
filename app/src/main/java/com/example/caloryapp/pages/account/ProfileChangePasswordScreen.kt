package com.example.caloryapp.pages.account

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.repository.UserRepository
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.viewmodel.UserViewModel
import com.example.caloryapp.widget.CustomPasswordTextField

@Composable
fun ProfileChangePasswordScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: UserViewModel
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    val userData = viewModel.user.value
    var isPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val userRepository = UserRepository()

    Box(
        Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(
            Modifier
                .padding(horizontal = 25.dp, vertical = 50.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Start) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    Modifier
                        .size(28.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(
                    text = "Ubah Password",
                    style = TextStyle(
                        fontSize = 25.sp,
                        color = primaryblack,
                        fontFamily = bold
                    )
                )
            }

            // Form untuk memasukkan password lama dan baru
            Spacer(modifier.height(30.dp))
            Text(
                text = "Password Lama",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(12.dp))
            CustomPasswordTextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                placeholderText = "Masukkan Password Lama",
                input = true
            )

            Spacer(modifier.height(18.dp))
            Text(
                text = "Password Baru",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(12.dp))
            CustomPasswordTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                placeholderText = "Masukkan Password Baru",
                input = true
            )

            Spacer(modifier.height(18.dp))
            Button(
                onClick = {
                    if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                        Toast.makeText(context, "Password tidak boleh kosong", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        // Panggil fungsi untuk memperbarui password berdasarkan username
                        userData?.username?.let {
                            userRepository.updatePasswordByUsername(
                                it,
                                oldPassword,
                                newPassword
                            ) { isSuccess ->
                                if (isSuccess) {
                                    Toast.makeText(
                                        context,
                                        "Password berhasil diperbarui",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.popBackStack()  // Navigasi kembali setelah berhasil
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Gagal memperbarui password",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .width(360.dp)
                    .height(50.dp),
                colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = primary),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Ubah",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.White,
                        fontFamily = MaterialTheme.typography.h1.fontFamily,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}
