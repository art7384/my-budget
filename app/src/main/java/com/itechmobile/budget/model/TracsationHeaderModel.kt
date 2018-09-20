package com.itechmobile.budget.model

import android.annotation.SuppressLint
import com.itechmobile.budget.ui.list.helpers.TransactionAdapter
import java.text.SimpleDateFormat
import java.util.*

class TracsationHeaderModel(val date:Date) : TransactionAdapter.IItem {

    constructor():this(Date())

    override val type = TransactionAdapter.RowType.HEADER

    val dayForWeek:String
    @SuppressLint("SimpleDateFormat")
    get() = SimpleDateFormat("EEEE").format(date)

    val dataForMonth:String
    @SuppressLint("SimpleDateFormat")
    get() = SimpleDateFormat("d").format(date)
}