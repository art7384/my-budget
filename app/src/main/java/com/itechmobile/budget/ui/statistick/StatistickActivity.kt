package com.itechmobile.budget.ui.statistick

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.TransactionService
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by artem on 29.01.18.
 */
class StatistickActivity : AppCompatActivity() {

    private lateinit var mViewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistick)

        val thisDate = Date()
        val startDate = TransactionService.INSTANCE.startDate

        val listDate = ArrayList<Date>()

        var itemDate = Date(startDate.year, startDate.month, 1)
        while (itemDate.time < thisDate.time) {
            listDate.add(itemDate)
            itemDate = Date(itemDate.year, itemDate.month + 1, 1)
        }

        mViewPager = findViewById(R.id.container)
        mViewPager.adapter = SectionsPagerAdapter(supportFragmentManager, listDate)
        mViewPager.currentItem = listDate.size - 1
        mViewPager.setOnPageChangeListener(MyOnPageChangeListener(this, listDate))

    }

    inner class SectionsPagerAdapter(fm: FragmentManager, val items: ArrayList<Date>) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            val item = items[position]
            return StatistickFragment.create(item, Date(item.year, item.month + 1, 1))
        }

        override fun getCount(): Int {
            return items.size
        }
    }

    class MyOnPageChangeListener(val activity: StatistickActivity, val items: ArrayList<Date>) : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {

            val months = activity.resources.getStringArray(R.array.months_)
            activity.supportActionBar!!.title = "Статистика ${months[items[position].month]}"
        }

    }

}