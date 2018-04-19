package com.itechmobile.budget.ui.regular.views

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.itechmobile.budget.R
import com.itechmobile.budget.ui.regular.helpers.RegularAdapter

class TitleViewHolder(val view: View) : BaseViewHolder(view) {

    var onClickListner: ((Boolean, String) -> Unit)? = null

    private val mText: TextView = view.findViewById(R.id.title)
    private val mMnBt: ImageButton = view.findViewById(R.id.mn)
    private val mPlBt: ImageButton = view.findViewById(R.id.pl)

    override fun getType(): RegularAdapter.ItemType {
        return RegularAdapter.ItemType.TITLE
    }

    fun set(title: String) {
        mText.text = title
        mMnBt.setOnClickListener {
            onClickListner?.invoke(true, title)
        }
        mPlBt.setOnClickListener {
            onClickListner?.invoke(false, title)
        }
    }


}