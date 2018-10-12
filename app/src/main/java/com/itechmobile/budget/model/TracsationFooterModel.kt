package com.itechmobile.budget.model

import com.itechmobile.budget.ui.list.helpers.TransactionAdapter
import com.itechmobile.budget.ui.list.views.FooterViewHolder
import java.util.*

data class TracsationFooterModel(val date: Date) : TransactionAdapter.IItem {
    constructor() : this(Date())

    override val type = TransactionAdapter.RowType.FOOTER
    var onClickPlus : FooterViewHolder.OnClickAddTransactionListner? = null
    var onClickMinus : FooterViewHolder.OnClickAddTransactionListner? = null

}