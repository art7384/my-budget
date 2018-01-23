package com.itechmobile.budget.logick.service

import com.itechmobile.budget.logick.persisten.UserPersisten

/**
 * Created by artem on 19.12.17.
 */
class UserService private constructor() {

    private object Holder {
        val INSTANCE = UserService()
    }

    companion object {
        private val LOG_TAG = "UserService"
        val INSTANCE: UserService by lazy { Holder.INSTANCE }
    }

    val numAppStart: Long
        get() = UserPersisten.numAppStart

    fun appStart(){
        UserPersisten.numAppStart = UserPersisten.numAppStart + 1
    }

}