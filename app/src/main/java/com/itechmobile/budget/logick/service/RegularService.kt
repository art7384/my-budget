package com.itechmobile.budget.logick.service

import com.itechmobile.budget.model.RegularModel

class RegularService private constructor() {

    private object Holder {
        val INSTANCE = RegularService()
    }

    companion object {
        private val LOG_TAG = "RegularService"
        val INSTANCE: RegularService by lazy { Holder.INSTANCE }
    }

    fun getAll(): ArrayList<RegularModel>{
        return ArrayList()
    }

}