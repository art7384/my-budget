package com.itechmobile.budget.ui.statistick

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.transaction.TransactionService
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.Bundler
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Статистика"
        supportActionBar?.elevation = 0f

        val thisDate = Date()
        val startDate = TransactionService.INSTANCE.startDate

        val listDate = ArrayList<Date>()

        var itemDate = Date(startDate.year, startDate.month, 1)
        while (itemDate.time < thisDate.time) {
            listDate.add(itemDate)
            itemDate = Date(itemDate.year, itemDate.month + 1, 1)
        }

        val months = resources.getStringArray(R.array.months_)

        val creator = FragmentPagerItems.with(this)
        for (d in listDate) {
            var m = d.month + 1
            var y = d.year
            if(m > 11) {
                m = 0
                y += 1
            }
            creator.add(months[d.month], StatistickFragment::class.java,  Bundler()
                    .putLong(StatistickFragment.START_TIME, d.time)
                    .putLong(StatistickFragment.STOP_TIME, Date(y, m, 1).time)
                    .get())
        }

        mViewPager = findViewById(R.id.container)
        mViewPager.adapter = FragmentPagerItemAdapter(supportFragmentManager, creator.create())

        val viewPagerTab = findViewById<View>(R.id.viewpagertab) as SmartTabLayout
        viewPagerTab.setViewPager(mViewPager)

        mViewPager.currentItem = listDate.size - 1

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
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
            activity.supportActionBar?.title = "Статистика ${months[items[position].month]}"
        }

    }

}