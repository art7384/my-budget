package com.itechmobile.budget.ui.regular.views

import android.support.v7.widget.RecyclerView
import android.view.View
import com.itechmobile.budget.ui.regular.helpers.RegularAdapter

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun getType(): RegularAdapter.ItemType
}