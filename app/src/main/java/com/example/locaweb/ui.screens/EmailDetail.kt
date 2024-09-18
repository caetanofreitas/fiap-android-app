package com.example.locaweb.ui.screens

import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.locaweb.R
import com.example.locaweb.interfaces.IEmail
import com.example.locaweb.utils.formatLocalDate
import com.example.locaweb.utils.randomColor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EmailDetailScreen(goBack: () -> Unit, content: IEmail) {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(state = rememberScrollState())) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { goBack() }) {
                Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = "Go Back")
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(painter = painterResource(R.drawable.ic_archive), contentDescription = "archive")
                }
                IconButton(onClick = {}) {
                    Icon(painter = painterResource(R.drawable.ic_delete), contentDescription = "delete")
                }
                IconButton(onClick = {}) {
                    Icon(painter = painterResource(R.drawable.ic_answer), contentDescription = "answer")
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row {
                if (content.imageUrl != "" && content.imageUrl != null) {
                    AsyncImage(
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(content.imageUrl)
                            .size(Size.ORIGINAL)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(56.dp)
                    )
                } else {
                    Box(modifier = Modifier
                        .clip(CircleShape)
                        .size(56.dp)
                        .background(randomColor()))
                }
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 32.dp)
                        .width(200.dp),
                ) {
                    Text(
                        text = content.subject,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF162129),
                    )
                    Text(
                        text = "De: ${content.sender}",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF365366),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
            }
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = formatLocalDate(content.date),
                    fontSize = 10.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFC2C2C2),
                )
            }
        }
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        if (!content.content.isNullOrBlank()) {
            AndroidView(
                factory = { TextView(it) },
                update = { it.text = HtmlCompat.fromHtml(content.content ?: "", 0) },
            )
        }
    }
}

