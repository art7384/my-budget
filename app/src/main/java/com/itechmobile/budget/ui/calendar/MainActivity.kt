package com.itechmobile.budget.ui.calendar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.TransactionService
import com.itechmobile.budget.ui.day.DayActivity
import com.itechmobile.budget.ui.nodone.NoDoneActivity
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidListener
import java.util.*

class MainActivity : AppCompatActivity() {

    private val mCaldroidFragment = MyCaldroidFragment()

    companion object {
        private val LOG_TAG = "MainActivity"
    }
    private val CALDROID_LISTENER = MyCaldroidListener(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initCalendar()

        showNoDone()

        val models = TransactionService.INSTANCE.getNoDone(Date().time)
        if(!models.isEmpty()){
            val intent = Intent(this, NoDoneActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        mCaldroidFragment.refreshView()

    }

    private fun showNoDone() {

    }

    private fun initCalendar(){

        val cal = Calendar.getInstance()
        val args = Bundle()
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1)
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR))

        mCaldroidFragment.arguments = args

        val t = supportFragmentManager.beginTransaction()
        t.replace(R.id.activityMain_FrameLayout_calendar, mCaldroidFragment)
        t.commit()

        mCaldroidFragment.caldroidListener = CALDROID_LISTENER

    }

    private class MyCaldroidListener(val cnx: Activity) : CaldroidListener() {

        override fun onSelectDate(date: Date?, view: View?) {
            val intent = Intent(cnx, DayActivity::class.java)
            if (date != null) {
                intent.putExtra(DayActivity.EXTTRA_DATA, date.time)
            }
            cnx.startActivity(intent)
        }
    }
}
