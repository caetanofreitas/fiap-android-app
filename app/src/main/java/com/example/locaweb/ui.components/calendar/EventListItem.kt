package com.example.locaweb.ui.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.locaweb.interfaces.IEvent
import com.example.locaweb.utils.getHour

@Composable
fun EventItem(event: IEvent) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFAFAFA))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp, end = 8.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .border(width = 3.dp, color = event.color)
                    .background(Color.Transparent)
            )
            Text(
                text = "${getHour(event.startDate)}-${getHour(event.dueDate)}",
                color = Color(0xFFC2C2C2),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 18.sp,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
        Text(
            text = event.name,
            color = Color(0xFF162129),
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
        )
        Text(
            text = event.description,
            color = Color(0xFFC2C2C2),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            modifier = Modifier.padding(bottom = 12.dp, start = 12.dp, end = 12.dp)
        )
    }
}
