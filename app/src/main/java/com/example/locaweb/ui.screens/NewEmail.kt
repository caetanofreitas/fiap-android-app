package com.example.locaweb.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.locaweb.integrations.SendEmailBody
import com.example.locaweb.ui.components.BottomSheetWrapper
import com.example.locaweb.ui.components.Input
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NewEmailScreen(onClose: () -> Unit, onConfirm: (body: SendEmailBody) -> Unit) {
    var to by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var cc by remember { mutableStateOf("") }

   BottomSheetWrapper(
       title = "Nova Mensagem",
       onClose = { onClose() },
       onConfirm = {
           val zonedDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"))
           val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
           onConfirm(SendEmailBody(
               to = to,
               subject = subject,
               sendDate = zonedDateTime.format(formatter),
               content = content,
               cc = cc.split(",").toTypedArray(),
           ))
       },
       confirmText = "Enviar",
       onCancel = { onClose() },
       cancelText = "Cancelar"
   ) {
       Row(modifier = Modifier.fillMaxWidth()) {
            Input(
                label = "Para",
                placeholder = "@gmail.com",
                value = to,
            ) {
                to = it
            }
       }
       Row(
           modifier = Modifier
               .padding(vertical = 12.dp)
               .fillMaxWidth()
       ) {
           Input(
               label = "Assunto",
               placeholder = "Assunto",
               value = subject,
           ) {
               subject = it
           }
       }
       Row(modifier = Modifier.fillMaxWidth()) {
           Input(
               label = "Conteúdo",
               placeholder = "Escreva sua mensagem...",
               singleLine = false,
               value = content,
           ) {
               content = it
           }
       }
   }
}