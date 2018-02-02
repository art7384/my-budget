package com.itechmobile.budget.ui.calendar.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        val txtDay2 = cellView.findViewById<TextView>(R.id.callCaldroid_TextView_day2)
        val txtDay = cellView.findViewById<TextView>(R.id.callCaldroid_TextView_day)
        val txtMany = cellView.findViewById<TextView>(R.id.callCaldroid_TextView_many)
        val read = cellView.findViewById<View>(R.id.callCaldroid_View_read)
        val green = cellView.findViewById<View>(R.id.callCaldroid_View_green)
        val content = cellView.findViewById<View>(R.id.callCaldroid_View_content)

        val da = this.datetimeList[position]

        txtDay.text = "" + da.day
        txtDay2.text = da.day.toString()

        val date = Date(da.year - 1900, da.month - 1, da.day)

        txtMany.text = ""

        if (TransactionService.INSTANCE.getDayPl(date.time) != 0) {
            if (green.visibility != View.VISIBLE) green.visibility = View.VISIBLE
        } else {
            if (green.visibility != View.GONE) green.visibility = View.GONE
        }

        if (TransactionService.INSTANCE.getDayMn(date.time) != 0) {
            if (read.visibility != View.VISIBLE) read.visibility = View.VISIBLE
        } else {
            if (read.visibility != View.GONE) read.visibility = View.GONE
        }

        if (month != date.month + 1) {
            txtDay.alpha = .4f
            txtMany.alpha = .4f
            read.alpha = .4f
            green.alpha = .4f
        }

        val sum = TransactionService.INSTANCE.getSumTo(date.time)
        var strSum = sum.toString()

        if (sum >= 1000 || sum <= -1000) {
            val n = Math.floor(sum.toDouble() / 100) / 10
            strSum = n.toString() + " т"
        }
        var colorManyTxt = App.instance.resources.getColor(R.color.many_sum_pl)
        if (sum < 0) {
            colorManyTxt = App.instance.resources.getColor(R.color.many_sum_mn)
        }

        txtMany.text = strSum
        if (sum == 0L) {
            txtMany.text = ""
        } else if (txtMany.textColors.defaultColor != colorManyTxt) {
            txtMany.setTextColor(colorManyTxt)
        }

        val activeDate = Date(sActiveTime)

        if (activeDate.year == date.year && activeDate.month == date.month && activeDate.date == date.date) {
            content.setBackgroundColor(resources.getColor(R.color.colorAccent))
            txtDay2.visibility = View.VISIBLE
        } else {
            if (txtDay2.visibility != View.INVISIBLE) txtDay2.visibility = View.INVISIBLE
            content.setBackgroundColor(Color.WHITE)
            txtMany.setTextColor(colorManyTxt)
        }

        return cellView
    }

}