package com.example.locaweb.view.models

import android.content.ContentResolver
import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import com.example.locaweb.interfaces.IUser
import androidx.lifecycle.viewModelScope
import com.example.locaweb.database.UserDbHandler
import com.example.locaweb.integrations.ApiService
import com.example.locaweb.integrations.LoginRequest
import com.example.locaweb.integrations.RegisterRequest
import com.example.locaweb.interfaces.IUserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.Exception

enum class Screen {
    Login, Register
}

class AccessViewModel(
    private val apiService: ApiService,
    private val context: Context,
): ViewModel() {
    var currentScreen by mutableStateOf(Screen.Login)
    private val dbHandler: UserDbHandler = UserDbHandler(context)

    private val _loggedUser = MutableStateFlow<IUser?>(null)
    val loggedUser = _loggedUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val user = dbHandler.checkLoggedUser()
            if (user != null) {
                _loggedUser.value = user
                getLoggedUserInfo()
                showLoggedInState()
            } else {
                _loggedUser.value = null
                showLoginScreen()
            }
        }
    }

    suspend fun verifyUserInfo(): IUser? {
        val userResponse = apiService.userInfo()
        if (userResponse.isSuccessful) {
            val body = userResponse.body()
            var preferences: IUserPreferences? = null

            if (body?.preferences != null) {
                preferences = IUserPreferences(
                    isNotReadActive = body.preferences.is_not_read_active,
                    isFavoritesActive = body.preferences.is_favorites_active,
                    isArchivedActive = body.preferences.is_archived_active,
                    colorMode = body.preferences.color_mode,
                    markers = body.preferences.markers,
                )
            }

            _loggedUser.value = IUser(
                email = body?.email,
                name = body?.name,
                profilePicture = body?.profile_picture,
                token = getToken(),
                userPreferences = preferences,
            )
            _isLoggedIn.value = true
            dbHandler.addNewUser(_loggedUser.value!!)
            return _loggedUser.value
        }

        doLogout()
        _loggedUser.value = null
        return _loggedUser.value
    }

    fun getLoggedUserInfo(): IUser? {
        viewModelScope.launch {
            if (isUserComplete()) {
                return@launch
            }

            verifyUserInfo()
            return@launch
        }
        return _loggedUser.value
    }

    fun isUserComplete(): Boolean {
        return _loggedUser.value != null && _loggedUser.value!!.userPreferences != null
    }

    fun doLogin(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = apiService.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    saveToken(response.body()?.token ?: "")
                    getLoggedUserInfo()
                    showLoggedInState()
                } else {
                    doLogout()
                }
            } catch (e: Exception) {
                doLogout()
            }
        }
    }

    fun doRegister(name: String, email: String, password: String) {
        // TODO: IMAGE UPLOAD
        viewModelScope.launch {
            try {
                val response = apiService.register(RegisterRequest(email, password, name, "https://picsum.photos/1080"))
                if (response.isSuccessful) {
                    saveToken(response.body()?.token ?: "")
                    getLoggedUserInfo()
                    showLoggedInState()
                } else {
                    doLogout()
                }
            } catch (e: Exception) {
                doLogout()
            }
        }
    }

    fun doLogout() {
        viewModelScope.launch {
            dbHandler.clearUserInfo()
            _isLoggedIn.value = false
            _loggedUser.value = null
            showLoginScreen()
            removeToken()
            apiService.logout()
        }
    }

    private fun saveToken(token: String) {
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    private fun getToken(): String? {
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }

    private fun removeToken() {
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("auth_token").apply()
    }

    private fun showLoggedInState() {
        _isLoggedIn.value = true
    }

    fun showRegisterScreen() {
        currentScreen = Screen.Register
    }

    fun showLoginScreen() {
        currentScreen = Screen.Login
    }
}

class AccessViewModelFactory(private val contentResolver: ContentResolver, private val req: ApiService, val ctx: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccessViewModel::class.java)) {
            return AccessViewModel(req, ctx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}