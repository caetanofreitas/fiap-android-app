package com.example.locaweb.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.locaweb.ui.components.calendar.AddNewEventBottomSheet
import com.example.locaweb.ui.components.calendar.CalendarDayCell
import com.example.locaweb.ui.components.calendar.CalendarHeader
import com.example.locaweb.ui.components.calendar.EventListHeader
import com.example.locaweb.ui.components.calendar.EventsList
import com.example.locaweb.view.models.CalendarViewModel
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(model: CalendarViewModel) {
    val selectedDate by model.selectedDate.collectAsState()
    val calendarState = rememberCalendarState(
        startMonth = model.calendarFirstVisibleMonth.minusMonths(100),
        endMonth = model.calendarFirstVisibleMonth.plusMonths(100),
        firstVisibleMonth = model.calendarFirstVisibleMonth,
        firstDayOfWeek = firstDayOfWeekFromLocale()
    )
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val openNewEventBottomSheet = {
        scope.launch { sheetState.show() }
    }

    val closeNewEventBottomSheet = {
        scope.launch { sheetState.hide() }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .wrapContentSize()
            .padding(top = 12.dp, bottom = 80.dp)
    ) {
        HorizontalCalendar(
            state = calendarState,
            monthHeader = {
                CalendarHeader(state = calendarState, it)
            },
            dayContent = { day ->
                CalendarDayCell(day, isSelected = selectedDate == day.date, events = model.filterElementsPerData(day.date), model.setSelectedDate)
            },
        )
        Divider(modifier = Modifier.padding(vertical = 24.dp))
        EventListHeader(model.filterElementsPerData(selectedDate).isEmpty(), selectedDate) { openNewEventBottomSheet() }
        EventsList(model.filterElementsPerData(selectedDate)) { openNewEventBottomSheet() }
    }

    if (sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { closeNewEventBottomSheet() }) {
            AddNewEventBottomSheet(closeModal = { closeNewEventBottomSheet()}){
                    model.addNewEvent(it)
                    closeNewEventBottomSheet()
            }
        }
    }
}
