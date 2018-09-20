package com.itechmobile.budget.ui.list.fragments

import android.annotation.SuppressLint
import com.itechmobile.budget.logick.service.TransactionService
import com.itechmobile.budget.model.TracsationFooterModel
import com.itechmobile.budget.model.TracsationHeaderModel
import com.itechmobile.budget.model.TracsationModel
import com.itechmobile.budget.ui.list.helpers.TransactionAdapter
import com.itechmobile.budget.ui.mvp.PresenterBase
import java.text.SimpleDateFormat
import java.util.*

class DayListFromMonthPresenter : PresenterBase<DayListFromMonthContract.View>(), DayListFromMonthContract.Presenter {

    private val mCl = Calendar.getInstance()!!
    var mPageNum = 0
    override var pageNum: Int
        get() = mPageNum
        set(value) {
            mPageNum = value
            mCl.add(Calendar.MONTH, mPageNum - 1)

        }

    @SuppressLint("SimpleDateFormat")
    override fun viewIsReady() {
        view?.title = SimpleDateFormat("LLLL yyyy").format(mCl.time)
        val items = arrayListOf<TransactionAdapter.IItem>()

        val arrTransactions = TransactionService.INSTANCE.getForMonth(mCl.time)
        var itemsOld = TracsationModel()
        for (itemTransaction in arrTransactions) {
            if (items.size == 0) { // если первая транзакция
                // добавляем заголовок
                items.add(TracsationHeaderModel(itemTransaction.date))
                // добовляем транзакцию
                items.add(itemTransaction)
            } else {
                if (itemsOld.date.date == itemTransaction.date.date) { // если текущая транзакция за то же число что и прошлая транзакция
                    // добовляем транзакцию
                    items.add(itemTransaction)
                } else {
                    // добавлякм подвал
                    items.add(TracsationFooterModel(TransactionService.INSTANCE.getSumTo(itemsOld.date)))
                    // добовляем заголовок
                    items.add(TracsationHeaderModel(itemTransaction.date))
                    // добовляем транзакцию
                    items.add(itemTransaction)
                }
            }
            itemsOld = itemTransaction
        }
        if (items.size > 0) { // если транзакции добавлены
            // добавлякм подвал
            items.add(TracsationFooterModel(TransactionService.INSTANCE.getSumTo(itemsOld.date)))
        }

        view?.newTransaction(items)
    }
}