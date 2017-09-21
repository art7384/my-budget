package com.itechmobile.budget.logick.service

import com.itechmobile.budget.logick.datebase.TableMany
import com.itechmobile.budget.model.TracsationModel
import java.util.*


/**
 * Created by artem on 25.07.17.
 */

class TransactionService private constructor(){

    private object Holder { val INSTANCE = TransactionService() }

    companion object {
        //private val LOG_TAG = "TransactionService"
        val INSTANCE: TransactionService by lazy { Holder.INSTANCE }
    }

    fun save(model: TracsationModel): TracsationModel {
        return TableMany.add(model)
    }

    fun get(time: Long): List<TracsationModel> {
        val timeFormat = timeFormat(time)
        return TableMany.get(timeFormat)
    }

    fun getFoeId(id: Long): TracsationModel {
        return TableMany.getFoeId(id)
    }

    fun getSumTo(time: Long): Long {
        val timeFormat = timeFormat(time)
        return TableMany.getSumTo(timeFormat)
    }

    fun dell(id: Long) : Int {
        return TableMany.dell(id)
    }

    fun update(model: TracsationModel) : Int{
        return TableMany.update(model)
    }

    fun getNoDone(time: Long) : List<TracsationModel> {
        val timeFormat = timeFormat(time)
        return TableMany.getNoDone(timeFormat)
    }

    private fun timeFormat(time: Long) : Long {
        // Удаляем часы, минуты, секунды
        val d = Date(time)
        val date = Date(d.year, d.month, d.date)
        return date.time
    }

}
