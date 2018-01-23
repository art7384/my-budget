package com.itechmobile.budget.ui.calendar

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.itechmobile.budget.App
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.TransactionService
import com.itechmobile.budget.logick.service.UserService
import com.itechmobile.budget.ui.day.DayActivity
import com.itechmobile.budget.ui.editor.TransactionEditorActivity
import com.itechmobile.budget.ui.nodone.NoDoneActivity
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidListener
import java.util.*

class MainActivity : AppCompatActivity() {

    private val mCaldroidFragment = MyCaldroidFragment()
    private lateinit var mTxt: TextView
    private lateinit var mAddBt: ImageButton

    companion object {
        private val LOG_TAG = "MainActivity"
    }

    private val CALDROID_LISTENER = MyCaldroidListener(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTxt = findViewById(R.id.activityMain_TextView_many)
        mAddBt = findViewById(R.id.activityMain_ImageButton_add)

        mAddBt.setOnClickListener {
            val intent = Intent(this, TransactionEditorActivity::class.java)
            intent.putExtra(TransactionEditorActivity.EXTTRA_DATA, Date().time)
            startActivity(intent)
        }

        UserService.INSTANCE.appStart()
        initCalendar()
        showNoDone()

        val models = TransactionService.INSTANCE.getNoDone(Date().time)
        if (!models.isEmpty()) {
            val intent = Intent(this, NoDoneActivity::class.java)
            startActivity(intent)
        }

        val args = Bundle()
        args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark)
        mCaldroidFragment.arguments = args

    }

    override fun onResume() {
        super.onResume()

        mCaldroidFragment.refreshView()
        mTxt.text = TransactionService.INSTANCE.getDoneSumTo(Date().time).toString()

    }

    private fun showNoDone() {

    }

    private fun initCalendar() {

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

    private class MyCaldroidListener(val cnx: MainActivity) : CaldroidListener() {

        override fun onSelectDate(date: Date?, view: View?) {
            val intent = Intent(cnx, DayActivity::class.java)
            if (date != null) {
                intent.putExtra(DayActivity.EXTTRA_DATA, date.time)
            }
            cnx.startActivity(intent)
        }

        override fun onCaldroidViewCreated() {

            cnx.mCaldroidFragment.weekdayGridView.adapter = WeekdayAdapter()

            val leftButton = cnx.mCaldroidFragment.leftArrowButton
            val rightButton = cnx.mCaldroidFragment.rightArrowButton

            leftButton.setBackgroundResource(R.mipmap.ic_navigate_before_white_48dp)
            rightButton.setBackgroundResource(R.mipmap.ic_navigate_next_white_48dp)

        }
    }

    private class WeekdayAdapter : BaseAdapter() {

        val items: Array<out String> = App.instance.resources.getStringArray(R.array.weekday)!!

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = convertView
            if (view == null) {
                view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_weekday, parent, false)
            }

            val txt = view!!.findViewById<TextView>(R.id.itemWeekday_TextView_txt)
            txt.text = getItem(position) as String

            return view
        }

        override fun getItem(position: Int): Any {
            return items[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return items.size
        }

    }


}
