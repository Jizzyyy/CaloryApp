package com.example.caloryapp.pages.onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.R
import com.example.caloryapp.navigation.NavigationScreen
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.blueunderlined
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.ui.theme.semibold
import com.example.caloryapp.widget.CustomTextField

@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(
            modifier.padding(horizontal = 25.dp, vertical = 50.dp)
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
                onValueChange = { username = it },
                placeholderText = "Username"
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
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                placeholderText = "Password"
            )
            Spacer(modifier.height(18.dp))
            Row(
                modifier
                    .align(Alignment.End)
                    .clickable { navController.navigate(NavigationScreen.ForgotPasswordScreen.name) }) {
                Text(
                    text = stringResource(R.string.lupa_password),
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = blueunderlined,
                        fontFamily = semibold,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
            Spacer(modifier.height(35.dp))
            Button(
                onClick = { navController.navigate(NavigationScreen.NavBarScreen.name) },
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
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun Preview(modifier: Modifier = Modifier) {
//    LoginScreen(navController = rememberNavController())
//}