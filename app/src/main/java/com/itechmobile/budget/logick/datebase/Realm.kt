package com.itechmobile.budget.logick.datebase

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by artem on 05.03.18.
 */

open class TransactionTable(@PrimaryKey var id: Long = -1,
                            var name: String = "",
                            var money: Int = 0,
                            var date: Date = Date(),
                            var isDone: Boolean = false,
                            var categoryId: Long = -1) : RealmObject()

open class CategoryTable(@PrimaryKey var id: Long = -1,
                         var name: String = "",
                         var icoName: String = "",
                         var isIncome: Boolean = false,
                         var isDell: Boolean = false) : RealmObject()