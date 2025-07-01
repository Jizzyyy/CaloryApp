package com.example.caloryapp.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.OutlinedTextField
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caloryapp.R
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.ui.theme.primarygrey
import com.example.caloryapp.ui.theme.semibold

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    input: Boolean,
    keyboardOptions: KeyboardOptions
) {
    OutlinedTextField(
        textStyle =  TextStyle(
            fontSize = 16.sp,
            color = primaryblack,
            fontFamily = semibold,
            letterSpacing = 0.5.sp
        ),

        value = value,
        enabled = input,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        placeholder = {
            androidx.compose.material.Text(
                text = placeholderText,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = primarygrey,
                    fontFamily = bold,
                    letterSpacing = 0.5.sp
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.White, // Warna putih dengan sedikit transparansi
            focusedBorderColor = Color.Transparent, // Menghilangkan border saat fokus
            unfocusedBorderColor = Color.Transparent, // Menghilangkan border saat tidak fokus
            cursorColor = Color.Black // Warna kursor
        ),
        shape = RoundedCornerShape(10.dp) // Membuat border melengkung
    )
}