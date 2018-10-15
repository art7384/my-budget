package com.itechmobile.budget.ui.list.views

import android.annotation.SuppressLint
import android.support.text.emoji.widget.EmojiTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.heders.NumberFormatHeder
import com.itechmobile.budget.logick.service.category.CategoryService
import com.itechmobile.budget.model.CategoryModel
import com.itechmobile.budget.model.TracsationModel

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mTxtName: TextView = itemView.findViewById(R.id.itemTransaction_TextView_name)
    private var mTxtPrice: TextView = itemView.findViewById(R.id.itemTransaction_TextView_price)
    private val mCategoryIco: EmojiTextView = itemView.findViewById(R.id.itemTransaction_EmojiTextView_ico)
    private val mColorView: View = itemView.findViewById(R.id.itemTransaction_View_color)

    private var mTransactionModel = TracsationModel()
    private var mCategoryModel = CategoryModel()


    var model: TracsationModel
        get() = mTransactionModel
        @SuppressLint("SetTextI18n")
        set(value) {
            mTransactionModel = value
            mTxtName.text = mTransactionModel.name
            mTxtPrice.text = NumberFormatHeder.currency(mTransactionModel.price)
            mCategoryModel = CategoryService.INSTANCE.get(mTransactionModel.idCategory)
            mCategoryIco.text = mCategoryModel.icoName
            mColorView.setBackgroundColor(if (mCategoryModel.isIncome) 0xff2DBF00.toInt() else 0xffFFCD00.toInt())
        }

}