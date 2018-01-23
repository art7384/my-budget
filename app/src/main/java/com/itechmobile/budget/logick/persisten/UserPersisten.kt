package com.itechmobile.budget.logick.persisten

import android.annotation.SuppressLint
import android.content.Context
import com.itechmobile.budget.App

/**
 * Created by artem on 19.12.17.
 */
class UserPersisten private constructor() {

    companion object {
        private val mPref = App.instance.baseContext.getSharedPreferences("budget", Context.MODE_PRIVATE)

        var categoryId: Long
            get() = mPref.getLong(Key.CATEGORY_ID.key, 0)
            @SuppressLint("ApplySharedPref")
            set(value) {
                val edit = mPref.edit()
                synchronized(Key.CATEGORY_ID) {
                    edit.putLong(Key.CATEGORY_ID.key, value)
                    edit.commit()
                }
            }

        var numAppStart: Long
            get() = mPref.getLong(Key.NUM_APP_START.key, 0)
            @SuppressLint("ApplySharedPref")
            set(value) {
                val edit = mPref.edit()
                synchronized(Key.NUM_APP_START) {
                    edit.putLong(Key.NUM_APP_START.key, value)
                    edit.commit()
                }
            }

        private enum class Key(val key: String) {
            NUM_APP_START("NUM_APP_START"),
            CATEGORY_ID("CATEGORY_ID")
        }

    }
}