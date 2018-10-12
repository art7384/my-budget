package com.itechmobile.budget.ui.list

import com.itechmobile.budget.ui.mvp.MvpPresenter
import com.itechmobile.budget.ui.mvp.MvpView
import java.util.*

interface ListTransactionContract {
    interface View : MvpView {
        fun startCalendarActivity()
    }

    interface Presenter : MvpPresenter<View> {
        fun gerDateNewTransaction(position: Int): Date
    }
}