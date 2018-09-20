package com.itechmobile.budget.ui.calendar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ListView
import android.widget.TextView
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.TransactionService
import com.itechmobile.budget.model.TracsationModel
import com.itechmobile.budget.ui.calendar.helpers.TransactionListAdapter
import com.itechmobile.budget.ui.editor.transaction.BaseTransactionEditor
import com.itechmobile.budget.ui.editor.transaction.GreenTransactionActivity
import com.itechmobile.budget.ui.editor.transaction.YellowTransactionActivity
import com.itechmobile.budget.ui.statistick.StatistickActivity
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by artem on 01.03.18.
 */
class CalendarActivity : AppCompatActivity() {

    private lateinit var mTitleTxt: TextView
    private lateinit var mSumTxt: TextView
    private lateinit var mAddFlowBt: FloatingActionButton
    private lateinit var mAddIncomeBt: FloatingActionButton
    private lateinit var mCalContent: View
    private lateinit var mTransactionsList: ListView
    private lateinit var mListAdapter: TransactionListAdapter

    private var mDate = Date()

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        mTitleTxt = findViewById(R.id.title)
        mSumTxt = findViewById(R.id.summ)
        mAddFlowBt = findViewById(R.id.addFlow)
        mAddIncomeBt = findViewById(R.id.addIncome)
        mCalContent = findViewById(R.id.contenerCalendar)
        mTransactionsList = findViewById(R.id.listTransactions)
        mListAdapter = TransactionListAdapter(this)
        mTransactionsList.adapter = mListAdapter
        mTransactionsList.setOnItemClickListener { parent, view, position, id ->
            val model = TransactionService.INSTANCE.get(id) ?: return@setOnItemClickListener
            val intent = if (model.price < 0) Intent(this, YellowTransactionActivity::class.java)
            else Intent(this, GreenTransactionActivity::class.java)
            intent.putExtra(BaseTransactionEditor.EXTTRA_MANY_ID, model.id)
            startActivity(intent)
        }
        findViewById<View>(R.id.statistick).setOnClickListener {
            startActivity(Intent(this, StatistickActivity::class.java))
        }

        mTransactionsList.addFooterView(layoutInflater.inflate(R.layout.view_footer, null))

        mCalContent.alpha = 0f

        mAddFlowBt.animation = AnimationUtils.loadAnimation(this, R.anim.start_bt)
        mAddIncomeBt.animation = AnimationUtils.loadAnimation(this, R.anim.start_bt2)

        mAddFlowBt.setOnClickListener {
            val intent = Intent(this, YellowTransactionActivity::class.java)
            intent.putExtra(BaseTransactionEditor.EXTTRA_DATA, mDate.time)
            startActivity(intent)
        }
        mAddIncomeBt.setOnClickListener {
            val intent = Intent(this, GreenTransactionActivity::class.java)
            intent.putExtra(BaseTransactionEditor.EXTTRA_DATA, mDate.time)
            startActivity(intent)
        }

        initCalendar()

        mSumTxt.text = TransactionService.INSTANCE.getSumTo(mDate).toString()

        val calContent = findViewById<CardView>(R.id.card_view)
        val anim = AnimationUtils.loadAnimation(this, R.anim.start_calrndar)
        anim.setAnimationListener(CalendarAnimationListener(this))
        calContent.animation = anim

    }

    override fun onRestart() {
        super.onRestart()
        updateTransaction(mDate)
    }

    private fun initCalendar() {
        val t = supportFragmentManager.beginTransaction()
        val fragment = MyCaldroidFragment()
        fragment.onSelectDateListner = { date ->
            mDate = date
            updateTitle(date)
            updateTransaction(date)
            mSumTxt.text = TransactionService.INSTANCE.getSumTo(date).toString()
        }
        t.replace(R.id.contenerCalendar, fragment)
        t.commit()
    }

    private fun updateTransaction(date: Date) {
        val tracsationModels: List<TracsationModel> = TransactionService.INSTANCE.getDay(date)
        mListAdapter.updateList(tracsationModels)
        if (tracsationModels.isNotEmpty()) {
            setListViewHeight()
        }
    }

    // Изменяем высату list view для того чтобы нормально работал скрол
    private fun setListViewHeight() {
        val listAdapter = mTransactionsList.adapter
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(mTransactionsList.width, View.MeasureSpec.UNSPECIFIED)
        var totalHeight = 0
        var view: View? = null
        for (i in 0 until listAdapter.count) {
            view = listAdapter.getView(i, view, mTransactionsList)
            if (i == 0) view.layoutParams = ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT)

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += view.measuredHeight
        }

        val params: ViewGroup.LayoutParams = mTransactionsList.layoutParams

        params.height = totalHeight + (mTransactionsList.dividerHeight * (listAdapter.count - 1))

        mTransactionsList.layoutParams = params
        mTransactionsList.requestLayout()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun updateTitle(date: Date) {
        val sdf = SimpleDateFormat("EEEE, d MMMM")
        val str = sdf.format(date)
        mTitleTxt.text = str.substring(0, 1).toUpperCase().toUpperCase() + str.subSequence(1, str.length)
    }

    private class CalendarAnimationListener(val activity: CalendarActivity) : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {}
        override fun onAnimationRepeat(animation: Animation?) {}
        override fun onAnimationEnd(animation: Animation?) {
            activity.mCalContent.alpha = 1f
        }
    }

}
