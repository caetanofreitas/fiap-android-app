package com.example.locaweb.view.models

import android.content.ContentResolver
import android.provider.CalendarContract
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locaweb.interfaces.IEvent
import com.kizitonwose.calendar.core.CalendarDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import java.time.LocalDate
import java.time.YearMonth
import android.content.ContentValues
import android.database.Cursor
import android.icu.util.TimeZone
import androidx.lifecycle.ViewModelProvider
import com.example.locaweb.utils.convertDateToMilliseconds
import com.example.locaweb.utils.convertMillisecondsToDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CalendarViewModel(private val contentResolver: ContentResolver): ViewModel() {
    private val _events = MutableStateFlow(emptyList<IEvent>())
    val calendarFirstVisibleMonth: YearMonth = YearMonth.now()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    fun loadData() {
        val events: MutableList<IEvent> = mutableListOf()
        val projection = arrayOf(
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
        )
        val calendarId = getDefaultCalendarId()
        val selection = "${CalendarContract.Events.CALENDAR_ID} = ?"
        val selectionArgs = arrayOf(calendarId.toString())
        val cursor: Cursor? = contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
        null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val eventTitle = it.getColumnIndex(CalendarContract.Events.TITLE)
                val eventDesc = it.getColumnIndex(CalendarContract.Events.DESCRIPTION)
                val eventStart = it.getColumnIndex(CalendarContract.Events.DTSTART)
                val eventEnd = it.getColumnIndex(CalendarContract.Events.DTEND)

                val name = it.getString(eventTitle)
                val desc = it.getString(eventDesc)
                val startDate = convertMillisecondsToDate(it.getLong(eventStart))
                val endDate = convertMillisecondsToDate(it.getLong(eventEnd))

                val event = IEvent(
                    name = name,
                    description = desc,
                    startDate = startDate,
                    dueDate = endDate,
                )
                events.add(event)
            }
        }

        _events.value = events
    }

    fun addNewEvent(item: IEvent) {
        addEventToCalendar(item)
        _events.update {currentState ->
            val items = currentState.toMutableList()
            items.add(item)
            items.toImmutableList()
        }
    }

    private fun addEventToCalendar(item: IEvent) {
        viewModelScope.launch {
            val calendarId = getDefaultCalendarId()

            val eventValues = ContentValues().apply {
                put(CalendarContract.Events.CALENDAR_ID, calendarId)
                put(CalendarContract.Events.TITLE, item.name)
                put(CalendarContract.Events.DESCRIPTION, item.description)
                put(CalendarContract.Events.DTSTART, convertDateToMilliseconds(item.startDate))
                put(CalendarContract.Events.DTEND, convertDateToMilliseconds(item.dueDate))
                put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
            }

            withContext(Dispatchers.IO) {
                val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, eventValues)
            }
        }
    }

    private fun getDefaultCalendarId(): Long {
        val primaryCalendarId = getPrimaryCalendarId()
        return primaryCalendarId ?: getSystemDefaultCalendarId()
    }

    private fun getPrimaryCalendarId(): Long? {
        val projection = arrayOf(CalendarContract.Calendars._ID)
        val selection = "${CalendarContract.Calendars.IS_PRIMARY} = 1"
        val uri = CalendarContract.Calendars.CONTENT_URI

        contentResolver.query(uri, projection, selection, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(CalendarContract.Calendars._ID)
                return cursor.getLong(idIndex)
            }
        }

        return null
    }

    private fun getSystemDefaultCalendarId(): Long {
        val projection = arrayOf(CalendarContract.Calendars._ID)
        val selection = "${CalendarContract.Calendars.VISIBLE} = 1" // Calendários visíveis
        val uri = CalendarContract.Calendars.CONTENT_URI

        contentResolver.query(uri, projection, selection, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(CalendarContract.Calendars._ID)
                return cursor.getLong(idIndex)
            }
        }

        return 0L
    }

    val setSelectedDate = { d: CalendarDay ->
        _selectedDate.update { d.date }
    }

    fun filterElementsPerData(data: LocalDate): List<IEvent> {
        return _events.value.filter {
            (
                data.isEqual(it.startDate.toLocalDate()) || data.isAfter(it.startDate.toLocalDate())
            ) && (
                data.isEqual(it.dueDate.toLocalDate()) || data.isBefore(it.dueDate.toLocalDate())
            )
        }
    }
}

class CalendarViewModelFactory(private val contentResolver: ContentResolver) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            return CalendarViewModel(contentResolver) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}