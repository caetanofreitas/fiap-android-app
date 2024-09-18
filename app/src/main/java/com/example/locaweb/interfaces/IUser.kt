package com.example.locaweb.interfaces;

import java.util.UUID

data class IUserPreferences(
    val isNotReadActive: Boolean? = false,
    val isFavoritesActive: Boolean? = false,
    val isArchivedActive: Boolean? = false,
    val colorMode: String? = "light",
    var markers: Array<String>?,
)

data class IUser(
    val id: String? = UUID.randomUUID().toString(),
    var email: String?,
    var name: String?,
    var profilePicture: String?,
    var password: String? = "",
    var token: String?,
    var userPreferences: IUserPreferences?
)
