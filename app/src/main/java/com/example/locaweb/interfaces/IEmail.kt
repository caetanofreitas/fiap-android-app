package com.example.locaweb.interfaces

import java.time.LocalDateTime
import java.util.UUID

data class IEmail(
    val id: String = UUID.randomUUID().toString(),
    val imageUrl: String?,
    val sender: String,
    val subject: String,
    val date: LocalDateTime,
    val content: String? = null,
    val preview: String? = null,
    var isFavorite: Boolean,
    val markers: Array<String>? = emptyArray(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IEmail

        if (markers != null) {
            if (other.markers == null) return false
            if (!markers.contentEquals(other.markers)) return false
        } else if (other.markers != null) return false

        return true
    }

    override fun hashCode(): Int {
        return markers?.contentHashCode() ?: 0
    }
}