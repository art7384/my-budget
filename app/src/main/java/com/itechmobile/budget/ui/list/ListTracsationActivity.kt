package com.itechmobile.budget.ui.list

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.view.View
import com.itechmobile.budget.R
import com.itechmobile.budget.ui.calendar.CalendarActivity
import com.itechmobile.budget.ui.editor.transaction.BaseTransactionEditor
import com.itechmobile.budget.ui.editor.transaction.GreenTransactionActivity
import com.itechmobile.budget.ui.editor.transaction.YellowTransactionActivity
import com.itechmobile.budget.ui.list.helpers.MonthPagerAdapter
import java.util.*

@SuppressLint("Registered")
class ListTracsationActivity : FragmentActivity() {

    private lateinit var mPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_tracsation)

        mPager = findViewById<ViewPager>(R.id.activityMain_ViewPager_pager)
        mPager.adapter = MonthPagerAdapter(supportFragmentManager)
        findViewById<View>(R.id.activityListTransaction_FrameLayout_calendar).setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
            finish()
        }
        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        mPager.currentItem = 1

        findViewById<View>(R.id.addFlow).setOnClickListener {
            val cl = Calendar.getInstance()!!
            cl.add(Calendar.MONTH, mPager.currentItem - 1)
            val intent = Intent(this, YellowTransactionActivity::class.java)
            intent.putExtra(BaseTransactionEditor.EXTTRA_DATA, cl.time.time)
            startActivity(intent)
        }
        findViewById<View>(R.id.addIncome).setOnClickListener {
            val cl = Calendar.getInstance()!!
            cl.add(Calendar.MONTH, mPager.currentItem - 1)
            val intent = Intent(this, GreenTransactionActivity::class.java)
            intent.putExtra(BaseTransactionEditor.EXTTRA_DATA, cl.time.time)
            startActivity(intent)
        }

    }
}