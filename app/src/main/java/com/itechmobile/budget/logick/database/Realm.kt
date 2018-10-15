package com.itechmobile.budget.logick.database

import io.realm.RealmObject
import java.util.*

/**
 * Created by artem on 05.03.18.
 */

open class TransactionTable(var name: String = "",
                            var money: Int = 0,
                            var date: Date = Date(),
                            var isDone: Boolean = false,
                            var categoryId: Long = -1) : RealmObject() {
    var id: Long = -1
}

open class CategoryTable(var name: String = "",
                         var icoName: String = "",
                         var isIncome: Boolean = false,
                         var isDell: Boolean = false) : RealmObject() {
    var id: Long = -1
}

/**
 * Если date=-1, day=-1 - транзакция на каждый день
 * Если date=-1, day!=-1 - транзакция на каждый месяц
 * Если date!=-1, day=-1 - транзакция на каждый недели
 */
open class RegularTable(var name: String = "",
                        var money: Int = 0,
                        var date: Int = -1, // число
                        var day: Int = -1, // день недели
                        var categoryId: Long = -1) : RealmObject() {
    var id: Long = -1
}