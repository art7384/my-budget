package com.itechmobile.budget.ui.nodone.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.itechmobile.budget.App
import com.itechmobile.budget.R
import com.itechmobile.budget.model.TracsationModel
import java.util.*

/**
 * Created by artem on 31.07.17.
 */
class NoDoneAdapter(private var mCnx: Context,
                    private var mItems: MutableList<TracsationModel>,
                    private val mOnClickButtonListner: OnClickButtonListner
) : BaseAdapter() {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val item = getItem(position) as TracsationModel

        var v: View? = view

        if (v == null) {
            val li: LayoutInflater = mCnx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = li.inflate(R.layout.item_no_done, viewGroup, false)
        }

        val btDate = v!!.findViewById<Button>(R.id.itemNoDone_Button_date)
        val btOk = v.findViewById<Button>(R.id.itemNoDone_Button_ok)
        val btTh = v.findViewById<Button>(R.id.itemNoDone_Button_th)

        val txtMany = v.findViewById<TextView>(R.id.itemNoDone_TextView_many)
        val txtName = v.findViewById<TextView>(R.id.itemNoDone_TextView_name)

        if (item.money < 0) {
            txtMany.setTextColor(App.instance.resources.getColor(R.color.many_sum_mn))
            txtMany.text = item.money.toString()
        } else {
            txtMany.setTextColor(App.instance.resources.getColor(R.color.many_sum_pl))
            txtMany.text = "+" + item.money.toString()
        }

        txtName.text = item.name

        val date = Date(item.time)
        var strMonth = "" + (date.month + 1)
        if (strMonth.length == 1) {
            strMonth = "0" + strMonth
        }
        btDate.text = "" + date.date + "." + strMonth + "." + (date.year + 1900)
        btDate.setOnClickListener { mOnClickButtonListner.onClickDate(item) }

        btOk.setOnClickListener { mOnClickButtonListner.onClickOk(item) }

        btTh.setOnClickListener { mOnClickButtonListner.onClickTh(item) }

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

    fun add(model: TracsationModel) {
        mItems.add(0, model)
        notifyDataSetChanged()
    }

    fun updateList(items: List<TracsationModel>) {
        mItems.clear()
        mItems.addAll(items)
        notifyDataSetChanged()
    }

    fun remove(model: TracsationModel){
        var i = 0
        while (i < mItems.size) {
            if(mItems[i].id == model.id) {
                mItems.remove(mItems[i])
                break
            }
            i++
        }
        notifyDataSetChanged()
    }

    interface OnClickButtonListner {
        fun onClickDate(model: TracsationModel)
        fun onClickOk(model: TracsationModel)
        fun onClickTh(model: TracsationModel)
    }

}