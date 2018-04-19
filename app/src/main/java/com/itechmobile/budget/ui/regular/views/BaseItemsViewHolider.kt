package com.itechmobile.budget.ui.regular.views

import android.view.View
import com.itechmobile.budget.model.RegularModel

abstract class BaseItemsViewHolider(view: View) : BaseViewHolder(view) {
    open var onClickListner: ((RegularModel) -> Unit)? = null
    abstract fun set(model: RegularModel)

}