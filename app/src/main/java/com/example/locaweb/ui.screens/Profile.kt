package com.example.locaweb.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.locaweb.R
import com.example.locaweb.interfaces.IUser
import com.example.locaweb.ui.components.Input

@Composable
fun ProfileScreen(
    navController: NavController,
    user: IUser,
    doLogout: () -> Unit,
    markers: List<String>,
    addToList: (String) -> Unit,
    removeFromList: (String) -> Unit,
) {
    var newItem by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    navController.navigateUp()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(0f),
                modifier = Modifier.size(24.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
            Button(
                onClick = {
                    doLogout()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(0f),
                modifier = Modifier.size(24.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_answer),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .graphicsLayer {
                            scaleX = -1f
                        },
                    colorFilter = ColorFilter.tint(Color.Red)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(vertical = 18.dp)
                .fillMaxWidth(),
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(user.profilePicture)
                    .size(Size.ORIGINAL)
                    .build(),
                contentDescription = "Profile",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(120.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = user.name ?: "",
                    modifier = Modifier.padding(bottom = 8.dp),
                    textAlign = TextAlign.Start,
                    color = Color(0xFF20313C),
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                )
                Text(
                    text = user.email ?: "",
                    modifier = Modifier.padding(bottom = 12.dp),
                    textAlign = TextAlign.Start,
                    color = Color(0xFF40484d),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                )
            }
        }
        Input(
            label = "Marcadores",
            placeholder = "Adicionar novo marcador",
            value = newItem,
            onChange = { newItem = it },
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Button(
            modifier = Modifier.padding(bottom = 16.dp),
            onClick = {
            if (newItem.isNotBlank()) {
                addToList(newItem)
                newItem = ""
            }
        }) {
            Text("Adicionar Novo Marcador")
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(markers) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = item, modifier = Modifier.weight(1f))
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(0f),
                        modifier = Modifier.size(16.dp),
                        onClick = { removeFromList(item) }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}