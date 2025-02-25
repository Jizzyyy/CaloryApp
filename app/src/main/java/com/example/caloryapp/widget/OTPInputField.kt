package com.example.caloryapp.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeuisuite.ohteepee.OhTeePeeInput
import com.composeuisuite.ohteepee.configuration.OhTeePeeCellConfiguration
import com.composeuisuite.ohteepee.configuration.OhTeePeeConfigurations
import com.example.caloryapp.ui.theme.primary
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.ui.theme.primarygrey
import com.example.caloryapp.ui.theme.primaryred
import com.example.caloryapp.ui.theme.semibold

enum class OtpState { Loading, Error, None }

@Composable
fun OtpInput() {
    // a mutable state to handle OTP value changes…
    var otpValue: String by remember { mutableStateOf("") }
    var state: OtpState by remember {
        mutableStateOf(OtpState.None)
    }

    // this config will be used for each cell
    val defaultCellConfig = OhTeePeeCellConfiguration.withDefaults(
        borderColor = primarygrey,
        borderWidth = 1.dp,
        shape = RoundedCornerShape(16.dp),
        textStyle = TextStyle(
            fontSize = 20.sp,
            color = primaryblack,
            fontFamily = semibold
        )
    )

    OhTeePeeInput(
        value = otpValue,
        onValueChange = { newValue, isValid ->
            otpValue = newValue

            if (isValid) {
                state = OtpState.Loading
                // Send a request to validate the value here...
//                vm.validateOtp(otpValue)
            } else {
                // Reset the state when user enters (to remove the error state)
                state = OtpState.None
            }

        },
        configurations = OhTeePeeConfigurations.withDefaults(
            cellsCount = 4,
            emptyCellConfig = defaultCellConfig,
            activeCellConfig = defaultCellConfig.copy(
                borderColor = primary,
                borderWidth = 1.dp
            ),
            errorCellConfig = defaultCellConfig.copy(
                borderColor = primaryred,
                borderWidth = 1.dp
            ),
            cellModifier = Modifier
                .padding(horizontal = 10.dp)
                .size(60.dp),
            placeHolder = "-",
        ),
    )
}


