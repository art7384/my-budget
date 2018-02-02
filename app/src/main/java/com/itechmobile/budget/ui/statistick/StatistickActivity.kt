package com.itechmobile.budget.ui.statistick

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.itechmobile.budget.R


/**
 * Created by artem on 29.01.18.
 */
class StatistickActivity: AppCompatActivity() {

    private lateinit var mCart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistick)

        mCart = findViewById(R.id.activityStatistick_PieChart_chart)

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(10f, 20f))
        entries.add(PieEntry(30f, 40f))

        val dataSet = PieDataSet(entries, "Label"); // add entries to dataset
        dataSet.color = Color.RED
        dataSet.valueTextColor = Color.BLACK

        val lineData = PieData(dataSet)
        mCart.data = lineData
        mCart.invalidate()

    }
}