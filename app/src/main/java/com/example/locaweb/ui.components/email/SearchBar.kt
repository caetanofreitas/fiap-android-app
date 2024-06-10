package com.example.locaweb.ui.components.email

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.locaweb.R

@Composable
fun SearchBar(value: String, onValueChange: (String) -> Unit) {
    TextField(
        modifier = Modifier.fillMaxWidth().clip(CircleShape),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFFAFAFA),
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedPlaceholderColor = Color(0xFFC2C2C2),
            focusedContainerColor = Color(0xFFFAFAFA),
            focusedIndicatorColor = Color.Transparent,
            focusedPlaceholderColor = Color(0xFFC2C2C2),
        ),
        leadingIcon = {
            Image(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(start = 12.dp, end = 8.dp)
            )
        },
        singleLine = true,
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = "Pesquisar por e-mail") },
        label = null,
    )
}