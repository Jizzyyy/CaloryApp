package com.example.caloryapp.pages.onboard

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.R
import com.example.caloryapp.navigation.NavigationScreen
import com.example.caloryapp.ui.theme.blueunderlined
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.medium
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.ui.theme.semibold
import com.example.caloryapp.widget.OtpInput

@Composable
fun OTPVerificationScreen(modifier: Modifier = Modifier, navController: NavController) {
    Box(modifier.fillMaxSize()) {
        Column(Modifier.padding(horizontal = 25.dp, vertical = 50.dp)) {
            Spacer(Modifier.height(45.dp))
            Text(
                stringResource(R.string.kode_otp),
                style = TextStyle(
                    fontSize = 35.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.masukkan_kode_otp_yang_sudah_dikirimkan_ke_gmail_kamu_yaa),
                style = TextStyle(
                    fontSize = 17.sp,
                    color = primaryblack,
                    fontFamily = medium
                )
            )
            Spacer(modifier.height(50.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                OtpInput()
            }
            Spacer(modifier.height(18.dp))
            Row(
                modifier
                    .fillMaxWidth()
                    .clickable { }, Arrangement.End) {
                androidx.compose.material.Text(
                    text = stringResource(R.string.kirim_ulang_kode),
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = blueunderlined,
                        fontFamily = semibold,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
            Spacer(modifier.height(45.dp))
            Button(
                onClick = { navController.navigate(NavigationScreen.ChangePasswordScreen.name) },
                modifier
                    .width(360.dp)
                    .height(50.dp),
                colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = primary),
                shape = RoundedCornerShape(20.dp)
            ) {
                androidx.compose.material.Text(
                    text = stringResource(R.string.verifikasi_otp),
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