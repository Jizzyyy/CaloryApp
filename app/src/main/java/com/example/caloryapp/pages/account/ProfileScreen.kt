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
import androidx.compose.material.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.unregisterForAllProfilingResults
import androidx.navigation.NavController
import com.example.caloryapp.R
import com.example.caloryapp.navigation.NavigationScreen
import com.example.caloryapp.pages.DrawerScreen
import com.example.caloryapp.ui.theme.background
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.medium
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.ui.theme.primarygrey
import com.example.caloryapp.ui.theme.primaryred
import com.example.caloryapp.ui.theme.regular
import com.example.caloryapp.viewmodel.UserViewModel
import com.example.caloryapp.widget.SimpleAlertDialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    drawerState: DrawerState,
    scope: kotlinx.coroutines.CoroutineScope,
    viewModel: UserViewModel,
    ) {
    val user = viewModel.user.value
    val openAlertDialog = remember { mutableStateOf(false) }
    val currentDate = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
    val formattedDate = dateFormat.format(currentDate)

    Box(
        modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(modifier.padding(horizontal = 25.dp, vertical = 45.dp)) {
            Spacer(modifier.height(50.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.clickable { scope.launch { drawerState.open() } },
                    painter = painterResource(id = R.drawable.ic_home_acc),
                    contentDescription = null
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = user!!.fullName,
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontFamily = bold
                            )
                        )
                        Text(
                            text = "@${user.username}",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Black,
                                fontFamily = bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_profile_women),
                        contentDescription = null,
                        Modifier.size(45.dp)
                    )
                }
            }

            Spacer(modifier.height(40.dp))
            Text(
                text = "Hari Ini,",
                style = TextStyle(
                    fontSize = 38.sp,
                    color = Color.Black,
                    fontFamily = bold
                )
            )
            Text(
                text = formattedDate,
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontFamily = medium
                )
            )
            Spacer(modifier.height(10.dp))
            Divider(color = primary.copy(alpha = 0.2f), thickness = 3.dp)
            Spacer(modifier.height(40.dp))
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
                    modifier = Modifier.clickable { navController.navigate(NavigationScreen.ProfileDetailScreen.name) },
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
                    modifier = Modifier.clickable { navController.navigate(NavigationScreen.ProfileChangePasswordScreen.name) },
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
