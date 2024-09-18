package com.example.locaweb.ui.components.email

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.locaweb.R
import com.example.locaweb.integrations.GetEmailsFilters
import com.example.locaweb.utils.Debouncer
import com.example.locaweb.view.models.EmailViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun Header(
    profileImage: String,
    searchFilter: (String) -> Unit,
    markersList: State<List<String>>,
    filterEmails: StateFlow<GetEmailsFilters>,
    restartEmailFilter: () -> Unit,
    filterEmailList: (GetEmailsFilters) -> Unit,
    goToProfile: () -> Unit,
) {
    var text by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    val debounceDispatcher = Debouncer(300)

    fun handleInputChangeText(newText: String) {
        text = newText
        debounceDispatcher.debounce { searchFilter(newText) }
    }

    FilterBottomSheet(
        showBottomSheet = showBottomSheet,
        onDismissRequest = {
            showBottomSheet = false
        },
        myList = markersList,
        filter = filterEmails,
        restartState = restartEmailFilter,
        filterList = filterEmailList,
    ) {
        Column(verticalArrangement = Arrangement.Top) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(0f),
                    modifier = Modifier.size(36.dp),
                    onClick = { goToProfile() }
                ) {
                    AsyncImage(
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(profileImage)
                            .size(Size.ORIGINAL)
                            .build(),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(36.dp)
                    )
                }
                Image(
                    painter = painterResource(R.drawable.logo_locaweb),
                    contentDescription = null,
                    modifier = Modifier.size(height = 30.dp, width = 139.dp)
                )
                Button(
                    onClick = {
                        showBottomSheet = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(0f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_filter_list),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            SearchBar(
                value = text,
                onValueChange = { newText -> handleInputChangeText(newText) },
            )
        }
    }
}
