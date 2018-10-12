package com.itechmobile.budget.ui.list

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.view.View
import com.itechmobile.budget.R
import com.itechmobile.budget.ui.calendar.CalendarActivity
import com.itechmobile.budget.ui.list.helpers.MonthPagerAdapter

@SuppressLint("Registered")
class ListTracsationActivity : FragmentActivity(), ListTransactionContract.View {

    private lateinit var mPager: ViewPager
    private lateinit var mCalBt: View
    private val mPresenter: ListTransactionContract.Presenter = ListTracsationPresenter()

    private val mOnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageSelected(position: Int) { }
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }
        override fun onPageScrollStateChanged(state: Int) {}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_tracsation)

        mPresenter.attachView(this)

        intUi()
        activUi()

        mPresenter.viewIsReady()

    }

    private fun intUi() {
        mPager = this.findViewById<ViewPager>(R.id.activityMain_ViewPager_pager)
        mCalBt = findViewById<View>(R.id.activityListTransaction_FrameLayout_calendar)

    }

    private fun activUi() {

        mPager.adapter = MonthPagerAdapter(supportFragmentManager)
        mPager.currentItem = 1
        mPager.addOnPageChangeListener(mOnPageChangeListener)

        mCalBt.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
            finish()
        }
    }

    override fun startCalendarActivity(){
        startActivity(Intent(this, CalendarActivity::class.java))
        finish()
    }

}