package com.example.locaweb.integrations

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String
)

data class UserPreferences(
    val is_not_read_active: Boolean,
    val is_favorites_active: Boolean,
    val is_archived_active: Boolean,
    val color_mode: String,
    val markers: Array<String>,
)

data class UserInfoResponse(
    val email: String,
    val name: String,
    val profile_picture: String?,
    val preferences: UserPreferences?,
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val profile_picture: String?
)
