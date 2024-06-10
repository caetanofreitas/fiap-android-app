package com.example.locaweb.ui.components.email

import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle as TS
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import java.time.LocalDateTime
import com.example.locaweb.R
import com.example.locaweb.utils.formatLocalDate
import com.example.locaweb.utils.randomColor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EmailListItem(
    id: String,
    imageUrl: String?,
    sender: String,
    subject: String,
    preview: String,
    date: LocalDateTime,
    isFavorite: Boolean,
    toggleFavorite: (Boolean) -> Unit,
    onClick: (String) -> Unit,
    ) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 16.dp)
            .height(80.dp)
            .clickable(onClick = { onClick(id) }),
    ) {
        if (imageUrl != "" && imageUrl != null) {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(imageUrl)
                    .size(Size.ORIGINAL)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
            )
        } else {
            Box(modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
                .background(randomColor()))
        }
        Column(
            modifier = Modifier
                .width(190.dp)
                .padding(8.dp, 0.dp)
        ) {
            Text(
                text = sender,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TS(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF365366),
                ),
            )
            Text(
                text = subject,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(0.dp, 2.dp),
                style = TS(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF162129)
                )
            )
            Text(
                text = preview,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = TS(
                    fontSize = 10.sp,
                    color = Color(0xFF2C4352)
                )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp, 0.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = formatLocalDate(date),
                style = TS(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFC2C2C2),
                )
            )
            IconButton(
                onClick = { toggleFavorite(!isFavorite) },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_star),
                    contentDescription = null,
                    tint = if (isFavorite) Color(0xFFFF9901) else Color(0xFFC2C2C2),
                )
            }
        }
    }
    Divider(
        modifier = Modifier
            .height(2.pxToDp())
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFF0F0F0),
                        Color(0xFFFFFFFF)
                    )
                )
            ),
    )
}

fun Int.pxToDp(): Dp {
    val density = Resources.getSystem().displayMetrics.density
    return (this / density).dp
}
