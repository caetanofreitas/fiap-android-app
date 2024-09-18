package com.example.locaweb.view.models

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locaweb.database.EmailDbHandler
import com.example.locaweb.integrations.ApiService
import com.example.locaweb.integrations.GetEmailsFilters
import com.example.locaweb.integrations.SendEmailBody
import com.example.locaweb.integrations.UserPreferences
import com.example.locaweb.interfaces.IEmail
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class EmailViewModel(
    private val context: Context,
    private val apiService: ApiService,
    private val accessModel: AccessViewModel
): ViewModel() {
    private val emailDbHandler: EmailDbHandler = EmailDbHandler(context)

    private val _emails = MutableStateFlow<List<IEmail>>(emptyList())
    val emails = _emails.asStateFlow()

    private val _selectedEmail = MutableStateFlow<IEmail?>(null)
    val selectedEmail = _selectedEmail.asStateFlow()

    private val _markersList = MutableStateFlow(emptyList<String>())
    val markers = _markersList.asStateFlow()

    private val _requestParams = MutableStateFlow(GetEmailsFilters())
    val filter = _requestParams.asStateFlow()

    private var startup = true

    init {
        loadData()
    }

    private fun loadData() = runBlocking {
        viewModelScope.launch {
            val user = accessModel.verifyUserInfo()

            if (user?.userPreferences != null && startup) {
                startup = false
                _requestParams.value = GetEmailsFilters(
                    read = if (user.userPreferences?.isNotReadActive == true) false else null,
                    favorite = user.userPreferences?.isFavoritesActive,
                    archived = user.userPreferences?.isArchivedActive,
                )
                _markersList.value = user.userPreferences!!.markers!!.toList()
            }

            val emailsResponse = apiService.getEmails(
                date = _requestParams.value.date,
                markers = _requestParams.value.markers,
                read = _requestParams.value.read,
                favorite = _requestParams.value.favorite,
                importants = _requestParams.value.importants,
                archived = _requestParams.value.archived,
                search = _requestParams.value.search,
                page = _requestParams.value.page,
                limit = _requestParams.value.limit,
            )
            if (emailsResponse.isSuccessful) {
                val apiEmails = emailsResponse.body()?.items?.toList()
                val emailsList = apiEmails?.map{
                    IEmail(
                        id = it.id,
                        imageUrl = it.sender_id.profile_picture,
                        sender = it.sender_id.name,
                        subject = it.content.subject,
                        date = convertToDate(it.send_date),
                        preview = it.content.preview,
                        isFavorite = it.favorite,
                        markers = it.markers,
                    )
                } ?: emptyList()
                _emails.value = emailsList.ifEmpty { emailDbHandler.readEmails() }
            } else {
                val loadedElement = emailDbHandler.readEmails()
                _emails.value = loadedElement
            }
        }
    }

    private fun convertToDate(date: String): LocalDateTime {
        val zonedDateTime = ZonedDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        return zonedDateTime.toLocalDateTime()
    }

    private fun updateDb(email: IEmail) {
        emailDbHandler.updateEmail(email)
    }

    fun toggleFavorite(emailId: String, newState: Boolean) {
        viewModelScope.launch {
            val res = apiService.toggleFavorite(emailId)
            if (res.isSuccessful) {
                    var listCopy = _emails.value
                    _emails.value = emptyList()
                    delay(1)
                    listCopy = listCopy.map {
                        val email = it.copy()
                        if (email.id == emailId) {
                            email.isFavorite = newState
                            updateDb(email)
                        }
                        email
                    }
                    _emails.value = listCopy
            }
        }
    }

    fun findById(emailId: String) {
        viewModelScope.launch {
            try {
                val res = apiService.getEmailDetail(emailId)
                if (res.isSuccessful) {
                    val email = res.body()
                    var foundInList = _emails.value.firstOrNull { it.id == emailId }
                    if (foundInList == null) {
                        foundInList = IEmail(
                            id = emailId,
                            imageUrl = "",
                            sender = "",
                            subject = email?.content?.subject ?: "",
                            date = (email?.send_date ?: LocalDateTime.now()) as LocalDateTime,
                            content = "",
                            preview = "",
                            isFavorite = email?.favorite ?: false,
                            markers = email?.markers,
                        )
                    }
                    foundInList.content = email?.content?.content ?: ""
                    _selectedEmail.value = foundInList
                } else {
                    _selectedEmail.value = null
                }
            } catch (e: Exception) {
                _selectedEmail.value = null
            }
        }
    }

    fun filterBySearch(search: String) {
        _requestParams.value.search = search
        loadData()
    }

    fun restartState() {
        _requestParams.value = GetEmailsFilters()
        loadData()
    }

    fun filterList(filterState: GetEmailsFilters) {
        _requestParams.value = filterState
        loadData()
    }

    fun sendEmail(body: SendEmailBody) {
        viewModelScope.launch {
            apiService.sendEmail(body)

        }
    }

    fun addToList(newItem: String) {
        _markersList.value = _markersList.value + newItem
        saveUserParams()
    }

    fun removeFromList(item: String) {
        _markersList.value = _markersList.value.filter { it != item }
        saveUserParams()
    }

    fun saveUserParams() {
        viewModelScope.launch {
            apiService.updateInfo(UserPreferences(
                is_not_read_active = _requestParams.value.read ?: false,
                is_favorites_active = _requestParams.value.favorite ?: false,
                is_archived_active = _requestParams.value.archived ?: false,
                color_mode = "light",
                markers = _markersList.value.toTypedArray(),
            ))
        }
    }
}
