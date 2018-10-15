package com.itechmobile.budget.ui.nodone

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.transaction.TransactionService
import com.itechmobile.budget.model.TracsationModel
import com.itechmobile.budget.ui.nodone.helpers.NoDoneAdapter
import java.util.*

/**
 * Created by artem on 29.07.17.
 */
class NoDoneActivity : AppCompatActivity() {

    lateinit var mListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_done)

        mListView = findViewById(R.id.activityNoDone_ListView_list)
        createList()
    }

    private fun createList() {
        val manyModels = TransactionService.INSTANCE.getNoDone(Date())
        val items: MutableList<TracsationModel> = ArrayList()
        items.addAll(manyModels)
        val adapter = NoDoneAdapter(this, items, OnClickBtListListner(this))
        mListView.adapter = adapter
    }

    class OnClickBtListListner(var noDoneActivity: NoDoneActivity) : NoDoneAdapter.OnClickButtonListner {

        override fun onClickDate(model: TracsationModel) {
            // TODO: запустить диодлог со сменной даты
        }

        override fun onClickOk(model: TracsationModel) {
            val adapter = noDoneActivity.mListView.adapter as NoDoneAdapter
            model.isDone = true
            TransactionService.INSTANCE.update(model)
            adapter.remove(model)
            if(adapter.getCount() == 0){
                noDoneActivity.finish()
            }
        }

        override fun onClickTh(model: TracsationModel) {
            val adapter = noDoneActivity.mListView.adapter as NoDoneAdapter
            val d = Date()
            model.date = Date(d.year, d.month, d.date)
            TransactionService.INSTANCE.update(model)
            adapter.remove(model)
            if(adapter.count == 0){
                noDoneActivity.finish()
            }
        }

    }

}