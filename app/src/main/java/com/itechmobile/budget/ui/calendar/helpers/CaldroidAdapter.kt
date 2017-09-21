package com.itechmobile.budget.ui.calendar.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Handler
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

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val cellView: View

        if (convertView == null) cellView = inflater.inflate(R.layout.cell_caldroid, null)
        else cellView = convertView

        val txtDay = cellView.findViewById<TextView>(R.id.callCaldroid_TextView_day)
        val txtMany = cellView.findViewById<TextView>(R.id.callCaldroid_TextView_many)
        val txtManyPl = cellView.findViewById<TextView>(R.id.callCaldroid_TextView_many_pl)
        val txtManyMn = cellView.findViewById<TextView>(R.id.callCaldroid_TextView_many_mn)
        val border = cellView.findViewById<View>(R.id.callCaldroid_View_border)

        val date = this.datetimeList.get(position)

        txtDay.text = "" + date.day

        val d = Date(date.year - 1900, date.month - 1, date.day)
        val thisDate = Date()

        txtMany.text = ""
        txtManyPl.text = ""
        txtManyMn.text = ""

        if(month == date.month) {
            txtDay.setTextColor(App.instance.getColor(R.color.day))
            if(thisDate.year == d.year && thisDate.date == d.date){
                if(border.visibility != View.VISIBLE){
                    border.visibility = View.VISIBLE
                }
            } else {
                if(border.visibility != View.GONE){
                    border.visibility = View.GONE
                }
            }
        } else {
            txtDay.setTextColor(App.instance.getColor(R.color.day_p))
            txtMany.alpha = .4f
            txtManyPl.alpha = .4f
            txtManyMn.alpha = .4f
        }

        val handler = Handler()
        Thread{

            val manyModels = TransactionService.INSTANCE.get(d.time)

            var pl = 0
            var mn = 0

            for (model in manyModels) {
                if(model.many < 0){
                    mn += model.many
                } else {
                    pl += model.many
                }
            }

            var strMn = ""
            if(mn != 0) {
                strMn = mn.toString()
            }

            var strPl = ""
            if(pl != 0) {
                strPl = "+" + pl.toString()
            }

            handler.post {
                txtManyPl.text = strPl
                txtManyMn.text = strMn

            }
        }.start()

        Thread{
            val sum =  TransactionService.INSTANCE.getSumTo(d.time)
            var strSum = sum.toString()

            if(sum >= 1000 || sum <= -1000){
                val n = Math.floor(sum.toDouble() / 100) / 10
                strSum = n.toString() + " т"
            }
            var color = App.instance.getColor(R.color.many_sum_pl)
            if(sum < 0){
                color = App.instance.getColor(R.color.many_sum_mn)
            }

            handler.post {
                txtMany.text = strSum
                if(sum == 0L){
                    txtMany.text = ""
                } else if(txtMany.textColors.defaultColor != color) {
                    txtMany.setTextColor(color)
                }
            }
        }.start()

        return cellView
    }

}