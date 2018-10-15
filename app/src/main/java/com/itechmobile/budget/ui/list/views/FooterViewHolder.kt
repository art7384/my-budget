package com.itechmobile.budget.ui.list.views

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.itechmobile.budget.App
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.heders.NumberFormatHeder
import com.itechmobile.budget.logick.service.transaction.TransactionService
import com.itechmobile.budget.model.TracsationFooterModel
import java.util.*

class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mTxtPrice: TextView = itemView.findViewById(R.id.itemFooterTransaction_TextView_price)
    private val mBtPlus: ImageButton = itemView.findViewById(R.id.itemFooterTransaction_ImageButton_plus)
    private val mBtMinus: ImageButton = itemView.findViewById(R.id.itemFooterTransaction_ImageButton_minus)

    private var mModel = TracsationFooterModel()

    var model: TracsationFooterModel
        get() = mModel
        @SuppressLint("SetTextI18n")
        set(value) {
            mModel = value
            val price = TransactionService.INSTANCE.getSumTo(mModel.date)
            if (price < 0) {
                mTxtPrice.setTextColor(App.instance.resources.getColor(R.color.accent))
            } else {
                mTxtPrice.setTextColor(App.instance.resources.getColor(R.color.green))
            }
            mTxtPrice.text = NumberFormatHeder.currency(price)
            mBtPlus.setOnClickListener {
                mModel.onClickPlus?.onClick(mModel.date)
            }
            mBtMinus.setOnClickListener {
                mModel.onClickMinus?.onClick(mModel.date)
            }
        }

    interface OnClickAddTransactionListner {
        fun onClick(date: Date)
    }

}