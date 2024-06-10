package com.example.locaweb.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.locaweb.ui.components.BottomSheetWrapper
import com.example.locaweb.ui.components.Input

@Composable
fun NewEmailScreen(onClose: () -> Unit) {
   BottomSheetWrapper(
       title = "Nova Mensagem",
       onClose = { onClose() },
       onConfirm = { onClose() },
       confirmText = "Enviar",
       onCancel = { onClose() },
       cancelText = "Cancelar"
   ) {
       Row(modifier = Modifier.fillMaxWidth()) {
            Input(
                label = "Para",
                placeholder = "@gmail.com"
            )
       }
       Row(
           modifier = Modifier
               .padding(vertical = 24.dp)
               .fillMaxWidth()
       ) {
           Input(
               label = "Cc/Cco",
               placeholder = "@gmail.com"
           )
       }
       Row(modifier = Modifier.fillMaxWidth()) {
           Input(
               label = "Assunto",
               placeholder = "Escreva sua mensagem...",
               singleLine = false
           )
       }
       Row(
           modifier = Modifier.fillMaxWidth()
       ) {

       }
   }
}