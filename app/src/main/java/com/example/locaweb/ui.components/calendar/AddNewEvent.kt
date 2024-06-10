package com.example.locaweb.ui.components.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.locaweb.interfaces.IEvent
import com.example.locaweb.ui.components.BottomSheetWrapper
import com.example.locaweb.ui.components.DatePickerTextField
import com.example.locaweb.ui.components.Input
import com.example.locaweb.ui.components.TimePickerTextField
import java.time.LocalDateTime

@Composable
fun AddNewEventBottomSheet(closeModal: () -> Unit, addNewEvent: (IEvent) -> Unit) {
    val eventState by remember { mutableStateOf(IEvent(
        name = "",
        description = "",
        startDate = LocalDateTime.now(),
        dueDate = LocalDateTime.now(),
    )) }
    var eventName by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }

    val handleAddEvent = {
        eventState.name = eventName
        eventState.description = eventDescription
        addNewEvent(eventState)
    }
    
    BottomSheetWrapper(
        title = "Novo Evento",
        onClose = closeModal,
        onConfirm = handleAddEvent,
        confirmText = "Criar evento",
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)) {
            Input(
                value = eventName,
                label = "Nome do evento",
                placeholder = "Nome",
                onChange = { eventName = it }
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)) {
            Input(
                label = "Descrição",
                placeholder = "Fale mais sobre seu evento",
                singleLine = false,
                maxHeight = 94,
                value = eventDescription,
                onChange = { eventDescription = it }
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)) {
            DatePickerTextField(
                label = "Data",
                placeholder = "00/00/0000",
                onChange = {
                    eventState.startDate = eventState.startDate
                        .withDayOfMonth(it.dayOfMonth)
                        .withMonth(it.monthValue)
                        .withYear(it.year)
                    eventState.dueDate = eventState.dueDate
                        .withDayOfMonth(it.dayOfMonth)
                        .withMonth(it.monthValue)
                        .withYear(it.year)
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TimePickerTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                label = "Inicio",
                placeholder = "07:00",
                onChange = {
                    eventState.startDate = eventState.startDate
                        .withHour(it.hour)
                        .withMinute(it.minute)
                        .withSecond(0)
                        .withNano(0)
                }
            )
            TimePickerTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                label = "Fim",
                placeholder = "08:00",
                onChange = {
                    eventState.dueDate = eventState.dueDate
                        .withHour(it.hour)
                        .withMinute(it.minute)
                        .withSecond(0)
                        .withNano(0)
                }
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)) {
            DatePickerTextField(
                label = "Repetir até",
                placeholder = "00/00/0000",
                onChange = {
                    eventState.dueDate = eventState.dueDate
                        .withDayOfMonth(it.dayOfMonth)
                        .withMonth(it.monthValue)
                        .withYear(it.year)
                }
            )
        }
    }
}