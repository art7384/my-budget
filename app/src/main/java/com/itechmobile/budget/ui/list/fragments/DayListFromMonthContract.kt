package com.itechmobile.budget.ui.list.fragments

import com.itechmobile.budget.ui.list.helpers.TransactionAdapter
import com.itechmobile.budget.ui.mvp.MvpPresenter
import com.itechmobile.budget.ui.mvp.MvpView

interface DayListFromMonthContract {

    interface View : MvpView {
        var title:String
        fun newTransaction(items: ArrayList<TransactionAdapter.IItem>)
    }

    interface Presenter : MvpPresenter<View> {
        var pageNum: Int
    }

}