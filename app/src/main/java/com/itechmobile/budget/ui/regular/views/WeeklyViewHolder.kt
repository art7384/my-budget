package com.itechmobile.budget.ui.regular.views

import android.view.View
import android.widget.TextView
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.CategoryService
import com.itechmobile.budget.model.RegularModel
import com.itechmobile.budget.ui.regular.helpers.RegularAdapter

class WeeklyViewHolder(val view: View) : BaseItemsViewHolider(view) {

    private val mSumm: TextView = view.findViewById(R.id.summ)
    private val mDescription: TextView = view.findViewById(R.id.description)
    private val mCategory: TextView = view.findViewById(R.id.category)
    private val mMarker: View = view.findViewById(R.id.marker)
    private val mDateText = view.findViewById<TextView>(R.id.date)

    override fun getType(): RegularAdapter.ItemType {
        return RegularAdapter.ItemType.TITLE
    }

    override fun set(model: RegularModel) {
        if (model.money < 0) {
            mSumm.text = (model.money * -1).toString()
            mMarker.setBackgroundResource(R.color.yellow)
        } else {
            mSumm.text = model.money.toString()
            mMarker.setBackgroundResource(R.color.green)
        }
        mDescription.text = model.name
        mCategory.text = CategoryService.INSTANCE.get(model.id).icoName
        view.resources.getStringArray(R.array.days)[model.day]
        mDateText.text = view.resources.getStringArray(R.array.days)[model.day]
    }

}