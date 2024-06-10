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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.locaweb.ui.components.BottomSheetWrapper
import com.example.locaweb.ui.components.DatePickerTextField
import com.example.locaweb.ui.components.Input
import com.example.locaweb.view.models.EmailViewModel
import com.example.locaweb.view.models.FilterState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    showBottomSheet: Boolean = false,
    onDismissRequest: () -> Unit,
    viewModel: EmailViewModel,
    content: @Composable () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val myList by viewModel.markers.collectAsState()
    var tempFilter by remember { mutableStateOf(FilterState()) }

    val closeModal = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest()
            }
        }
    }

    LaunchedEffect(Unit) {
        tempFilter.date = viewModel.filter.value.date
        tempFilter.notReadFilter = viewModel.filter.value.notReadFilter
        tempFilter.withStarFilter = viewModel.filter.value.withStarFilter
        tempFilter.importantFilter = viewModel.filter.value.importantFilter
        tempFilter.archivedFilter = viewModel.filter.value.archivedFilter
        tempFilter.markers = viewModel.filter.value.markers
    }

    content()

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState
        ) {
            BottomSheetWrapper(
                title = "Filtrar",
                onClose = { closeModal() },
                onConfirm = {
                    viewModel.filterList(tempFilter)
                    closeModal()
                },
                confirmText = "Aplicar filtro",
                onCancel =  {
                    tempFilter = FilterState()
                    viewModel.restartState()
                    closeModal()
                },
                cancelText = "Limpar filtro"
            ) {
                Row {
                    DatePickerTextField(
                        label = "Data",
                        placeholder = "Selecione",
                        value = tempFilter.date?.toLocalDate(),
                        onChange = {
                            if (tempFilter.date == null) {
                                tempFilter.date = it.atStartOfDay()
                            }
                            tempFilter.date = tempFilter.date
                                ?.withDayOfMonth(it.dayOfMonth)
                                ?.withMonth(it.monthValue)
                                ?.withYear(it.year)
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
                            myList.forEach {
                                val active = tempFilter.markers.firstOrNull { vl -> vl == it } != null
                                Box(
                                    modifier = Modifier
                                        .background(Color(if (active) 0xFF20313C52 else 0xFFFAFAFA))
                                        .padding(8.dp, 12.dp)
                                        .clickable {
                                            var newMarkers = tempFilter.markers

                                            newMarkers = if (active) {
                                                newMarkers.filter { marker ->
                                                    it != marker
                                                }
                                            } else {
                                                newMarkers.plus(it)
                                            }

                                            tempFilter = tempFilter.copy(markers = newMarkers)
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
                                checked = tempFilter.notReadFilter,
                                onCheckedChange = { tempFilter = tempFilter.copy(notReadFilter = it) },
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
                                checked = tempFilter.withStarFilter,
                                onCheckedChange = { tempFilter = tempFilter.copy(withStarFilter = it) },
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
                                checked = tempFilter.importantFilter,
                                onCheckedChange = { tempFilter = tempFilter.copy(importantFilter = it) },
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
                                checked = tempFilter.archivedFilter,
                                onCheckedChange = { tempFilter = tempFilter.copy(archivedFilter = it) },
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
}