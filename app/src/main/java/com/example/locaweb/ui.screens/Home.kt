package com.example.locaweb.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.locaweb.ui.components.email.EmailListItem
import com.example.locaweb.ui.components.email.Header
import com.example.locaweb.view.models.EmailViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    model: EmailViewModel,
) {
    val list by model.emails.collectAsState()
    Column {
        Header(profileImage = "https://picsum.photos/1080", model)
        Spacer(modifier = Modifier.size(16.dp))
        LazyColumn {
            items(list) {
                EmailListItem(
                    id = it.id,
                    imageUrl = it.imageUrl,
                    sender = it.sender,
                    subject = it.subject,
                    preview = it.preview ?: "",
                    date = it.date,
                    isFavorite = it.isFavorite,
                    toggleFavorite = {newState ->
                        model.toggleFavorite(it.id, newState)
                    },
                    onClick = { _ -> navController.navigate("emailDetail/${it.id}")}
                )
            }
        }
    }
}