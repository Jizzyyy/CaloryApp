package com.example.caloryapp.pages.account

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.R
import com.example.caloryapp.repository.UserRepository
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.blueunderlined
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.ui.theme.semibold
import com.example.caloryapp.viewmodel.UserViewModel
import com.example.caloryapp.widget.CustomTextField

@Composable
fun ProfileDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: UserViewModel
) {
    val user = viewModel.user.value
    var username by remember { mutableStateOf(user!!.username) }
    var gmail by remember { mutableStateOf(user!!.email) }
    var fullName by remember { mutableStateOf(user!!.fullName) }
    var selectedGender by remember { mutableStateOf(user!!.gender) }
    var weight by remember { mutableStateOf(user!!.weight.toString()) }
    var height by remember { mutableStateOf(user!!.height.toString()) }
    var isEditing by remember { mutableStateOf(false) }  // Untuk toggle mode edit
    val userRepository = UserRepository()
    val context = LocalContext.current

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
            Spacer(modifier.height(50.dp))
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
                    text = "Profil Saya",
                    style = TextStyle(
                        fontSize = 25.sp,
                        color = primaryblack,
                        fontFamily = bold
                    )
                )
                Row(
                    Modifier.fillMaxWidth(), Arrangement.End
                ) {
                    Text(
                        modifier = Modifier.clickable {
                            if (username.isNotEmpty() && fullName.isNotEmpty() && gmail.isNotEmpty() && selectedGender.isNotEmpty() && weight.isNotEmpty() && height.isNotEmpty()) {
                                if (isEditing) {
                                    // Jika tombol Edit ditekan dan dalam mode edit, simpan perubahan
                                    userRepository.updateUserData(
                                        username,
                                        fullName,
                                        gmail,
                                        selectedGender,
                                        weight,
                                        height
                                    ) { success ->
                                        if (success) {
                                            Toast.makeText(
                                                context,
                                                "Data Berhasil Diubah",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                            isEditing = false
                                        }
                                    }
                                } else {
                                    isEditing = true
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Gagal",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        },
                        text = if (isEditing) "Simpan" else "Edit",
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = blueunderlined,
                            fontFamily = semibold
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(64.dp))

            // Gambar profil
            Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_men),
                    contentDescription = null,
                    Modifier.size(100.dp)
                )
            }

            Spacer(modifier.height(45.dp))
            // Nama Lengkap
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
                value = fullName,
                onValueChange = { fullName = it },
                placeholderText = "Masukkan Nama Lengkap",
                input = isEditing,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier.height(20.dp))
            // Username
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
                value = username,
                onValueChange = { username = it },
                placeholderText = "Masukkan Username",
                input = false,  // Tidak dapat mengedit username
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier.height(20.dp))
            // Email
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
                value = gmail,
                onValueChange = { gmail = it },
                placeholderText = "Masukkan Email",
                input = isEditing,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier.height(20.dp))
            // Gender
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
                value = selectedGender,
                onValueChange = { selectedGender = it },
                placeholderText = "Masukkan Gender",
                input = isEditing,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier.height(20.dp))
            // Berat Badan
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
                value = weight,
                onValueChange = { weight = it },
                placeholderText = "Masukkan Berat Badan",
                input = isEditing,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier.height(20.dp))
            // Tinggi Badan
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
                value = height,
                onValueChange = { height = it },
                placeholderText = "Masukkan Tinggi Badan",
                input = isEditing,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Tombol Edit
            Spacer(modifier.height(30.dp))

        }
    }
}
