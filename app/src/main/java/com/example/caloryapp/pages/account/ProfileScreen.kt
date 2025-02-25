package com.example.caloryapp.pages.account

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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caloryapp.R
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.ui.theme.primarygrey
import com.example.caloryapp.ui.theme.primaryred
import com.example.caloryapp.widget.SimpleAlertDialog

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, navController: NavController) {
    val openAlertDialog = remember { mutableStateOf(false) }

    Box(
        modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(modifier.padding(horizontal = 25.dp, vertical = 40.dp)) {
            Spacer(modifier.height(50.dp))
            androidx.compose.material.Text(
                text = stringResource(R.string.akun),
                style = TextStyle(
                    fontSize = 35.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
            Spacer(modifier.height(50.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_men),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier.width(20.dp))
                Column {
                    androidx.compose.material.Text(
                        text = "Naufal Kadhafi",
                        style = TextStyle(
                            fontSize = 23.sp,
                            color = primaryblack,
                            fontFamily = bold
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    androidx.compose.material.Text(
                        text = "kadhafinaufal2@gmail.com",
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = primarygrey,
                            fontFamily = bold
                        )
                    )
                }
            }
            Spacer(modifier.height(60.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Row {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_profile),
                        contentDescription = null
                    )
                    Spacer(modifier.width(25.dp))
                    Text(
                        text = stringResource(R.string.profile),
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = primaryblack,
                            fontFamily = bold
                        )
                    )
                }
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_btn_detail),
                    contentDescription = null
                )
            }

            Spacer(modifier.height(40.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Row {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_history),
                        contentDescription = null
                    )
                    Spacer(modifier.width(25.dp))
                    Text(
                        text = stringResource(R.string.riwayat),
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = primaryblack,
                            fontFamily = bold
                        )
                    )
                }
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_btn_detail),
                    contentDescription = null
                )
            }

            Spacer(modifier.height(40.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Row {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_change_pw),
                        contentDescription = null
                    )
                    Spacer(modifier.width(25.dp))
                    Text(
                        text = stringResource(R.string.ubah_password),
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = primaryblack,
                            fontFamily = bold
                        )
                    )
                }
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_btn_detail),
                    contentDescription = null
                )
            }

            Spacer(modifier.height(60.dp))
            Row(modifier.clickable {
                openAlertDialog.value = true
            }, verticalAlignment = Alignment.CenterVertically) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_log_out),
                    contentDescription = null
                )
                Spacer(modifier.width(20.dp))
                Text(
                    text = stringResource(R.string.keluar),
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = primaryred,
                        fontFamily = bold
                    )
                )
            }
            if (openAlertDialog.value) {
                SimpleAlertDialog(
                    dialogTitle = "Konfirmasi",
                    dialogSubTitle = "Apakah Anda yakin ingin keluar?",
                    onDismissRequest = { openAlertDialog.value = false },
                    onConfirmation = {
                        openAlertDialog.value = false
                        // Tambahkan aksi logout di sini
                    }
                )
            }
        }
    }
}
