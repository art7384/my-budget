package com.itechmobile.budget.ui.regular.views

import android.view.View
import android.widget.TextView
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.category.CategoryService
import com.itechmobile.budget.model.RegularModel
import com.itechmobile.budget.ui.regular.helpers.RegularAdapter

class DailyViewHolder(val view: View) : BaseViewHolder(view) {

    var onClickListner: OnClickListner? = null

    private val mSumm: TextView = view.findViewById(R.id.summ)
    private val mDescription: TextView = view.findViewById(R.id.description)
    private val mCategory: TextView = view.findViewById(R.id.category)
    private val mMarker: View = view.findViewById(R.id.marker)

    override fun getType(): RegularAdapter.ItemType {
        return RegularAdapter.ItemType.TITLE
    }

    fun set(model: RegularModel) {
        if (model.money < 0) {
            mSumm.text = (model.money * -1).toString()
            mMarker.setBackgroundResource(R.color.yellow)
        } else {
            mSumm.text = model.money.toString()
            mMarker.setBackgroundResource(R.color.green)
        }
        mDescription.text = model.name
        mCategory.text = CategoryService.INSTANCE.get(model.id).icoName
    }

    interface OnClickListner {
        fun onClick(model: RegularModel)
    }

}