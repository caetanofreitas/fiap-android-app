package com.example.locaweb.interfaces

import androidx.compose.ui.graphics.Color
import com.example.locaweb.utils.randomColor
import java.time.LocalDateTime
import java.util.UUID

data class IEvent(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var description: String,
    var startDate: LocalDateTime,
    var dueDate: LocalDateTime,
    val color: Color = randomColor(),
)