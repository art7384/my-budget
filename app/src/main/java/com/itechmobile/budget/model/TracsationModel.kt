package com.itechmobile.budget.model

import com.itechmobile.budget.ui.list.helpers.TransactionAdapter
import java.util.*

/**
 * Created by artem on 03.08.17.
 */
data class TracsationModel(var price: Int, var name: String, var date: Date, var idCategory: Long) : TransactionAdapter.IItem {
    constructor() : this(0, "", Date(), -1)

    override val type = TransactionAdapter.RowType.ITEM

    var id: Long = -1
    var isDone = false
}