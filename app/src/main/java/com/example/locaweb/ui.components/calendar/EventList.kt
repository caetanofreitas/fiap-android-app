package com.example.locaweb.ui.components.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.locaweb.interfaces.IEvent


@Composable
fun EventsList(list: List<IEvent>, onNewEvent: () -> Unit) {
    if (list.isEmpty()) {
        EmptyEventList(onNewEvent)
    } else {
        Column {
            list.forEach {
                Box(modifier = Modifier.padding(bottom = 24.dp)) {
                    EventItem(it)
                }
            }
        }
    }
}
