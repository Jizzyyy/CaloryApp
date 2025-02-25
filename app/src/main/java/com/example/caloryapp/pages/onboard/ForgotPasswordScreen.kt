package com.example.caloryapp.pages.onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.R
import com.example.caloryapp.navigation.NavigationScreen
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.medium
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.widget.CustomTextField

@Composable
fun ForgotPasswordScreen(modifier: Modifier = Modifier, navController: NavController) {
    var gmail by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.padding(horizontal = 25.dp, vertical = 50.dp)) {
            Spacer(Modifier.height(45.dp))
            Text(
                text = stringResource(R.string.lupa_password2),
                style = TextStyle(
                    fontSize = 35.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.masukkan_gmail_kamu_nanti_akan_kita_kirimkan_kode_otp_ke_gmail_kamu_yaa),
                style = TextStyle(
                    fontSize = 17.sp,
                    color = primaryblack,
                    fontFamily = medium
                )
            )
            Spacer(modifier.height(50.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_forgot_pw),
                contentDescription = null,
                Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier.height(60.dp))
            androidx.compose.material.Text(
                text = stringResource(R.string.masukkan_gmail),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(16.dp))
            CustomTextField(
                value = gmail,
                onValueChange = { gmail = it },
                placeholderText = stringResource(R.string.gmail)
            )
            Spacer(modifier.height(40.dp))
            Button(
                onClick = { navController.navigate(NavigationScreen.OTPVerificationScreen.name) },
                modifier
                    .width(360.dp)
                    .height(50.dp),
                colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = primary),
                shape = RoundedCornerShape(20.dp)
            ) {
                androidx.compose.material.Text(
                    text = stringResource(R.string.kirim_otp),
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