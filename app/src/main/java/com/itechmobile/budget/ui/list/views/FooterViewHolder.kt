package com.itechmobile.budget.ui.list.views

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.itechmobile.budget.R
import com.itechmobile.budget.model.TracsationFooterModel

class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val txtPrice: TextView = itemView.findViewById(R.id.itemFooterTransaction_TextView_price)

    private var mModel = TracsationFooterModel()
    var model: TracsationFooterModel
        get() = mModel
        @SuppressLint("SetTextI18n")
        set(value) {
            mModel = value
            txtPrice.text = mModel.price.toString()
        }

}