package com.example.locaweb.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerTextField(
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String,
    onChange: (LocalDate) -> Unit,
    value: LocalDate? = null,
) {
    var selectedDate by remember { mutableStateOf(value) }
    var showDialog by remember { mutableStateOf(false) }
    val state = rememberDatePickerState()

    val handleChange = {
        if (selectedDate == null) {
            selectedDate = LocalDate.now()
        }

        state.selectedDateMillis?.let {
             selectedDate = Instant
                .ofEpochMilli(it)
                .atZone(ZoneId.of("UTC"))
                .toLocalDate()
        }

        onChange(selectedDate!!)
        showDialog = false
    }

    Column(modifier = modifier) {
        if (label.isNotBlank()) {
            Text(
                text = label,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                textAlign = TextAlign.Start,
                color = Color(0xFF20313C),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFAFAFA),
            ),
            contentPadding = PaddingValues(12.dp)
        ) {
            Text(
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                text = selectedDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: placeholder,
                color = if (selectedDate == null) Color(0xFFC2C2C2) else Color.Black
            )
        }

        val confirmButton = @Composable {
            Button(
                onClick = handleChange,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF0142),
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "OK",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                )
            }
        }

        val dismissButton = @Composable {
            Button(
                onClick = { showDialog = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black,
                )
            ) {
                Text(
                    text = "Cancelar",
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                )
            }
        }


        if (showDialog) {
            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = confirmButton,
                dismissButton = dismissButton,
            ) {
                DatePicker(state = state)
            }
        }
    }
}
