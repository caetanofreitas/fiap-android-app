package com.example.locaweb.ui.components.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarHeader(state: CalendarState, it: CalendarMonth) {
    val scope = rememberCoroutineScope()

    val prevMonth = { date: YearMonth ->
        scope.launch {
            state.animateScrollToMonth(date.previousMonth)
        }
    }
    val nextMonth = { date: YearMonth ->
        scope.launch {
            state.animateScrollToMonth(date.nextMonth)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconButton(
            onClick = { prevMonth(it.yearMonth) },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                Icons.Filled.KeyboardArrowLeft,
                contentDescription = "prev",
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = "${it.yearMonth.month}",
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF162129),
        )
        IconButton(
            onClick = { nextMonth(it.yearMonth) },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                Icons.Filled.KeyboardArrowRight,
                contentDescription = "prev",
                modifier = Modifier.size(24.dp)
            )
        }
    }
    DaysOfWeekTitle()
}

@Composable
fun DaysOfWeekTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        for (dayOfWeek in daysOfWeek(firstDayOfWeek = firstDayOfWeekFromLocale())) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                fontSize = 16.sp,
                lineHeight = 20.sp,
                color = Color(0xFFC2C2C2),
            )
        }
    }
}
