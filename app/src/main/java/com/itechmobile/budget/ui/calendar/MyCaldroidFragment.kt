package com.itechmobile.budget.ui.calendar

import com.itechmobile.budget.ui.calendar.helpers.CaldroidAdapter
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidGridAdapter

/**
 * Created by artem on 24.07.17.
 */

class MyCaldroidFragment : CaldroidFragment() {
    override fun getNewDatesGridAdapter(month: Int, year: Int): CaldroidGridAdapter {
        // TODO Auto-generated method stub
        return CaldroidAdapter(activity, month, year,
                getCaldroidData(), extraData)
    }
}