package com.example.locaweb.ui.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun EmptyEventList(onNewEvent: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFAFAFA))
            .padding(vertical = 24.dp)
    ) {
        Text(
            text = "Nenhum evento encontrado",
            modifier = Modifier.padding(top = 32.dp, bottom = 8.dp),
            color = Color(0xFF365366),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Nao existem eventos para essa data. Deseja criar um evento?",
            modifier = Modifier
                .width(226.dp)
                .padding(bottom = 24.dp),
            color = Color(0xFF365366),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onNewEvent,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF0142)
            ),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .height(48.dp)
                .width(232.dp),
        ) {
            Text(
                text = "Novo evento",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}