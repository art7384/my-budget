package com.itechmobile.budget.ui.statistick

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.CategoryService
import com.itechmobile.budget.logick.service.TransactionService
import com.itechmobile.budget.ui.statistick.helpers.TransactionStatickTitleListAdapter
import java.util.*


/**
 * Created by artem on 26.02.18.
 */
class StatistickFragment : Fragment() {

    private var mStartDate = Date()
    private var mStopDate = Date()

    private lateinit var mPieChart: PieChart
    private lateinit var mListTransactions: ListView

    companion object {

        val START_TIME = "START_TIME"
        val STOP_TIME = "STOP_TIME"
        private val LOG_TAG = "StatistickFragment"

        fun create(start: Date, stop: Date): StatistickFragment {
            val f = StatistickFragment()
            f.mStartDate = start
            f.mStopDate = stop
            return f
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mStartDate = Date(arguments.getLong(START_TIME))
        mStopDate = Date(arguments.getLong(STOP_TIME))

        val view = inflater.inflate(R.layout.fragment_statistick, null)
        val adapter = TransactionStatickTitleListAdapter(activity)
        mListTransactions = view.findViewById(R.id.listTransactions)
        mListTransactions.adapter = adapter
        adapter.update(TransactionService.INSTANCE.getDayMn(mStartDate, mStopDate))

        initPieChart()
        mPieChart.setOnChartValueSelectedListener(MyOnChartValueSelectedListener(this, adapter))

        return view

    }

    private fun initPieChart() {

        val header = layoutInflater.inflate(R.layout.header_statistick, null)

        mPieChart = header.findViewById(R.id.chart)
        mPieChart.data = getPieData(true)
        mPieChart.centerText = "${getSumAll(true)}"
        val desc = Description()
        desc.text = ""
        mPieChart.description = desc

        mPieChart.legend.isEnabled = false

        mPieChart.invalidate()
        mPieChart.setNoDataText("")

        mListTransactions.addHeaderView(header)
    }

    private fun getSumAll(isMn: Boolean): Int {
        val sumHasMap = TransactionService.INSTANCE.getCategorySum(mStartDate, mStopDate)
        val categoryIds = sumHasMap.keys
        var sum = 0
        sumHasMap.map {
            if (isMn && it.value < 0) {
                sum += it.value
            } else if (!isMn && it.value > 0) {
                sum += it.value
            }
        }
        return sum
    }

    private fun getPieData(isMn: Boolean): PieData {

        val entries = ArrayList<PieEntry>()
        val sumHasMap = TransactionService.INSTANCE.getCategorySum(mStartDate, mStopDate)
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
                if (isMn) { // если расходы
                    if (many < 0L) {
//                        if (k < .07) { //меньше 7% (<.07) объединяем
//                            sumOther += (many * -1).toFloat()
//                            nameOther += categoryModel.icoName
//                        } else {
                            entries.add(PieEntry((many * -1).toFloat(), categoryModel.icoName, categoryModel.id))
//                        }
                    }
                } else { // если даходы
                    if (many > 0L) {
//                        if (k < .07) { //меньше 7% (<.1) объединяем
//                            sumOther += many.toFloat()
//                            nameOther += categoryModel.icoName
//                        } else {
                            entries.add(PieEntry(many.toFloat(), categoryModel.icoName, categoryModel.id))
//                        }
                    }
                }
            }
        }
//        if (sumOther > 0) {
//            entries.add(PieEntry(sumOther, nameOther, -1))
//        }

        val dataSet = createPieDataSet(entries)

        return PieData(dataSet as IPieDataSet?)
    }

    private fun createPieDataSet(entries: ArrayList<PieEntry>): PieDataSet {
        val dataSet = PieDataSet(entries, "") // save entries from dataset
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

    private class MyOnChartValueSelectedListener(val fragment: StatistickFragment, val adapter: TransactionStatickTitleListAdapter) : OnChartValueSelectedListener {
        override fun onNothingSelected() {
            adapter.update(TransactionService.INSTANCE.getDayMn(fragment.mStartDate, fragment.mStopDate))
        }

        override fun onValueSelected(e: Entry?, h: Highlight?) {

            val idCategory: Long = e!!.data as Long
            val tr = if (idCategory > -1) TransactionService.INSTANCE.getDayMn(idCategory, fragment.mStartDate, fragment.mStopDate)
            else TransactionService.INSTANCE.getDayMn(fragment.mStartDate, fragment.mStopDate)
            adapter.update(tr)
        }

    }

}

