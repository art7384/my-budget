package com.itechmobile.budget.ui.list.helpers

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.itechmobile.budget.ui.list.fragments.DayListFromMonthFragment

class MonthPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    companion object {
        private const val PAGE_COUNT = 13
    }

    override fun getItem(position: Int): Fragment {
        return DayListFromMonthFragment.newInstance(position)
    }

    override fun getCount(): Int {
        return PAGE_COUNT
    }
}