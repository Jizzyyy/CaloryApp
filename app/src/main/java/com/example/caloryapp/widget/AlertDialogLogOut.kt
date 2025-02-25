package com.example.caloryapp.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.caloryapp.R
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.medium
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.ui.theme.primaryred
import com.example.caloryapp.ui.theme.semibold

@Composable
fun SimpleAlertDialog(
    dialogTitle: String,
    dialogSubTitle: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(0.92f),
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true,
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        ),
        shape = RoundedCornerShape(20.dp),
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                androidx.compose.material.Text(
                    text = stringResource(R.string.ya),
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = primaryred,
                        fontFamily = semibold
                    )
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                androidx.compose.material.Text(
                    text = stringResource(R.string.tidak),
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = primaryblack,
                        fontFamily = semibold
                    )
                )
            }
        },
        title = {
            androidx.compose.material.Text(
                text = dialogTitle,
                style = TextStyle(
                    fontSize = 24.sp,
                    color = primaryblack,
                    fontFamily = bold
                )
            )
        },
        text = {
            androidx.compose.material.Text(
                text = dialogSubTitle,
                style = TextStyle(
                    fontSize = 18.sp,
                    color = primaryblack,
                    fontFamily = medium
                )
            )
        })
}