package com.itechmobile.budget.ui.regular.helpers

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.itechmobile.budget.App
import com.itechmobile.budget.R
import com.itechmobile.budget.model.RegularModel
import com.itechmobile.budget.ui.regular.views.BaseItemsViewHolider
import com.itechmobile.budget.ui.regular.views.BaseViewHolder
import com.itechmobile.budget.ui.regular.views.MonthlyViewHolder
import com.itechmobile.budget.ui.regular.views.TitleViewHolder

class RegularAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    var onClickItemListner: ((RegularModel) -> Unit)? = null
    var onClickTitleListner: ((Boolean, String) -> Unit)? = null

    private var mItems: ArrayList<RegularModel> = ArrayList()

    companion object {
        private const val LOG_TAG = "ListCardsAdapter"
        const val ITEM_VIEW_TYPE_TITLE = 0
        const val ITEM_VIEW_TYPE_MONTHLY = 1
        const val ITEM_VIEW_TYPE_WEEKLY = 2
        const val ITEM_VIEW_TYPE_DAILY = 3
    }

    override fun getItemViewType(position: Int): Int {
        val model = mItems[position]
        return when {
            model.id == -1L -> ITEM_VIEW_TYPE_TITLE
            model.date != -1 -> ITEM_VIEW_TYPE_MONTHLY
            model.day != -1 -> ITEM_VIEW_TYPE_WEEKLY
            else -> ITEM_VIEW_TYPE_DAILY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): BaseViewHolder {

        return when (viewType) {
            ITEM_VIEW_TYPE_MONTHLY -> MonthlyViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_regular_monthly, parent, false))
            ITEM_VIEW_TYPE_WEEKLY -> MonthlyViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_regular_monthly, parent, false))
            ITEM_VIEW_TYPE_DAILY -> MonthlyViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_regular_monthly, parent, false))
            else -> MonthlyViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_regular_monthly, parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

        val model = mItems[position]

        if (holder.itemViewType == ITEM_VIEW_TYPE_TITLE) {
            val h = holder as TitleViewHolder
            h.set(model.name)
            h.onClickListner = { isMn, title ->
                onClickTitleListner?.invoke(isMn, title)
            }
        } else {
            val h = holder as BaseItemsViewHolider
            h.set(model)
            h.onClickListner = {
                onClickItemListner?.invoke(it)
            }
        }
    }

    override fun onFailedToRecycleView(holder: BaseViewHolder?): Boolean {
        return true
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun update(regulars: ArrayList<RegularModel>) {
        mItems.clear()
        val months: ArrayList<RegularModel> = ArrayList()
        val weeks: ArrayList<RegularModel> = ArrayList()
        val dais: ArrayList<RegularModel> = ArrayList()
        for (r in regulars) {
            when {
                r.date != -1 -> months.add(r)
                r.day != -1 -> weeks.add(r)
                else -> dais.add(r)
            }
        }
        mItems.add(RegularModel(-1, App.instance.resources.getString(R.string.monthly), -1, -1, -1))
        for (r in months) mItems.add(r)
        mItems.add(RegularModel(-1, App.instance.resources.getString(R.string.weekly), -1, -1, -1))
        for (r in weeks) mItems.add(r)
        mItems.add(RegularModel(-1, App.instance.resources.getString(R.string.dayly), -1, -1, -1))
        for (r in dais) mItems.add(r)

        notifyDataSetChanged()
    }

    enum class ItemType {
        TITLE,
        MONTHLY,
        WEEKLY,
        DAILY
    }


}