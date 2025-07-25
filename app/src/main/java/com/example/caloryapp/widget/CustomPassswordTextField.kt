package com.example.caloryapp.widget

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caloryapp.R
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.ui.theme.primarygrey
import com.example.caloryapp.ui.theme.semibold

@Composable
fun CustomPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    input: Boolean
) {
    // State untuk toggle password visibility
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = primaryblack,
            fontFamily = semibold,
            letterSpacing = 0.5.sp
        ),
        value = value,
        enabled = input,
        onValueChange = onValueChange,
        placeholder = {
            Text(
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
            .height(50.dp), // Add padding for the icon
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    painter = if (isPasswordVisible) painterResource(id = R.drawable.ic_show) else painterResource(
                        id = R.drawable.ic_hide
                    ),
                    contentDescription = null,
                    tint = Color.Gray
                )
//                Icon(
//                    imageVector = if (isPasswordVisible) Icons.Filled.Check else Icons.Filled.Check,
//                    contentDescription = "Toggle Password Visibility",
//                    tint = Color.Gray // Sesuaikan dengan warna ikon pada desain
//                )
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.White, // Warna putih dengan sedikit transparansi
            focusedBorderColor = Color.Transparent, // Menghilangkan border saat fokus
            unfocusedBorderColor = Color.Transparent, // Menghilangkan border saat tidak fokus
            cursorColor = Color.Black // Warna kursor
        ),
        shape = RoundedCornerShape(10.dp) // Membuat border melengkung
    )
}
