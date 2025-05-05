package com.example.caloryapp.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caloryapp.ui.theme.bold
import com.example.caloryapp.ui.theme.primaryblack
import com.example.caloryapp.ui.theme.primarygrey

@Composable
fun GenderDropdown(selectedGender: String, onGenderSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Pria", "Wanita")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp))
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clickable { expanded = true },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (selectedGender.isEmpty()) "Pilih Gender" else selectedGender,
                color = if (selectedGender.isEmpty()) primarygrey else primaryblack,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontFamily = bold

            )
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown Icon", tint = Color.Gray)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            genderOptions.forEach { gender ->
                DropdownMenuItem(
                    text = { Text(gender) },
                    onClick = {
                        onGenderSelected(gender)
                        expanded = false
                    }
                )
            }
        }
    }
}
