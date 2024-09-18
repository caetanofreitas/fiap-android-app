package com.example.locaweb

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.locaweb.integrations.Requester
import com.example.locaweb.ui.components.BodyContainer
import com.example.locaweb.ui.components.PrivateBodyContainer
import com.example.locaweb.ui.theme.LocawebTheme
import com.example.locaweb.view.models.AccessViewModel
import com.example.locaweb.view.models.AccessViewModelFactory
import com.example.locaweb.view.models.CalendarViewModel
import com.example.locaweb.view.models.CalendarViewModelFactory
import com.example.locaweb.view.models.EmailViewModel

class MainActivity : ComponentActivity() {
    private val accessViewModel: AccessViewModel by viewModels {
        AccessViewModelFactory(contentResolver, Requester.apiService, applicationContext)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Requester.init(applicationContext)

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
        }


        setContent {
            LocawebTheme(darkTheme = false) {
                if (accessViewModel.isLoggedIn.collectAsState().value) {
                    val emailViewModel = EmailViewModel(applicationContext, Requester.apiService, accessViewModel)
                    val calendarViewModel: CalendarViewModel by viewModels {
                        CalendarViewModelFactory(contentResolver)
                    }
                    calendarViewModel.loadData()
                    BodyContainer(
                        calendarViewModel = calendarViewModel,
                        viewModel = accessViewModel,
                        emailViewModel = emailViewModel
                    )
                } else {
                    PrivateBodyContainer(
                        viewModel = accessViewModel,
                    )
                }
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
            } else {
                // Permissões negadas, você pode lidar com isso de acordo, por exemplo, exibindo uma mensagem ao usuário
            }
        }
}