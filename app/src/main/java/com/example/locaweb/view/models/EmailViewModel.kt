package com.example.locaweb.view.models

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locaweb.database.DBHandler
import com.example.locaweb.interfaces.IEmail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class FilterState(
    var date: LocalDateTime? = null,
    var notReadFilter: Boolean = false,
    var withStarFilter: Boolean = false,
    var importantFilter: Boolean = false,
    var archivedFilter: Boolean = false,
    var markers: List<String> = emptyList(),
)

class EmailViewModel(context: Context): ViewModel() {
    private val _emails = MutableStateFlow(emptyList<IEmail>())
    val emails =_emails.asStateFlow()

    private val dbHandler: DBHandler = DBHandler(context)

    private val _filters = MutableStateFlow(FilterState())
    val filter = _filters.asStateFlow()

    private val _markersList = MutableStateFlow(emptyList<String>())
    val markers = _markersList.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        dbHandler.addNewEmail(
            IEmail(
                imageUrl = "https://picsum.photos/1080",
                sender = "Locaweb",
                subject = "Garanta sua Hospedagem",
                date = LocalDateTime.now(),
                content = "<h1>Olá senhor(a), email teste</h1>",
                preview = "Lorem ipsum dolor sit amet consectetur. Nec elit laculis nibgh send dolor felis rhocus. Sed. Lorem ipsum dolor sit amet consectetur. Nec elit laculis nibgh send dolor felis rhocus. Sed.",
                isFavorite = false,
            )
        )
        val loadedElement = dbHandler.readEmails()
        _emails.value = loadedElement
        _markersList.value = listOf("Marcadores", "Social", "Brachfast", "Financeiro", "Cursos", "Atualizações", "Gym", "Promoções", "Faculdade ADS", "Stream", "Outro Marcador")
    }

    private fun updateDb(email: IEmail) {
        dbHandler.updateEmail(email)
    }

    fun toggleFavorite(emailId: String, newState: Boolean) {
        _emails.value = _emails.value.map {
            if (it.id == emailId) {
                val updatedEmail = it.copy(isFavorite = newState)
                updateDb(updatedEmail)
                updatedEmail
            } else {
                it
            }
        }
    }

    fun findById(emailId: String): IEmail? {
        return _emails.value.firstOrNull { it.id == emailId }
    }

    fun filterBySearch(search: String) {
        loadData()
        if (search.isBlank()) {
            return
        }
        val searchResult = _emails.value.filter { email ->
            email.subject.contains(search, ignoreCase = true) ||
                    email.sender.contains(search, ignoreCase = true)
        }
        _emails.value = searchResult
    }

    fun restartState() {
        loadData()
        _filters.value = FilterState()
    }

    fun filterList(filterState: FilterState) {
        loadData()
        _filters.value = filterState
        val searchResult = _emails.value.filter { email ->
            val dateFilterPassed = filterState.date?.let { email.date.isEqual(it) || email.date.isAfter(it) } ?: true

            val notReadFilterPassed = !filterState.notReadFilter || email.isFavorite

            val withStarFilterPassed = !filterState.withStarFilter || email.isFavorite

            val importantFilterPassed = !filterState.importantFilter || email.isFavorite

            val archivedFilterPassed = !filterState.archivedFilter || email.isFavorite

            val markersFilterPassed = filterState.markers.isEmpty() || (email.markers?.any { v -> v in filterState.markers } ?: true)

            dateFilterPassed && notReadFilterPassed && withStarFilterPassed && importantFilterPassed && archivedFilterPassed && markersFilterPassed
        }
        _emails.value = searchResult
    }
}