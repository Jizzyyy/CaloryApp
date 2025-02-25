package com.example.caloryapp.pages.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.R
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.ui.theme.primarygrey
import com.example.caloryapp.widget.FilterBar

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    var selectedFilter by remember { mutableStateOf("Semua") }

    Box(
        modifier
            .fillMaxSize()
            .background(background)) {
        Column(modifier.padding(horizontal = 25.dp, vertical = 45.dp)) {
            Spacer(modifier = Modifier.height(50.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.ic_home_acc), contentDescription = null)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        androidx.compose.material.Text(
                            text = "Naufal Kadhafi",
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = primaryblack,
                                fontFamily = bold
                            )
                        )
                        androidx.compose.material.Text(
                            text = "@kadhafiinl",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = primaryblack,
                                fontFamily = bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Image(painter = painterResource(id = R.drawable.ic_profile_women), contentDescription = null, Modifier.size(45.dp))
                }
            }
            Spacer(modifier = Modifier.height(35.dp))
            Row(Modifier.width(215.dp)) {
                Text(
                    text = "Hai Naufal, Bagaimana kabar kamu hari ini?",
                    style = TextStyle(
                        fontSize = 22.sp,
                        color = primaryblack,
                        fontFamily = bold
                    )
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Divider(color = primary.copy(alpha = 0.2f), thickness = 3.dp)
            Spacer(modifier = Modifier.height(15.dp))
            FilterBar(selectedFilter = selectedFilter, onFilterSelected = { selectedFilter = it})
        }
    }
}