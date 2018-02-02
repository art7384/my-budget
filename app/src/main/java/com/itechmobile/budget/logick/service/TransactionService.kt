package com.itechmobile.budget.logick.service

import com.itechmobile.budget.logick.datebase.TransactionTableOperation
import com.itechmobile.budget.model.TracsationModel
import java.util.*


/**
 * Created by artem on 25.07.17.
 */

class TransactionService private constructor(){

    private object Holder { val INSTANCE = TransactionService() }

    companion object {
        private val LOG_TAG = "TransactionService"
        val INSTANCE: TransactionService by lazy { Holder.INSTANCE }
    }

    fun save(model: TracsationModel): TracsationModel {
        return TransactionTableOperation.add(model)
    }

    fun getDoneSumTo(time: Long):Long{
        val d = Date(time)
        val t = Date(d.year, d.month, d.date).time + 24 * 60 * 60 * 1000
        return TransactionTableOperation.getDoneSumTo(t)
    }

    fun getDayPl(time: Long):Int{
        val d = Date(time)
        val startTime = Date(d.year, d.month, d.date).time
        val stopTime = Date(d.year, d.month, d.date).time + 24 * 60 * 60 * 1000
        return TransactionTableOperation.getSumPl(startTime, stopTime)
    }

    fun getDayMn(time: Long):Int{
        val d = Date(time)
        val startTime = Date(d.year, d.month, d.date).time
        val stopTime = Date(d.year, d.month, d.date).time + 24 * 60 * 60 * 1000
        return TransactionTableOperation.getSumMn(startTime, stopTime)
    }

    fun getDay(time: Long): List<TracsationModel> {
        val d = Date(time)
        val startTime = Date(d.year, d.month, d.date).time
        val stopTime = Date(d.year, d.month, d.date).time + 24 * 60 * 60 * 1000
        return TransactionTableOperation.get(startTime, stopTime)
    }

    fun getFoeId(id: Long): TracsationModel {
        return TransactionTableOperation.getFoeId(id)
    }

    fun getSumTo(time: Long): Long {
        val d = Date(time)
        val t = Date(d.year, d.month, d.date).time
        return TransactionTableOperation.getSumTo(t)
    }

    fun dell(id: Long) : Int {
        return TransactionTableOperation.dell(id)
    }

    fun update(model: TracsationModel) : Int{
        return TransactionTableOperation.update(model)
    }

    fun getNoDone(time: Long) : List<TracsationModel> {
        val d = Date(time)
        val t = Date(d.year, d.month, d.date).time
        return TransactionTableOperation.getNoDone(t)
    }

}
