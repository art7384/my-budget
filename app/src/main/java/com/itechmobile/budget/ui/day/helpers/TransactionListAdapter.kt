package com.itechmobile.budget.ui.day.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.itechmobile.budget.App
import com.itechmobile.budget.R
import com.itechmobile.budget.model.TracsationModel


/**
 * Created by artem on 27.07.17.
 */
class TransactionListAdapter(private var mCnx: Context, private var mItems: MutableList<TracsationModel>, private val mOnItemClicksListern: OnItemClicksListern) : BaseAdapter() {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val item = getItem(position) as TracsationModel

        var v: View? = view

        if (v == null) {
            val li: LayoutInflater = mCnx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = li.inflate(R.layout.item_transaction, viewGroup, false)
        }

        val txtMoney = v!!.findViewById<TextView>(R.id.itemTransaction_TextView_money)
        val txtName = v.findViewById<TextView>(R.id.itemTransaction_TextView_name)
        val btDell = v.findViewById<Button>(R.id.itemMany_Button_dell)
        val btOk = v.findViewById<View>(R.id.itemTransaction_Button_ok)
        val cbDone = v.findViewById<CheckBox>(R.id.itemTransaction_CheckBox_done)

        if (item.many < 0) {
            txtMoney.setTextColor(App.instance.resources.getColor(R.color.many_sum_mn))
            txtMoney.text = item.many.toString()
        } else {
            txtMoney.setTextColor(App.instance.resources.getColor(R.color.many_sum_pl))
            txtMoney.text = "+" + item.many.toString()
        }

        cbDone.isChecked = item.isDone

        txtName.text = item.name
        if (item.isDone) {
            txtName.alpha = .5F
            txtMoney.alpha = .5F
        } else {
            txtName.alpha = 1F
            txtMoney.alpha = .1F
        }


        // Реакция на клик

        btDell.setOnClickListener {
            mOnItemClicksListern.onClickDell(this, item)
        }

        btOk.setOnClickListener {
            mOnItemClicksListern.onClickItem(item)
        }

        cbDone.setOnCheckedChangeListener { compoundButton, isCheck ->
            mOnItemClicksListern.onCheckedChanged(this, item, isCheck)
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
                item.many = model.many
                item.time = model.time
                notifyDataSetChanged()
                return
            }
        }
    }

    fun add(model: TracsationModel) {
        mItems.add(0, model)
        notifyDataSetChanged()
    }

    fun remove(model: TracsationModel) {
        mItems.remove(model)
        notifyDataSetChanged()
    }

    fun updateList(items: List<TracsationModel>) {
        mItems.clear()
        mItems.addAll(items)
        notifyDataSetChanged()
    }

    interface OnItemClicksListern {
        //fun onClickMenu(adapter: TransactionListAdapter, model: TracsationModel, view: View)
        fun onClickDell(adapter: TransactionListAdapter, model: TracsationModel)

        fun onClickItem(model: TracsationModel)
        fun onCheckedChanged(adapter: TransactionListAdapter, model: TracsationModel, isCheck: Boolean)
    }

}