package com.example.locaweb.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.locaweb.ui.screens.LoginScreen
import com.example.locaweb.ui.screens.RegisterScreen
import com.example.locaweb.view.models.AccessViewModel
import com.example.locaweb.view.models.Screen
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.locaweb.R

@Composable
fun PrivateBodyContainer(viewModel: AccessViewModel = viewModel()) {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = LocalConfiguration.current.screenHeightDp.dp * 0.05f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.logo_locaweb),
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Navbar {
            TabRow(
                selectedTabIndex = viewModel.currentScreen.ordinal,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(4.dp),
                containerColor = Color.Transparent,
                contentColor = Color(0xFFFF0142),
                indicator = {
                    TabRowDefaults.Indicator(
                        color = Color(0xFFFF0142),
                        modifier = Modifier.tabIndicatorOffset(it[viewModel.currentScreen.ordinal])
                    )
                }
            ) {
                Tab(
                    selected = viewModel.currentScreen == Screen.Login,
                    onClick = { viewModel.showLoginScreen() },
                    modifier = Modifier.background(Color.Transparent)
                ) {
                    Text(text = "Login", modifier = Modifier.padding(16.dp))
                }
                Tab(
                    selected = viewModel.currentScreen == Screen.Register,
                    onClick = { viewModel.showRegisterScreen() },
                    modifier = Modifier.background(Color.Transparent)
                ) {
                    Text(text = "Cadastrar", modifier = Modifier.padding(16.dp))
                }
            }

        }

        when (viewModel.currentScreen) {
            Screen.Login -> LoginScreen(viewModel)
            Screen.Register -> RegisterScreen(viewModel)
        }
    }
}

