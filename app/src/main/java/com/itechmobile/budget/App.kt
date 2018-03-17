package com.itechmobile.budget

import android.annotation.SuppressLint
import android.app.Application
import android.database.sqlite.SQLiteDatabase
import android.support.text.emoji.EmojiCompat
import android.support.text.emoji.bundled.BundledEmojiCompatConfig
import com.itechmobile.budget.logick.datebase.DBHelper
import com.itechmobile.budget.logick.service.AnalyticsService
import io.realm.Realm
import io.realm.RealmConfiguration


/**
 * Created by artem on 24.07.17.
 *
 * Analytics tracking ID: UA-106039074-1
 * Google Analytics Account: art7384@gmail.com Apps
 * Analytics Property: Default Demo App Android: com.itechmobile.budget
 *
 * Firebaze
 * Название проекта: Budget
 * Общедоступное название (public-facing name): project-948471596417
 * Идентификатор проекта: budget-be16e
 * Ключ API для веб-приложения: AIzaSyBZqqYvfgtcHtzF3UhXspJlAljQZtRd9eY
 *
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

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()

        Holder.instance = this

        AnalyticsService.INSTANCE.init(this)
        EmojiCompat.init(BundledEmojiCompatConfig(this))

        initRealm()
        mSQLiteDatabase = DBHelper(this).writableDatabase

        //DBHelper.sqlMigrationReal(mSQLiteDatabase)

    }

    private fun initRealm(){
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build())

    }

}