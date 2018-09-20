package com.itechmobile.budget.ui.calendar.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.CategoryService
import com.itechmobile.budget.model.TracsationModel
import java.util.*


/**
 * Created by artem on 27.07.17.
 */
class TransactionListAdapter(private val mCnx: Context) : BaseAdapter() {

    private var mItems: MutableList<TracsationModel> = ArrayList()

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val item = getItem(position) as TracsationModel

        var v: View? = view

        if (v == null) {
            val li: LayoutInflater = mCnx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = li.inflate(R.layout.item_transaction, viewGroup, false)
        }

        val txtSumm = v!!.findViewById<TextView>(R.id.summ)
        val txtDdescription = v.findViewById<TextView>(R.id.description)
        val txtCategory = v.findViewById<TextView>(R.id.category)
        val mMarker = v.findViewById<View>(R.id.marker)

        txtDdescription.text = item.name
        if (item.price < 0) {
            txtSumm.text = (item.price * (-1)).toString()
            mMarker.setBackgroundColor(mCnx.resources.getColor(R.color.yellow))
        } else {
            txtSumm.text = item.price.toString()
            mMarker.setBackgroundColor(mCnx.resources.getColor(R.color.green))
        }

        txtCategory.text = CategoryService.INSTANCE.get(item.idCategory)?.icoName

        if (item.isDone) {
        } else {
        }

        return v

    }

    override fun getItem(position: Int): Any {
        return mItems[position]
    }

    override fun getItemId(position: Int): Long {
        return mItems[position].id
    }

    override fun getCount(): Int {
        return mItems.size
    }

    fun update(model: TracsationModel) {
        for (item in mItems) {
            if (item.id == model.id) {
                item.isDone = model.isDone
                item.name = model.name
                item.price = model.price
                item.date = model.date
                notifyDataSetChanged()
                return
            }
        }
    }

    fun add(model: TracsationModel) {
        mItems.add(0, model)
        notifyDataSetChanged()
    }

    fun updateList(items: List<TracsationModel>) {
        mItems.clear()
        mItems.addAll(items)
        notifyDataSetChanged()
    }

}