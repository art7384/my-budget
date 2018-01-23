package com.itechmobile.budget.logick.service

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Created by artem on 17.11.17.
 */
class AnalyticsService private constructor() {

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    private object Holder { val INSTANCE = AnalyticsService() }

    companion object {
        private val LOG_TAG = "AnalyticsService"
        val INSTANCE: AnalyticsService by lazy { Holder.INSTANCE }
    }

    fun init(app: Application){
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(app)
    }

    fun session(){

    }

    fun clickDay(){

    }

    fun openEditorTransaction(){
// isNew - открываем существующию транзакцию или создаем новую
    }

    fun addTransaction(){

    }

    fun updateTransaction(){

    }

}