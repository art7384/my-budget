package com.itechmobile.budget.ui.calendar.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.metalab.asyncawait.async
import com.itechmobile.budget.App
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.TransactionService
import com.roomorama.caldroid.CaldroidGridAdapter
import java.util.*

/**
 * Created by artem on 24.07.17.
 */
class CaldroidAdapter(context: Context,
                      month: Int,
                      year: Int,
                      caldroidData: Map<String, Any>,
                      extraData: Map<String, Any>) : CaldroidGridAdapter(context, month, year, caldroidData, extraData) {

    var activeTime: Long
        get() = sActiveTime
        set(value) {
            sActiveTime = value
        }

    companion object {
        private var sActiveTime = Date().time
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n", "ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val cellView: View

        cellView = convertView ?: inflater.inflate(R.layout.cell_caldroid, null)

        val incomeView = cellView.findViewById<View>(R.id.income)
        val flowView = cellView.findViewById<View>(R.id.flow)
        val dateText = cellView.findViewById<TextView>(R.id.date)
        val contMarker = cellView.findViewById<View>(R.id.containerMarker)

        val da = this.datetimeList[position]
        val date = Date(da.year - 1900, da.month - 1, da.day)

        dateText.text = da.day.toString()

        if (incomeView.visibility != View.GONE) incomeView.visibility = View.GONE
        if (flowView.visibility != View.GONE) flowView.visibility = View.GONE

        if (month != da.month) {
            if (dateText.alpha != .1f) dateText.alpha = .1f
            if (contMarker.visibility != View.INVISIBLE) contMarker.visibility = View.INVISIBLE
        } else {
            if (dateText.alpha != 1f) dateText.alpha = 1f
            val activeDate = Date(sActiveTime)
            if (activeDate.year == date.year && activeDate.month == date.month && activeDate.date == date.date) {
                dateText.setTextColor(0xffffffff.toInt())
                dateText.setBackgroundResource(R.drawable.oval_active)
                if (contMarker.visibility != View.INVISIBLE) contMarker.visibility = View.INVISIBLE
            } else {
                dateText.setTextColor(App.instance.resources.getColor(R.color.text))
                dateText.setBackgroundResource(0)
                if (contMarker.visibility != View.VISIBLE) contMarker.visibility = View.VISIBLE
            }
        }

        async {

            val dayPl = await { TransactionService.INSTANCE.getDayPl(date) }
            if (dayPl != 0) {
                if (incomeView.visibility != View.VISIBLE) incomeView.visibility = View.VISIBLE
            } else {
                if (incomeView.visibility != View.GONE) incomeView.visibility = View.GONE
            }

            val dayMn = await { TransactionService.INSTANCE.getDayMn(date) }
            if (dayMn != 0) {
                if (flowView.visibility != View.VISIBLE) flowView.visibility = View.VISIBLE
            } else {
                if (flowView.visibility != View.GONE) flowView.visibility = View.GONE
            }

            val sum = await { TransactionService.INSTANCE.getSumTo(date) }
            if (sum < 0 && month == da.month) {
                val activeDate = Date(sActiveTime)
                if (!(activeDate.year == date.year && activeDate.month == date.month && activeDate.date == date.date)) {
                    dateText.setTextColor(App.instance.resources.getColor(R.color.accent))
                }
            }

        }

        return cellView
    }

}