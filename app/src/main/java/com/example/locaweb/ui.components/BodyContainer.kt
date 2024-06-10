package com.example.locaweb.ui.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.locaweb.R
import com.example.locaweb.interfaces.IEmail
import com.example.locaweb.ui.screens.CalendarScreen
import com.example.locaweb.ui.screens.EmailDetailScreen
import com.example.locaweb.ui.screens.HomeScreen
import com.example.locaweb.ui.screens.NewEmailScreen
import com.example.locaweb.view.models.CalendarViewModel
import com.example.locaweb.view.models.EmailViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyContainer(
    calendarViewModel: CalendarViewModel,
    emailViewModel: EmailViewModel = viewModel(),
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var activeScreen by remember { mutableStateOf("home") }
    val navController = rememberNavController()

    val handleCloseModal = {
        if (sheetState.isVisible) {scope.launch { sheetState.hide() }}
    }

    val onNavigateToHome = {
        handleCloseModal()
        navController.navigate("home")
    }

    val onNavigateToCalendar = {
        handleCloseModal()
        navController.navigate("calendar")
    }

    val onNavigateToAddScreen = {
        scope.launch { sheetState.show() }
        activeScreen = "write"
    }

    val goBackToHome = {
        navController.navigateUp()
    }

    Scaffold(
        floatingActionButton = {
            if (activeScreen != "emailDetail") {
                Navbar(
                    tonalElevation = 8.dp,
                ) {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(240, 240, 240, 0xE6))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(
                            onClick = onNavigateToHome,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (activeScreen == "home") Color(0xFFFFFFFF) else Color.Transparent)
                                .padding(16.dp, 0.dp),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_inbox),
                                contentDescription = "Tela 1",
                                modifier = Modifier.size(24.dp),
                                tint = if (activeScreen == "home") Color(0xFFFF0142) else Color(0xFF20313C)
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        IconButton(
                            onClick = onNavigateToAddScreen,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (activeScreen == "write") Color(0xFFFFFFFF) else Color.Transparent)
                                .padding(16.dp, 0.dp),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_pencil),
                                contentDescription = "Tela 1",
                                modifier = Modifier.size(24.dp),
                                tint = if (activeScreen == "write") Color(0xFFFF0142) else Color(0xFF20313C)
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        IconButton(
                            onClick = onNavigateToCalendar,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (activeScreen == "calendar") Color(0xFFFFFFFF) else Color.Transparent)
                                .padding(16.dp, 0.dp),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_calendar),
                                contentDescription = "Tela 2",
                                modifier = Modifier.size(24.dp),
                                tint = if (activeScreen == "calendar") Color(0xFFFF0142) else Color(0xFF20313C)
                            )
                        }
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {_ ->
        Surface(modifier = Modifier
            .padding(24.dp, 16.dp, 24.dp, 0.dp)
            .fillMaxWidth(), color = MaterialTheme.colorScheme.background) {
            NavHost(navController = navController, startDestination =  "home") {
                composable(route = "home") {
                    activeScreen = "home"
                    HomeScreen(navController, emailViewModel)
                }

                composable(
                    route = "emailDetail/{emailId}",
                    arguments = listOf(navArgument("emailId") { type = NavType.StringType })
                ) {
                    activeScreen = "emailDetail"
                    val element = emailViewModel.findById(it.arguments?.getString("emailId") ?: "")
                    val fallback = IEmail(
                        date = LocalDateTime.now(),
                        imageUrl = null,
                        isFavorite = false,
                        sender = "E-mail not found",
                        subject = "E-mail not found"
                    )
                    EmailDetailScreen({ goBackToHome() }, element ?: fallback)
                }

                composable(route = "calendar") {
                    activeScreen = "calendar"
                    CalendarScreen(calendarViewModel)
                }
            }
        }
    }

    if (sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { scope.launch { sheetState.hide() } },
            shape = BottomSheetDefaults.ExpandedShape
        ) {
            NewEmailScreen(handleCloseModal)
        }
    }
}


@Composable
fun Navbar(
    modifier: Modifier = Modifier,
    tonalElevation: Dp = BottomAppBarDefaults.ContainerElevation,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = Color.Transparent,
        tonalElevation = tonalElevation,
        modifier = modifier
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}
