package com.example.locaweb.ui.components.email

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.locaweb.integrations.GetEmailsFilters
import com.example.locaweb.ui.components.BottomSheetWrapper
import com.example.locaweb.ui.components.DatePickerTextField
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    showBottomSheet: Boolean = false,
    onDismissRequest: () -> Unit,
    myList: State<List<String>>,
    filter: StateFlow<GetEmailsFilters>,
    restartState: () -> Unit,
    filterList: (GetEmailsFilters) -> Unit,
    content: @Composable () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    content()
    if (!showBottomSheet) {
        return
    }

    var tempFilter by remember { mutableStateOf(filter.value) }

    val closeModal = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest()
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        BottomSheetWrapper(
            title = "Filtrar",
            onClose = { closeModal() },
            onConfirm = {
                filterList(tempFilter)
                closeModal()
            },
            confirmText = "Aplicar filtro",
            onCancel =  {
                tempFilter = GetEmailsFilters()
                restartState()
                closeModal()
            },
            cancelText = "Limpar filtro"
        ) {
            Row {
                DatePickerTextField(
                    label = "Data",
                    placeholder = "Selecione",
                    value = if (!tempFilter.date.isNullOrBlank()) LocalDateTime.parse(tempFilter.date).toLocalDate() else null,
                    onChange = {
                        if (tempFilter.date.isNullOrBlank()) {
                            tempFilter.date = it.atStartOfDay().toString()
                        }
                        tempFilter.date = LocalDateTime.parse(tempFilter.date ?: "")
                            ?.withDayOfMonth(it.dayOfMonth)
                            ?.withMonth(it.monthValue)
                            ?.withYear(it.year)
                            .toString()
                    }
                )
            }
            Row(modifier = Modifier.padding(top = 32.dp)) {
                Column {
                    Text(
                        text = "Marcadores",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        textAlign = TextAlign.Start,
                        color = Color(0xFF20313C),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        myList.value.forEach {
                            val active = tempFilter.markers?.split(",")?.firstOrNull { vl -> vl == it } != null
                            Box(
                                modifier = Modifier
                                    .background(Color(if (active) 0xFF20313C52 else 0xFFFAFAFA))
                                    .padding(8.dp, 12.dp)
                                    .clickable {
                                        var newMarkers =
                                            tempFilter.markers?.split(",") ?: emptyList()

                                        newMarkers = if (active) {
                                            newMarkers.filter { marker ->
                                                it != marker
                                            }
                                        } else {
                                            newMarkers.plus(it)
                                        }

                                        tempFilter =
                                            tempFilter.copy(markers = newMarkers.joinToString(","))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = it,
                                    color = Color(if (active) 0xFF20313C else 0xFFC2C2C2),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
            Row(modifier = Modifier.padding(top = 20.dp)) {
                Column  {
                    Text(
                        text = "Opções",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        color = Color(0xFF20313C),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.width(241.dp)) {
                            Text(
                                text = "Não Lidas",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                color = Color(0xFF20313C),
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                            )
                            Text(
                                text = "Lorem ipsum dolor sit amet consectetur. Nec elit iaculis nibh sed dolor felis rhoncus. Sed.",
                                textAlign = TextAlign.Start,
                                lineHeight = 15.sp,
                                color = Color(0xFF20313C),
                                fontWeight = FontWeight.Normal,
                                fontSize = 10.sp,
                            )
                        }
                        Switch(
                            checked = tempFilter.read ?: false,
                            onCheckedChange = { tempFilter = tempFilter.copy(read = it) },
                            modifier = Modifier
                                .width(38.dp)
                                .height(20.dp)
                                .padding(end = 24.dp),
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = Color(0xFF20313C),
                                uncheckedTrackColor = Color(0xFFF0F0F0),
                                uncheckedThumbColor = Color.White,
                                uncheckedBorderColor = Color.Transparent
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.width(241.dp)) {
                            Text(
                                text = "Com estrela",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                color = Color(0xFF20313C),
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                            )
                            Text(
                                text = "Lorem ipsum dolor sit amet consectetur. Nec elit iaculis nibh sed dolor felis rhoncus. Sed.",
                                textAlign = TextAlign.Start,
                                lineHeight = 15.sp,
                                color = Color(0xFF20313C),
                                fontWeight = FontWeight.Normal,
                                fontSize = 10.sp,
                            )
                        }
                        Switch(
                            checked = tempFilter.favorite ?: false,
                            onCheckedChange = { tempFilter = tempFilter.copy(favorite = it) },
                            modifier = Modifier
                                .width(38.dp)
                                .height(20.dp)
                                .padding(end = 24.dp),
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = Color(0xFF20313C),
                                uncheckedTrackColor = Color(0xFFF0F0F0),
                                uncheckedThumbColor = Color.White,
                                uncheckedBorderColor = Color.Transparent
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.width(241.dp)) {
                            Text(
                                text = "Importantes",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                color = Color(0xFF20313C),
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                            )
                            Text(
                                text = "Lorem ipsum dolor sit amet consectetur. Nec elit iaculis nibh sed dolor felis rhoncus. Sed.",
                                textAlign = TextAlign.Start,
                                lineHeight = 15.sp,
                                color = Color(0xFF20313C),
                                fontWeight = FontWeight.Normal,
                                fontSize = 10.sp,
                            )
                        }
                        Switch(
                            checked = tempFilter.importants ?: false,
                            onCheckedChange = { tempFilter = tempFilter.copy(importants = it) },
                            modifier = Modifier
                                .width(38.dp)
                                .height(20.dp)
                                .padding(end = 24.dp),
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = Color(0xFF20313C),
                                uncheckedTrackColor = Color(0xFFF0F0F0),
                                uncheckedThumbColor = Color.White,
                                uncheckedBorderColor = Color.Transparent
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.width(241.dp)) {
                            Text(
                                text = "Arquivados",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                color = Color(0xFF20313C),
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                            )
                            Text(
                                text = "Lorem ipsum dolor sit amet consectetur. Nec elit iaculis nibh sed dolor felis rhoncus. Sed.",
                                textAlign = TextAlign.Start,
                                lineHeight = 15.sp,
                                color = Color(0xFF20313C),
                                fontWeight = FontWeight.Normal,
                                fontSize = 10.sp,
                            )
                        }
                        Switch(
                            checked = tempFilter.archived ?: false,
                            onCheckedChange = { tempFilter = tempFilter.copy(archived = it) },
                            modifier = Modifier
                                .width(38.dp)
                                .height(20.dp)
                                .padding(end = 24.dp),
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = Color(0xFF20313C),
                                uncheckedTrackColor = Color(0xFFF0F0F0),
                                uncheckedThumbColor = Color.White,
                                uncheckedBorderColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    }
}