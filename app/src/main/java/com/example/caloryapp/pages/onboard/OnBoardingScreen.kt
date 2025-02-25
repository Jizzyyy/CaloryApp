package com.example.caloryapp.pages.onboard

import androidx.compose.foundation.Image
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
import com.example.caloryapp.ui.theme.primary

@Composable
fun OnBoardingScreen(modifier: Modifier = Modifier, navController: NavController) {
    Box(modifier.fillMaxSize()) {
        Column(
            modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp, vertical = 40.dp)
        ) {
            Spacer(modifier.height(60.dp))
            Text(
                text = stringResource(R.string.selamat_datang) +
                        stringResource(R.string.di_caloriey_app),
                style = TextStyle(
                    fontSize = 35.sp,
                    color = primary,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(80.dp))
            Image(
                painter = painterResource(id = R.drawable.image_onboard),
                contentDescription = null,
                Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier.height(32.dp))
            Row(modifier.align(Alignment.CenterHorizontally)) {
                Text(
                    text = "Lorem ipsum dolor sit amet consectetur. Feugiat pellentesque tellus feugiat tristique.",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = primary,
                        fontFamily = bold,
                        textAlign = TextAlign.Center
                    )
                )
            }
            Spacer(modifier.height(200.dp))
            Row(
                modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = { navController.navigate(NavigationScreen.LoginScreen.name) },
                    modifier
                        .width(360.dp)
                        .height(50.dp),
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = primary),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.lanjut),
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

//@Preview(showBackground = true)
//@Composable
//fun Preview(modifier: Modifier = Modifier) {
//    OnBoardingScreen(navController = rememberNavController())
//}