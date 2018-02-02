package com.itechmobile.budget.ui.calendar.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.itechmobile.budget.App
import com.itechmobile.budget.R

/**
 * Created by artem on 29.01.18.
 */
class WeekdayAdapter : BaseAdapter() {

    val items: Array<out String> = App.instance.resources.getStringArray(R.array.weekday)!!

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_weekday, parent, false)
        }

        val txt = view!!.findViewById<TextView>(R.id.itemWeekday_TextView_txt)
        txt.text = getItem(position) as String

        return view
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

}