package com.itechmobile.budget.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.itechmobile.budget.R
import com.itechmobile.budget.ui.calendar.helpers.CaldroidAdapter
import com.itechmobile.budget.ui.calendar.helpers.WeekdayAdapter
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidGridAdapter
import com.roomorama.caldroid.CaldroidListener
import java.util.*

/**
 * Created by artem on 24.07.17.
 */

class MyCaldroidFragment : CaldroidFragment() {


    var onSelectDateListner: ((date: Date) -> Unit)? = null
    private lateinit var mCalendarAdapter: CaldroidAdapter

    override fun getNewDatesGridAdapter(month: Int, year: Int): CaldroidGridAdapter {
        // TODO Auto-generated method stub
        mCalendarAdapter = CaldroidAdapter(activity, month, year,
                getCaldroidData(), extraData)
        return mCalendarAdapter
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val cal = Calendar.getInstance()
        val args = Bundle()
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1)
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR))
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY)
        args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark)
        arguments = args
        caldroidListener = MyCaldroidListener(this)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        refreshView()

    }

    private class MyCaldroidListener(val fragment: MyCaldroidFragment) : CaldroidListener() {

        override fun onSelectDate(date: Date?, view: View?) {
            if(date != null && view != null) {
//                view.findViewById<View>(R.id.callCaldroid_View_content).setBackgroundColor(App.Companion.instance.resources.getColor(R.color.color700))
//                view.findViewById<TextView>(R.id.callCaldroid_TextView_day).setTextColor(Color.WHITE)
//                view.findViewById<TextView>(R.id.callCaldroid_TextView_many).setTextColor(Color.WHITE)
//                view.findViewById<View>(R.id.callCaldroid_View_read).visibility = View.GONE
//                view.findViewById<View>(R.id.callCaldroid_View_green).visibility = View.GONE
                fragment.mCalendarAdapter.activeTime = date.time
                fragment.onSelectDateListner?.let { it(date) }
                fragment.refreshView()
            }

//            val intent = Intent(fragment.activity, DayActivity::class.java)
//            if (date != null) {
//                intent.putExtra(DayActivity.EXTTRA_DATA, date.time)
//            }
//            fragment.activity.startActivity(intent)
        }

        override fun onCaldroidViewCreated() {
            fragment.weekdayGridView.adapter = WeekdayAdapter()
            fragment.leftArrowButton.setBackgroundResource(R.mipmap.ic_navigate_before_white_48dp)
            fragment.rightArrowButton.setBackgroundResource(R.mipmap.ic_navigate_next_white_48dp)
        }
    }

}