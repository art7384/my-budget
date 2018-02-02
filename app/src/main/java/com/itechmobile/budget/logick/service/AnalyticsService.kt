package com.itechmobile.budget.logick.service

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.yandex.metrica.YandexMetrica

/**
 * Created by artem on 17.11.17.
 */
class AnalyticsService private constructor() {

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    private object Holder { val INSTANCE = AnalyticsService() }

    companion object {
        val INSTANCE: AnalyticsService by lazy { Holder.INSTANCE }
    }

    fun init(app: Application){
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(app)
        YandexMetrica.activate(app.applicationContext, "dccab762-b39e-47db-93fb-8b8bbf2d8fe8")
        YandexMetrica.enableActivityAutoTracking(app)
    }

}