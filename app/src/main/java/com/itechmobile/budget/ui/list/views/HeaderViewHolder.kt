package com.itechmobile.budget.ui.list.views

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.itechmobile.budget.R
import com.itechmobile.budget.model.TracsationHeaderModel

class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var mTxtName: TextView = itemView.findViewById(R.id.itemHeaderTransaction_TextView_name)
    private var mModel = TracsationHeaderModel()
    var model: TracsationHeaderModel
        get() = mModel
        @SuppressLint("SetTextI18n")
        set(value) {
            mModel = value
            mTxtName.text = "${mModel.dataForMonth}, ${mModel.dayForWeek}"
        }
}