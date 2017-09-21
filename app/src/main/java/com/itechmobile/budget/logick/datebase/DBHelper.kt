package com.itechmobile.budget.logick.datebase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


/**
 * Created by artem on 26.07.17.
 */
class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {

    companion object {

        private val LOG_TAG = "DBHelper"

        private val DB_VER = 1
        private val DB_NAME = "budgetDB.db"

        val TABLE_MANY = "table_many"

        private val CREATE_TABLE_MANY = "create table " + TABLE_MANY + " (" +
                ManyKey.ID + " integer primary key autoincrement, " +
                ManyKey.NAME + " text, " +
                ManyKey.MANY + " integer, " +
                ManyKey.TIME + " integer, " +
                ManyKey.IS_DONE + " integer" +
                ");"

    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(LOG_TAG, CREATE_TABLE_MANY)
        db.execSQL(CREATE_TABLE_MANY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    class ManyKey {
        companion object {
            val ID = "_id"
            val NAME = "name"
            val MANY = "many"
            val TIME = "time"
            val IS_DONE = "isDone"
        }
    }

}