package com.itechmobile.budget.ui.list.helpers

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.itechmobile.budget.R
import com.itechmobile.budget.model.TracsationFooterModel
import com.itechmobile.budget.model.TracsationHeaderModel
import com.itechmobile.budget.model.TracsationModel
import com.itechmobile.budget.ui.list.views.FooterViewHolder
import com.itechmobile.budget.ui.list.views.HeaderViewHolder
import com.itechmobile.budget.ui.list.views.ItemViewHolder
import java.util.*

class TransactionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mItems = arrayListOf<IItem>()

    override fun getItemViewType(position: Int): Int {
        return mItems[position].type.ordinal
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(viewGroup.context)
        when (viewType) {
            RowType.ITEM.ordinal -> {
                val view = layoutInflater.inflate(R.layout.item_list_transaction, viewGroup, false)
                return ItemViewHolder(view)
            }
            RowType.HEADER.ordinal -> {
                val view = layoutInflater.inflate(R.layout.item_header_transaction, viewGroup, false)
                return HeaderViewHolder(view)
            }
            RowType.FOOTER.ordinal -> {
                val view = layoutInflater.inflate(R.layout.item_footer_transaction, viewGroup, false)
                return FooterViewHolder(view)
            }
            else -> throw RuntimeException("there is no type that matches the type $viewType make sure your using types correctly")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is HeaderViewHolder -> {
                val header = holder as HeaderViewHolder
                header.model = mItems[position] as TracsationHeaderModel
            }
            is ItemViewHolder -> {
                val item = holder as ItemViewHolder
                item.model = mItems[position] as TracsationModel
            }
            is FooterViewHolder -> {
                val header = holder as FooterViewHolder
                header.model = mItems[position] as TracsationFooterModel
            }
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun newItems(items: ArrayList<IItem>) {
        if(items.size == 0 && mItems.size == 0) return
        mItems.clear()
        mItems.addAll(items)
        notifyDataSetChanged()
    }

    interface IItem {
        val type: RowType
    }

    enum class RowType {
        HEADER, ITEM, FOOTER
    }

}