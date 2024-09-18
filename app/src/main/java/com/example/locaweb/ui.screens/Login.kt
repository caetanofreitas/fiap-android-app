package com.example.locaweb.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.locaweb.view.models.AccessViewModel
import com.example.locaweb.ui.components.Input

@Composable
fun LoginScreen(viewModel: AccessViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(LocalConfiguration.current.screenWidthDp.dp * 0.05f),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Input(
            value = email,
            onChange = { email = it },
            label = "E-mail",
            placeholder = "E-mail",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Input(
            value = password,
            onChange = { password = it },
            label = "Senha",
            placeholder = "Senha",
            modifier = Modifier.fillMaxWidth(),
            type = KeyboardType.Password,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.doLogin(email, password) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF0142),
                contentColor = Color.White,
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = "Login",
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
            )
        }
    }
}