package com.itechmobile.budget.ui.list.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.itechmobile.budget.R
import com.itechmobile.budget.ui.list.helpers.TransactionAdapter


class DayListFromMonthFragment
@SuppressLint("ValidFragment")
private constructor() : Fragment(), DayListFromMonthContract.View {

    private lateinit var mTxt: TextView
    private lateinit var mRecyclerView: RecyclerView
    private val mPresenter: DayListFromMonthContract.Presenter = DayListFromMonthPresenter()
    override var title: String
        get() = mTxt.text.toString()
        set(value) {
            mTxt.text = value
        }

    companion object {
        private const val ARGUMENT_PAGE_NUMBER = "ARGUMENT_PAGE_NUMBER"
        fun newInstance(page: Int): DayListFromMonthFragment {
            val fragment = DayListFromMonthFragment()
            val arguments = Bundle()
            arguments.putInt(ARGUMENT_PAGE_NUMBER, page)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)
        mPresenter.pageNum = arguments!!.getInt(ARGUMENT_PAGE_NUMBER)
    }

    @SuppressLint("InflateParams", "SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_day_list_from_month, null)

        initUI(view)

        mPresenter.viewIsReady()

        return view
    }

    override fun newTransaction(items: ArrayList<TransactionAdapter.IItem>) {
        (mRecyclerView.adapter as TransactionAdapter).newItems(items)
    }

    private fun initUI(view: View) {

        mTxt = view.findViewById(R.id.fragmentDayListFromMonth_TextView_text)

        mRecyclerView = view.findViewById(R.id.recycleView)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.adapter = TransactionAdapter()
    }

}