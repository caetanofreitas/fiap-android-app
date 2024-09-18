package com.example.locaweb.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.locaweb.integrations.GetEmailsFilters
import com.example.locaweb.interfaces.IEmail
import com.example.locaweb.ui.components.email.EmailListItem
import com.example.locaweb.ui.components.email.Header
import com.example.locaweb.view.models.EmailViewModel
import kotlinx.coroutines.flow.StateFlow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    toggleFavorite: (String, Boolean) -> Unit,
    searchFilter: (String) -> Unit,
    emailList: List<IEmail>,
    filterEmails: StateFlow<GetEmailsFilters>,
    markersList: State<List<String>>,
    restartEmailFilter: () -> Unit,
    filterEmailList: (GetEmailsFilters) -> Unit,
    profileImage: String,
) {
    Column {
        Header(
            profileImage = profileImage,
            searchFilter = searchFilter,
            markersList = markersList,
            filterEmails = filterEmails,
            restartEmailFilter = restartEmailFilter,
            filterEmailList = filterEmailList,
            goToProfile = { navController.navigate("profile")}
        )
        Spacer(modifier = Modifier.size(16.dp))
        LazyColumn {
            items(emailList) {
                EmailListItem(
                    id = it.id,
                    imageUrl = it.imageUrl,
                    sender = it.sender,
                    subject = it.subject,
                    preview = it.preview ?: "",
                    date = it.date,
                    isFavorite = it.isFavorite,
                    toggleFavorite = {newState ->
                        toggleFavorite(it.id, newState)
                    },
                    onClick = { _ -> navController.navigate("emailDetail/${it.id}")}
                )
            }
        }
    }
}