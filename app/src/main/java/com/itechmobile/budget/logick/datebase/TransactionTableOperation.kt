package com.itechmobile.budget.logick.datebase

import android.annotation.SuppressLint
import com.itechmobile.budget.logick.parsers.TransactionParser
import com.itechmobile.budget.model.TracsationModel
import com.vicpin.krealmextensions.*
import io.realm.Sort
import java.util.*
import kotlin.collections.HashMap


/**
 * Created by artem on 22.08.17.
 */
class TransactionTableOperation private constructor() {

    companion object {

        private val LOG_TAG = "TransactionTableOperation"

        val size: Int = TransactionTable().queryAll().size

        val startDate: Date = TransactionTable().querySorted("date", Sort.ASCENDING).first().date

        fun add(models: List<TracsationModel>) = TransactionParser.toRealm(models).saveAll()

        fun add(model: TracsationModel) = TransactionParser.toRealm(model).save()

        fun getSumPl(startDate: Date, stopDate: Date): Int = TransactionTable().queryAll().filter { predicate ->
            predicate.date in startDate..stopDate && predicate.money > 0
        }.sumBy { it.money }

        fun getSumMn(startDate: Date, stopDate: Date): Int = TransactionTable().queryAll().filter {
            it.date in startDate..stopDate && it.money < 0
        }.sumBy { it.money }

        fun get(startDate: Date, stopDate: Date): List<TracsationModel> = TransactionParser.from(
                TransactionTable().queryAll().filter {
                    it.date in startDate..stopDate
                })

        fun get(date: Date): List<TracsationModel> = TransactionParser.from(TransactionTable().queryAll().filter {
            it.date < date
        })

        fun getId(id: Long): TracsationModel? {
            val trTb = TransactionTable().queryFirst {
                it.equalTo("id", id)
            } ?: return null
            return TransactionParser.from(trTb)
        }

        fun getForCategorySize(categoryId: Long): Int = TransactionTable().query {
            it.equalTo("categoryId", categoryId)
        }.size

        fun getNoDone(date: Date): ArrayList<TracsationModel> = TransactionParser.from(TransactionTable().queryAll().filter {
            it.date < date && !it.isDone
        })

        fun getDoneSumTo(date: Date): Int = TransactionTable().queryAll().filter {
            it.date < date && it.isDone
        }.sumBy { it.money }

        @SuppressLint("LongLogTag")
        fun getCategorySum(startDate: Date, stopDate: Date): HashMap<Long, Int> {

            val hashMap = HashMap<Long, Int>()
            TransactionTable().querySorted("categoryId", Sort.DESCENDING)
                    .filter {
                        it.date in startDate..stopDate
                    }.map {
                        if (hashMap[it.categoryId] == null) hashMap[it.categoryId] = 0
                        hashMap[it.categoryId] = hashMap[it.categoryId]!! + it.money
                    }
            return hashMap
        }

        fun getSumTo(date: Date): Int = TransactionTable().queryAll().filter {
            it.date < date
        }.sumBy { it.money }

        fun dell(id: Long) = TransactionTable().queryFirst {
            it.equalTo("id", id)
        }?.deleteFromRealm()

        fun update(model: TracsationModel) {
            val table = TransactionTable().queryFirst {
                it.equalTo("id", model.id)
            }
            if (table != null) {
                table.name = model.name
                table.money = model.money
                table.date = model.date
                table.isDone = model.isDone
                table.categoryId = model.idCategory
                table.save()
            }
        }

    }
}