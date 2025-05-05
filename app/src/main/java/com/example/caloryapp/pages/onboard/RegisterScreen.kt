package com.example.caloryapp.pages.onboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.caloryapp.model.UserModel
import com.example.caloryapp.navigation.NavigationScreen
import com.example.caloryapp.repository.UserRepository
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.medium
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.viewmodel.UserViewModel
import com.example.caloryapp.widget.CustomTextField
import com.example.caloryapp.widget.GenderDropdown

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: UserViewModel
) {
    var username by remember { mutableStateOf("") }
    var gmail by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    val context = LocalContext.current
    val registerState by viewModel.registerState.collectAsState()
    val firestoreHelper = UserRepository()

    LaunchedEffect(registerState) {
        try {
            if (registerState == true) {

            } else if (registerState == false) {

            }
        } catch (e: Exception) {
            throw e
        }
        if (registerState == true) {
            Toast.makeText(context, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
//            navController.navigate("login") // Arahkan ke halaman login
        } else if (registerState == false) {
            Toast.makeText(context, "Registrasi gagal!", Toast.LENGTH_SHORT).show()
        }
    }


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
            Text(
                text = "Daftar",
                style = TextStyle(
                    fontSize = 35.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Yuk isi biodata kamu \n" +
                        "dulu!",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = medium
                )
            )

            // username
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.username),
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
                input = true
            )

            // gmail
            Spacer(modifier.height(16.dp))
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
                input = true,
                placeholderText = "Masukkan Email"
            )

            // nama lengkap
            Spacer(modifier.height(16.dp))
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
                onValueChange = { fullName = it }, input = true,
                placeholderText = "Masukkan Nama Lengkap"
            )

            // pw


            // nama lengkap
            Spacer(modifier.height(16.dp))
            Text(
                text = "Kata Sandi",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(12.dp))
            CustomTextField(
                value = password,
                onValueChange = { password = it }, input = true,
                placeholderText = "Masukkan Kata Sandi"
            )

            // gender
            Spacer(modifier.height(16.dp))
            Text(
                text = "Gender",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(12.dp))
            GenderDropdown(selectedGender = selectedGender) {
                selectedGender = it
            }

            // berat badan
            Spacer(modifier.height(16.dp))
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
                value = weight.toString(),
                onValueChange = { weight = it }, input = true,
                placeholderText = "Masukkan Berat Badan"
            )

            // tinggi badan
            Spacer(modifier.height(16.dp))
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
                value = height.toString(),
                onValueChange = { height = it }, input = true,
                placeholderText = "Masukkan Tinggi Badan"
            )

            // btn daftar
            Spacer(modifier.height(35.dp))
            Button(
                onClick = {
                    if (username.isEmpty() && password.isEmpty() && gmail.isEmpty() && fullName.isEmpty() && selectedGender.isEmpty() &&
                        weight.isEmpty() && height.isEmpty()
                    ) {
                        Toast.makeText(context, "Data Tidak Boleh Kosong!", Toast.LENGTH_SHORT)
                            .show()
                    } else if (username.isNotEmpty() && password.isNotEmpty() && gmail.isNotEmpty() && fullName.isNotEmpty() && selectedGender.isNotEmpty() &&
                        weight.isNotEmpty() && height.isNotEmpty()
                    ) {
                        val user = UserModel(
                            username,
                            fullName,
                            gmail,
                            password,
                            selectedGender,
                            weight,
                            height
                        )
                        firestoreHelper.registerUserWithCustomID(user, gmail) { success ->
                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Kamu Berhasil Membuat Akun !",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate(NavigationScreen.SuccessRegister.name)
                            } else {
                                Toast.makeText(context, "Error Registering", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
//                        viewModel.register(gmail, password, user)
//                        viewModel.registerUser(
//                            username, fullName, password, gmail, selectedGender,
//                            weight, height,
//                            onSuccess = {
////                                navController.navigate(NavigationScreen.MainScreen.name)
//                                Toast.makeText(context, "Pendaftaran Berhasil!", Toast.LENGTH_SHORT).show()
//                            },
//                            onFailure = {
//                                Log.e("Register", "Pendaftaran gagal")
//                                Toast.makeText(context, "GAGAL COKKK", Toast.LENGTH_SHORT).show()
//                            }
//                        )
                    }
                },
                modifier
                    .width(360.dp)
                    .height(50.dp),
                colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = primary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Daftar",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.White,
                        fontFamily = bold,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}