package com.itechmobile.budget.ui.list

import com.itechmobile.budget.ui.mvp.PresenterBase
import java.util.*

class ListTracsationPresenter : PresenterBase<ListTransactionContract.View>(), ListTransactionContract.Presenter {
    override fun viewIsReady() {

    }

    override fun gerDateNewTransaction(position: Int): Date {
        val cl = Calendar.getInstance()!!
        cl.add(Calendar.MONTH, position - 1)
        return cl.time
    }
}