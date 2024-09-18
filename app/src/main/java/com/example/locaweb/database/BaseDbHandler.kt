package com.example.locaweb.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

abstract class BaseDBHandler(
    context: Context?,
    dbName: String,
    dbVersion: Int
) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
    abstract fun onCreateTables(db: SQLiteDatabase)

    abstract fun onUpgradeTables(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)

    override fun onCreate(db: SQLiteDatabase) {
        onCreateTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgradeTables(db, oldVersion, newVersion)
    }

    fun insert(tableName: String, contentValues: ContentValues) {
        writableDatabase.use { db ->
            db.insert(tableName, null, contentValues)
        }
    }

    fun delete(tableName: String, whereClause: String, whereArgs: Array<String>) {
        writableDatabase.use { db ->
            db.delete(tableName, whereClause, whereArgs)
        }
    }

    fun update(
        tableName: String,
        contentValues: ContentValues,
        whereClause: String,
        whereArgs: Array<String>
    ) {
        writableDatabase.use { db ->
            db.update(tableName, contentValues, whereClause, whereArgs)
        }
    }

    fun query(
        tableName: String,
        columns: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        groupBy: String? = null,
        having: String? = null,
        orderBy: String? = null
    ): Cursor? {
        return readableDatabase.query(
            tableName,
            columns,
            selection,
            selectionArgs,
            groupBy,
            having,
            orderBy
        )
    }
}