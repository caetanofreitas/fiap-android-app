package com.example.locaweb.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.locaweb.utils.MaskVisualTransformation

@Composable
fun Input(
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String,
    singleLine: Boolean = true,
    maxHeight: Int? = null,
    mask: String? = null,
    type: KeyboardType? = KeyboardType.Text,
    value: String = "",
    onChange: ((String) -> Unit) = {},
) {
    var transformation: VisualTransformation = VisualTransformation.None
    if (type != null && type.equals(KeyboardType.Password)) {
        transformation = PasswordVisualTransformation()
    } else if (!mask.isNullOrBlank()) {
        transformation = MaskVisualTransformation(mask)
    }

    Column(modifier = modifier) {
        if (label.isNotBlank()) {
            Text(
                text = label,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                textAlign = TextAlign.Start,
                color = Color(0xFF20313C),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
            )
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (singleLine) 55.dp else (maxHeight?.dp ?: 150.dp)),
            shape = RoundedCornerShape(4.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFFAFAFA),
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedPlaceholderColor = Color(0xFFC2C2C2),
                focusedContainerColor = Color(0xFFFAFAFA),
                focusedIndicatorColor = Color.Transparent,
                focusedPlaceholderColor = Color(0xFFC2C2C2),
            ),
            singleLine = singleLine,
            value = value,
            onValueChange = { newText: String -> onChange(newText) },
            label = null,
            visualTransformation = transformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = type ?: KeyboardType.Text,
            ),
            placeholder = {
                if (label != "") {
                    Text(text = placeholder)
                }
            },
        )
    }
}