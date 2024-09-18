package com.example.locaweb.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getStringOrNull
import com.example.locaweb.interfaces.IEmail
import com.example.locaweb.interfaces.IUser
import java.time.LocalDateTime

class EmailDbHandler(context: Context?) : BaseDBHandler(context, DB_NAME, DB_VERSION) {
    companion object {
        private const val DB_NAME = "emailsdb"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "myemails"
        private const val ID_COL = "id"
        private const val IMAGE_URL_COL = "image_url"
        private const val SENDER_COL = "sender"
        private const val SUBJECT_COL = "subject"
        private const val DATE_COL = "date"
        private const val CONTENT_COL = "content"
        private const val PREVIEW_COL = "preview"
        private const val IS_FAVORITE_COL = "is_favorite"
    }

    override fun onCreateTables(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " TEXT PRIMARY KEY, "
                + IMAGE_URL_COL + " TEXT,"
                + SENDER_COL + " TEXT,"
                + SUBJECT_COL + " TEXT,"
                + DATE_COL + " TEXT,"
                + CONTENT_COL + " TEXT,"
                + PREVIEW_COL + " TEXT,"
                + IS_FAVORITE_COL + " INTEGER DEFAULT 0)")

        db.execSQL(query)
    }

    override fun onUpgradeTables(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addNewEmail(item: IEmail) {
        val values = ContentValues()
        values.put(ID_COL, item.id)
        values.put(IMAGE_URL_COL, item.imageUrl)
        values.put(SENDER_COL, item.sender)
        values.put(SUBJECT_COL, item.subject)
        values.put(DATE_COL, item.date.toString())
        values.put(CONTENT_COL, item.content)
        values.put(PREVIEW_COL, item.preview)
        values.put(IS_FAVORITE_COL, if (item.isFavorite) 1 else 0)
        this.insert(TABLE_NAME, values)
    }

    fun updateEmail(item: IEmail) {
        val values = ContentValues()
        values.put(ID_COL, item.id)
        values.put(IMAGE_URL_COL, item.imageUrl)
        values.put(SENDER_COL, item.sender)
        values.put(SUBJECT_COL, item.subject)
        values.put(DATE_COL, item.date.toString())
        values.put(CONTENT_COL, item.content)
        values.put(PREVIEW_COL, item.preview)
        values.put(IS_FAVORITE_COL, if (item.isFavorite) 1 else 0)
        val args: Array<String> = arrayOf(item.id)
        this.update(TABLE_NAME, values, "id = ?", args)
    }

    fun readEmails(args: Array<String>? = null): ArrayList<IEmail> {
        val emailsCursor: Cursor? = this.query(
            tableName = TABLE_NAME,
            selectionArgs =  args,
        )
        val emailModelArrayList: ArrayList<IEmail> = ArrayList()

        if (emailsCursor != null && emailsCursor.moveToNext()) {
            do {
                val item = IEmail(
                    id = emailsCursor.getString(0),
                    imageUrl = emailsCursor.getStringOrNull(1),
                    sender = emailsCursor.getString(2),
                    subject = emailsCursor.getString(3),
                    date = LocalDateTime.parse(emailsCursor.getString(4)),
                    content = emailsCursor.getStringOrNull(5),
                    preview = emailsCursor.getStringOrNull(6),
                    isFavorite = emailsCursor.getInt(7) == 1,
                )
                emailModelArrayList.add(item)
            } while (emailsCursor.moveToNext())
        }
        emailsCursor?.close()
        return emailModelArrayList
    }

    fun clearEmails() {
        this.delete(tableName = TABLE_NAME, whereClause = "", whereArgs = Array(0) { "" })
    }
}

class UserDbHandler(context: Context?) : BaseDBHandler(context, DB_NAME, DB_VERSION) {
    companion object {
        private const val DB_NAME = "userdb"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "userinfo"
        private const val ID_COL = "id"
        private const val PROFILE_URL_COL = "profile_picture"
        private const val EMAIL_COL = "email"
        private const val NAME_COL = "name"
        private const val TOKEN_COL = "token"
    }

    override fun onCreateTables(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " TEXT PRIMARY KEY, "
                + PROFILE_URL_COL + " TEXT,"
                + EMAIL_COL + " TEXT,"
                + NAME_COL + " TEXT,"
                + TOKEN_COL + " TEXT)")

        db.execSQL(query)
    }

    override fun onUpgradeTables(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun checkLoggedUser(): IUser? {
        val cursor = this.query(tableName = TABLE_NAME)
        if (cursor != null && cursor.moveToNext()) {
            val user = IUser(
                id = cursor.getStringOrNull(0),
                profilePicture = cursor.getStringOrNull(1),
                email = cursor.getStringOrNull(2),
                name = cursor.getStringOrNull(3),
                token = cursor.getStringOrNull(4),
                userPreferences = null,
            )
            cursor.close()
            return user
        }
        cursor?.close()
        return null
    }

    fun addNewUser(item: IUser) {
        val values = ContentValues()
        values.put(ID_COL, item.id)
        values.put(PROFILE_URL_COL, item.profilePicture)
        values.put(EMAIL_COL, item.email)
        values.put(NAME_COL, item.name)
        values.put(TOKEN_COL, item.token)
        this.insert(TABLE_NAME, values)
    }

    fun clearUserInfo() {
        this.delete(tableName = TABLE_NAME, whereClause = "", whereArgs = Array(0) { "" })
    }
}
