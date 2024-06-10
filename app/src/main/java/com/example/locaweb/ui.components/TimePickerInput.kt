package com.example.locaweb.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerTextField(
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String,
    onChange: (LocalTime) -> Unit,
) {
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val state = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute,
        is24Hour = true
    )

    val handleChange = {
        if (selectedTime == null) {
            selectedTime = LocalTime.now()
        }

        selectedTime = selectedTime!!
            .withHour(state.hour)
            .withMinute(state.minute)

        onChange(selectedTime!!)
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
                text = selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: placeholder,
                color = if (selectedTime == null) Color(0xFFC2C2C2) else Color.Black
            )
        }


        if (showDialog) {
            Dialog(onDismissRequest = handleChange) {
                Surface(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        TimeInput(
                            state = state
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
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
                            Button(
                                onClick = handleChange,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF0142),
                                    contentColor = Color.White,
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "OK",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
