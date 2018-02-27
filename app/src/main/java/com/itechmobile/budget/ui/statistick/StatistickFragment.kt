package com.itechmobile.budget.ui.statistick

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.CategoryService
import com.itechmobile.budget.logick.service.TransactionService
import java.util.*

/**
 * Created by artem on 26.02.18.
 */
class StatistickFragment
@SuppressLint("ValidFragment")
private constructor() : Fragment() {

    private var mStartLong = 0L
    private var mStopLong = 0L

    companion object {
        fun create(startLong: Long, stopLong: Long): StatistickFragment {
            val f = StatistickFragment()
            f.mStartLong = startLong
            f.mStopLong = stopLong
            return f
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.statistick_fragment, null)

        initPieChartMn(view)
        initPieChartPl(view)

        return view

    }

    private fun initPieChartMn(v: View) {
        val pieChart = v.findViewById<PieChart>(R.id.fragmentStatistick_PieChart_chartMn)
        pieChart.data = getPieData(true)
        pieChart.centerText = "${getSumAll(true)}"
        setingpieChart(pieChart)
    }

    private fun initPieChartPl(v: View) {
        val pieChart = v.findViewById<PieChart>(R.id.fragmentStatistick_PieChart_chartPl)
        pieChart.data = getPieData(false)
        pieChart.centerText = "${getSumAll(false)}"
        setingpieChart(pieChart)
    }

    private fun setingpieChart(pieChart: PieChart){

        val desc = Description()
        desc.text = ""
        pieChart.description = desc

        pieChart.legend.isEnabled = false
        pieChart.invalidate()
        pieChart.setNoDataText("")

    }

    private fun getSumAll(isMn: Boolean): Long{
        val sumHasMap = TransactionService.INSTANCE.getCategorySum(mStartLong, mStopLong)
        val categoryIds = sumHasMap.keys
        return categoryIds
                .mapNotNull { sumHasMap[it] }
                .filter { if(isMn)  it < 0 else it > 0} // если расход или доход
                .sum()
    }

    private fun getPieData(isMn: Boolean): PieData{

        val entries = ArrayList<PieEntry>()
        val sumHasMap = TransactionService.INSTANCE.getCategorySum(mStartLong, mStopLong)
        val categoryIds = sumHasMap.keys

        // находим сумму всех транзакций
        val sum = getSumAll(isMn)

        // объединенные транзакции
        var sumOther = 0f
        var nameOther = ""

        for (categoryId in categoryIds) {
            val many = sumHasMap[categoryId]
            val categoryModel = CategoryService.INSTANCE.get(categoryId)
            if (many != null) {
                val k = many.toFloat() / sum.toFloat()
                if(isMn) { // если расходы
                    if (many < 0L) {
                        if (k < .07) { //меньше 7% (<.1) объединяем
                            sumOther += (many * -1).toFloat()
                            nameOther += categoryModel.icoName
                        } else {
                            entries.add(PieEntry((many * -1).toFloat(), categoryModel.icoName))
                        }
                    }
                } else { // если даходы
                    if (many > 0L) {
                        if (k < .07) { //меньше 7% (<.1) объединяем
                            sumOther += many.toFloat()
                            nameOther += categoryModel.icoName
                        } else {
                            entries.add(PieEntry(many.toFloat(), categoryModel.icoName))
                        }
                    }
                }
            }
        }
        if (sumOther > 0) {
            entries.add(PieEntry(sumOther, nameOther))
        }

        val dataSet = createPieDataSet(entries)

        return PieData(dataSet)
    }

    private fun createPieDataSet(entries: ArrayList<PieEntry>): PieDataSet {
        val dataSet = PieDataSet(entries, "") // add entries to dataset
        dataSet.setColors(resources.getColor(R.color.pie_chart_1),
                resources.getColor(R.color.pie_chart_2),
                resources.getColor(R.color.pie_chart_3),
                resources.getColor(R.color.pie_chart_4),
                resources.getColor(R.color.pie_chart_5),
                resources.getColor(R.color.pie_chart_6),
                resources.getColor(R.color.pie_chart_7),
                resources.getColor(R.color.pie_chart_8),
                resources.getColor(R.color.pie_chart_9),
                resources.getColor(R.color.pie_chart_10),
                resources.getColor(R.color.pie_chart_11),
                resources.getColor(R.color.pie_chart_12))
        dataSet.valueTextColor = Color.WHITE
        return dataSet
    }


}

