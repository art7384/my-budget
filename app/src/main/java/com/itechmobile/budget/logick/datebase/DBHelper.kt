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

        private val DB_VER = 4
        private val DB_NAME = "budgetDB.db"

        private val CREATE_TABLE_TRANSACTION = "create table " + TableName.TRANSACTION + " (" +
                TransactionKey._ID + " integer primary key autoincrement, " +
                TransactionKey.NAME + " text, " +
                TransactionKey.MONEY + " integer, " +
                TransactionKey.TIME + " integer, " +
                TransactionKey.IS_DONE + " integer, " +
                TransactionKey.IS_DELL + " integer, " +
                TransactionKey.IS_SYNCH + " integer, " +
                TransactionKey.TAG_ID + " integer, " +
                TransactionKey.CATEGORY_ID + " integer, " +
                TransactionKey.BUDGET_ID + " integer" +
                ");"

        private val CREATE_TABLE_CATEGORY = "create table " + TableName.CATEGORY + " (" +
                CategoryKey._ID + " integer primary key autoincrement, " +
                CategoryKey.NAME + " text, " +
                CategoryKey.ICO_NAME + " text, " +
                CategoryKey.IS_INCOME + " integer" +
                CategoryKey.IS_VISIBLE + " integer" +
                ");"
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(LOG_TAG, "Create table: ${TableName.TRANSACTION}")
        db.execSQL(CREATE_TABLE_TRANSACTION)
        db.execSQL(CREATE_TABLE_CATEGORY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(LOG_TAG, "UPGRADE oldVersion: $oldVersion, newVersion: $newVersion")
        if(oldVersion < 3){
            db.execSQL("ALTER TABLE ${TableName.TRANSACTION} ADD ${TransactionKey.CATEGORY_ID} text default -1")
        }
        if(oldVersion < 4){
            db.execSQL("ALTER TABLE ${TableName.CATEGORY} ADD ${CategoryKey.IS_VISIBLE} integer default 1")
        }
    }

    class TableName {
        companion object {
            val USER = "user"
            val BUDGET = "budget"
            val TRANSACTION = "transact"
            val INVITE = "invite"
            val CATEGORY = "category"
        }
    }

    class CategoryKey {
        companion object {
            val _ID = "_id"
            val NAME = "name"
            val ICO_NAME = "ico_name"
            val IS_INCOME = "is_income"
            val IS_VISIBLE = "is_visible"// удаленная категория
        }
    }

    class TransactionKey {
        companion object {
            val _ID = "_id"
            val MONEY = "money"
            val TIME = "time"
            val IS_DONE = "is_done"
            val IS_SYNCH = "is_synch"
            val IS_DELL = "is_dell"
            val TAG_ID = "tag_id"
            val BUDGET_ID = "budget_id"
            val NAME = "name"
            val CATEGORY_ID = "category_id"
        }
    }

}