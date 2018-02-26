package com.itechmobile.budget.ui.calendar

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.TransactionService
import com.itechmobile.budget.logick.service.UserService
import com.itechmobile.budget.model.TracsationModel
import com.itechmobile.budget.ui.calendar.helpers.TransactionListAdapter
import com.itechmobile.budget.ui.editor.TransactionEditorActivity
import com.itechmobile.budget.ui.nodone.NoDoneActivity
import com.itechmobile.budget.ui.statistick.StatistickActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mTxt: TextView
    private lateinit var mListTransactions: ListView
    private var mTime = 0L

    companion object {
        private val LOG_TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTxt = findViewById(R.id.activityMain_TextView_many)
        mListTransactions = findViewById(R.id.activityMain_ListView_transaction)
        findViewById<ImageButton>(R.id.activityMain_ImageButton_pl).setOnClickListener { addMany(true) }
        findViewById<ImageButton>(R.id.activityMain_ImageButton_mn).setOnClickListener { addMany(false) }
        findViewById<View>(R.id.activityMain_ImageButton_statistick).setOnClickListener { startActivity(Intent(this, StatistickActivity::class.java)) }

        UserService.INSTANCE.appStart()
        initCalendar()
        showNoDone()
        createListTransactions()

        val models = TransactionService.INSTANCE.getNoDone(Date().time)
        if (!models.isEmpty()) {
            val intent = Intent(this, NoDoneActivity::class.java)
            startActivity(intent)
        }

        mTime = Date().time

    }

    override fun onResume() {
        super.onResume()
        updateListTransactions()
        mTxt.text = TransactionService.INSTANCE.getSumTo(mTime).toString()

    }

    private fun addMany(isPl: Boolean) {
        val intent = Intent(this, TransactionEditorActivity::class.java)
        intent.putExtra(TransactionEditorActivity.EXTTRA_DATA, mTime)
        intent.putExtra(TransactionEditorActivity.EXTTRA_IS_PL, isPl)
        startActivity(intent)
    }

    private fun updateMany(model: TracsationModel) {
        val intent = Intent(this, TransactionEditorActivity::class.java)
        intent.putExtra(TransactionEditorActivity.EXTTRA_MANY_ID, model.id)
        startActivity(intent)
    }

    private fun showNoDone() {

    }

    private fun initCalendar() {
        val t = supportFragmentManager.beginTransaction()
        val fragment = MyCaldroidFragment()
        fragment.onSelectDateListner = { date ->
            mTime = date.time
            updateListTransactions()
            mTxt.text = TransactionService.INSTANCE.getSumTo(mTime).toString()
        }
        t.replace(R.id.activityMain_FrameLayout_calendar, fragment)
        t.commit()
    }

    private fun createListTransactions() {
        val items: MutableList<TracsationModel> = ArrayList()
        val adapter = TransactionListAdapter(this, items, MyOnItemClicksListern(this))
        mListTransactions.adapter = adapter
    }

    private fun updateListTransactions() {
        val adapter = mListTransactions.adapter as TransactionListAdapter
        val tracsationModels: List<TracsationModel> = TransactionService.INSTANCE.getDay(mTime)
        adapter.updateList(tracsationModels)
        setListViewHeight()
    }

    private fun setListViewHeight() {
        val listAdapter = mListTransactions.adapter
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(mListTransactions.width, View.MeasureSpec.UNSPECIFIED)
        var totalHeight = 0
        var view: View? = null
        for (i in 0 until listAdapter.count) {
            view = listAdapter.getView(i, view, mListTransactions)
            if (i == 0) view.layoutParams = ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT)

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += view.measuredHeight
        }

        val params: ViewGroup.LayoutParams = mListTransactions.layoutParams

        params.height = totalHeight + (mListTransactions.dividerHeight * (listAdapter.count - 1))

        mListTransactions.layoutParams = params
        mListTransactions.requestLayout()
    }

    private class MyOnItemClicksListern(private val activity: MainActivity) : TransactionListAdapter.OnItemClicksListern {
        override fun onClickMenu(adapter: TransactionListAdapter, model: TracsationModel) {
            //activity.updateMany(model)
        }

        override fun onClickItem(model: TracsationModel) {
            activity.updateMany(model)
        }
    }

}
