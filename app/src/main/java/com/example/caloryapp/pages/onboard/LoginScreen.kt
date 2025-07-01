package com.example.caloryapp.pages.onboard

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.R
import com.example.caloryapp.navigation.NavigationScreen
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.blueunderlined
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.fontGrey
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.ui.theme.semibold
import com.example.caloryapp.viewmodel.LoginState
import com.example.caloryapp.viewmodel.UserViewModel
import com.example.caloryapp.widget.CustomPasswordTextField
import com.example.caloryapp.widget.CustomTextField

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: UserViewModel
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state = viewModel.loginState.value
    val userData = viewModel.user.value
    val context = LocalContext.current

    LaunchedEffect(key1 = state) {
        if (state is LoginState.Success) {
            Toast.makeText(context, "Selamat Datang, ${state.user.fullName}", Toast.LENGTH_SHORT).show()
            navController.navigate(NavigationScreen.MainScreen.name)
        } else if (state is LoginState.Error) {
            Toast.makeText(context, "Username atau Kata Sandi Tidak Sesuai!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    Box(
        modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(
            modifier
                .padding(horizontal = 25.dp, vertical = 50.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier.height(50.dp))
            Text(
                text = stringResource(R.string.masuk),
                style = TextStyle(
                    fontSize = 35.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(35.dp))
            Image(
                painter = painterResource(id = R.drawable.image_onboard),
                contentDescription = null,
                Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier.height(35.dp))
            Text(
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
                onValueChange = { username = it }, input = true,
                placeholderText = "Masukkan Username",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier.height(16.dp))
            Text(
                text = stringResource(R.string.password),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(16.dp))
            CustomPasswordTextField(
                value = password,
                onValueChange = { password = it },
                placeholderText = "Masukkan Kata Sandi",
                input = true
            )
            Spacer(modifier.height(18.dp))
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.End
            ) {
                Text(
                    modifier = Modifier.clickable { navController.navigate(NavigationScreen.ChangePasswordScreen.name) },
                    text = "Lupa Password",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = blueunderlined,
                        fontFamily = semibold,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
            Spacer(modifier.height(35.dp))

            Button(
                onClick = {
                    if (username.isEmpty()) {
                        Toast.makeText(context, "Username Tidak Boleh Kosong", Toast.LENGTH_SHORT)
                            .show()
                    } else if (password.isEmpty()){
                        Toast.makeText(context, "Password Tidak Boleh Kosong", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.login(username, password)
                    }
                },
                modifier
                    .width(360.dp)
                    .height(50.dp),
                colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = primary),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.masuk),
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.White,
                        fontFamily = bold,
                        textAlign = TextAlign.Center
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                Modifier.fillMaxWidth(), Arrangement.Center
            ) {
                Text(
                    text = "Belum Punya Akun ?",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = fontGrey,
                        fontFamily = semibold
                    )
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    modifier = Modifier.clickable { navController.navigate(NavigationScreen.RegisterScreen.name) },
                    text = "Daftar",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = blueunderlined,
                        fontFamily = semibold,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }

//            if (state is LoginState.Error) {
//                Text(text = "", color = MaterialTheme.colors.error)
//                Toast.makeText(context, "Username atau Kata Sandi Tidak Sesuai!", Toast.LENGTH_SHORT)
//                    .show()
//            }

            Spacer(modifier.height(42.dp))
        }
    }
}
