package com.example.locaweb.ui.components.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.locaweb.utils.formatLocalDate
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventListHeader(isListEmpty: Boolean, actualDate: LocalDate?, onNewEvent: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalArrangement = if (isListEmpty) Arrangement.Center else Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            horizontalAlignment = if (isListEmpty) Alignment.CenterHorizontally else Alignment.Start
        ) {
            Text(
                text = "Eventos",
                color = Color(0xFF162129),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
            )
            if (actualDate != null) Text(
                text = formatLocalDate(actualDate, true),
                color = Color(0xFF365366),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
            )
        }
        if (!isListEmpty) {
            OutlinedIconButton(
                onClick = onNewEvent,
                shape = RoundedCornerShape(4.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xFFFF0142),
                    contentColor = Color.White,
                ),
                modifier = Modifier.size(48.dp),
                border = IconButtonDefaults.outlinedIconButtonBorder(false)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "new event",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}