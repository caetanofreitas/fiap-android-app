package com.example.locaweb.integrations

import com.example.locaweb.interfaces.IEmail
import java.time.LocalDateTime

data class Sender (
    val name: String,
    val profile_picture: String
    )

data class EmailContent (
    val id: String,
    val subject: String,
    val preview: String,
)

data class EmailResponse (
    val id: String,
    val send_date: String,
    val markers: Array<String>,
    val read: Boolean,
    val favorite: Boolean,
    val archived: Boolean,
    val sender_id: Sender,
    val content: EmailContent,
)

data class EmailDetailContent(
    val content: String,
    val subject: String
)

data class EmailDetailResponse(
    val id: String,
    val send_date: String,
    val markers: Array<String>,
    val favorite: Boolean,
    val archived: Boolean,
    val content: EmailDetailContent,
)

data class GetEmailsResponse(
    val limit: Number,
    val page: Number,
    val pages: Number,
    val items: Array<EmailResponse>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GetEmailsResponse

        if (limit != other.limit) return false
        if (page != other.page) return false
        if (pages != other.pages) return false
        return items.contentEquals(other.items)
    }

    override fun hashCode(): Int {
        var result = limit.hashCode()
        result = 31 * result + page.hashCode()
        result = 31 * result + pages.hashCode()
        result = 31 * result + items.contentHashCode()
        return result
    }
}

data class GetEmailsFilters(
    var date: String? = null,
    var markers: String? = null,
    var read: Boolean? = null,
    var favorite: Boolean? = null,
    var importants: Boolean? = null,
    var archived: Boolean? = null,
    var search: String? = null,
    var page: Number? = null,
    var limit: Number? = null
)

data class SendEmailBody(
    var to: String = "",
    var subject: String = "",
    var sendDate: String = "",
    var content: String =  "",
    var cc: Array<String> = emptyArray(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SendEmailBody

        return cc.contentEquals(other.cc)
    }

    override fun hashCode(): Int {
        return cc.contentHashCode()
    }
}