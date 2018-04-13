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

        val size: Int = TransactionTable().queryAll().size

        val startDate: Date
            get() {
                val tts = TransactionTable().querySorted("date", Sort.ASCENDING)
                if (tts.isEmpty()) return Date()
                return tts.first().date
            }

        fun add(models: List<TracsationModel>) {
            var id = TransactionTable().queryAll().maxBy { it.id }?.id ?: 0L
            models.map {
                it.id = ++id
            }
            TransactionParser.toRealm(models).saveAll()
        }

        fun add(model: TracsationModel): Long {
            var id = TransactionTable().queryAll().maxBy { it.id }?.id ?: 0L
            model.id = ++id
            TransactionParser.toRealm(model).save()
            return id
        }

        fun getSumPl(startDate: Date, stopDate: Date): Int = TransactionTable().queryAll().filter {
            it.date.time in startDate.time until stopDate.time && it.money > 0
        }.sumBy { it.money }

        fun getSumMn(startDate: Date, stopDate: Date): Int = TransactionTable().queryAll().filter {
            it.date.time in startDate.time until stopDate.time && it.money < 0
        }.sumBy { it.money }

        fun get(startDate: Date, stopDate: Date): List<TracsationModel> = TransactionParser.from(
                TransactionTable().queryAll().filter {
                    it.date.time in startDate.time until stopDate.time
                })

        fun get(idCategory: Long, startDate: Date, stopDate: Date, isIncomes: Boolean): List<TracsationModel> = TransactionParser.from(
                TransactionTable().queryAll().filter {
                    it.date.time in startDate.time until stopDate.time &&
                            if (isIncomes) it.money > 0 else it.money < 0
                                    && it.categoryId == idCategory
                })

        fun get(startDate: Date, stopDate: Date, isIncomes: Boolean): List<TracsationModel> = TransactionParser.from(
                TransactionTable().queryAll().filter {
                    it.date.time in startDate.time until stopDate.time &&
                            if (isIncomes) it.money > 0 else it.money < 0
                })

        fun get(date: Date): List<TracsationModel> = TransactionParser.from(TransactionTable().queryAll().filter {
            it.date < date
        })

        fun getId(id: Long): TracsationModel? {
            val trTb = TransactionTable().queryFirst {
                this.equalTo("id", id)
            } ?: return null
            return TransactionParser.from(trTb)
        }

        fun getForCategorySize(categoryId: Long): Int = TransactionTable().query {
            this.equalTo("categoryId", categoryId)
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
                        it.date.time in startDate.time until stopDate.time
                    }.map {
                        if (hashMap[it.categoryId] == null) hashMap[it.categoryId] = 0
                        hashMap[it.categoryId] = hashMap[it.categoryId]!! + it.money
                    }
            return hashMap
        }

        fun getSumTo(date: Date): Int = TransactionTable().queryAll().filter {
            it.date < date
        }.sumBy { it.money }

        fun dell(id: Long) = TransactionTable().delete {
            this.equalTo("id", id)
        }

        fun update(model: TracsationModel) {
            val table = TransactionTable().queryFirst {
                this.equalTo("id", model.id)
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