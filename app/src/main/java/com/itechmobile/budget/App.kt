package com.itechmobile.budget

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import com.itechmobile.budget.logick.datebase.DBHelper




/**
 * Created by artem on 24.07.17.
 */
class App : Application() {

    private lateinit var mSQLiteDatabase: SQLiteDatabase

    private object Holder {
        var instance = App()
    }

    val database: SQLiteDatabase
        get() = mSQLiteDatabase

    companion object {
        val instance: App by lazy { Holder.instance }
    }

    override fun onCreate() {
        super.onCreate()
        mSQLiteDatabase = DBHelper(this).writableDatabase
        Holder.instance = this
    }

}