package com.example.locaweb

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.locaweb.ui.components.BodyContainer
import com.example.locaweb.ui.theme.LocawebTheme
import com.example.locaweb.view.models.CalendarViewModel
import com.example.locaweb.view.models.CalendarViewModelFactory
import com.example.locaweb.view.models.EmailViewModel

class MainActivity : ComponentActivity() {
    private val calendarViewModel: CalendarViewModel by viewModels {
        CalendarViewModelFactory(contentResolver)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val readCalendarPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED
        val writeCalendarPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED

        if (!readCalendarPermission || !writeCalendarPermission) {
            requestCalendarPermissions()
        } else {
            startLocalCalendar()
        }


        setContent {
            LocawebTheme(darkTheme = false) {
                BodyContainer(
                    calendarViewModel = calendarViewModel,
                    emailViewModel = EmailViewModel(LocalContext.current)
                )
            }
        }
    }

    private fun requestCalendarPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
        )
        requestCalendarPermissionLauncher.launch(permissions)
    }

    private val requestCalendarPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.READ_CALENDAR] == true &&
                permissions[Manifest.permission.WRITE_CALENDAR] == true) {
                startLocalCalendar()
            } else {
                // Permissões negadas, você pode lidar com isso de acordo, por exemplo, exibindo uma mensagem ao usuário
            }
        }

    private fun startLocalCalendar() {
        calendarViewModel.loadData()
    }
}