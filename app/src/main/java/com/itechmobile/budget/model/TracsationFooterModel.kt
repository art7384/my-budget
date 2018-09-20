package com.itechmobile.budget.model

import com.itechmobile.budget.ui.list.helpers.TransactionAdapter

data class TracsationFooterModel(val price: Int) : TransactionAdapter.IItem {
    constructor() : this(0)

    override val type = TransactionAdapter.RowType.FOOTER
}