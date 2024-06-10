package com.example.locaweb.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.database.getStringOrNull
import com.example.locaweb.interfaces.IEmail
import java.time.LocalDateTime

class DBHandler(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
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

    fun addNewEmail(item: IEmail) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID_COL, item.id)
        values.put(IMAGE_URL_COL, item.imageUrl)
        values.put(SENDER_COL, item.sender)
        values.put(SUBJECT_COL, item.subject)
        values.put(DATE_COL, item.date.toString())
        values.put(CONTENT_COL, item.content)
        values.put(PREVIEW_COL, item.preview)
        values.put(IS_FAVORITE_COL, if (item.isFavorite) 1 else 0)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateEmail(item: IEmail) {
        val db = this.writableDatabase
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
        db.update(TABLE_NAME, values, "id = ?", args)
        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

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

    fun readEmails(args: Array<String>? = null): ArrayList<IEmail> {
        val db = this.readableDatabase
        val emailsCursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", args)
        val emailModelArrayList: ArrayList<IEmail> = ArrayList()

        if (emailsCursor.moveToFirst()) {
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
        emailsCursor.close()
        return emailModelArrayList
    }
}