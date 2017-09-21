package com.itechmobile.budget.ui.day

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.TransactionService
import com.itechmobile.budget.model.TracsationModel
import com.itechmobile.budget.ui.calendar.MainActivity
import com.itechmobile.budget.ui.day.helpers.TransactionListAdapter
import com.itechmobile.budget.ui.transaction.TransactionActivity
import java.util.*


/**
 * Created by artem on 24.07.17.
 */
class DayActivity : AppCompatActivity() {

    private lateinit var mList: ListView
    private var mTime: Long = 0

    companion object {
        private val LOG_TAG = "DayActivity"
        private val REQUEST_CODE_ADD_MANY_ACTIVITY = 100
        val EXTTRA_DATA = "data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mList = findViewById(R.id.activityDay_ListView_list)

        mTime = intent.getLongExtra(EXTTRA_DATA, 0)

        updateTitle(mTime)
        createList(mTime)

    }

    override fun onResume() {
        super.onResume()
        updateList(mTime)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.day, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_day_add -> {
                addMany()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ADD_MANY_ACTIVITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        return
                    }
                    mTime = data.getLongExtra(TransactionActivity.EXTTRA_DATA, 0)
                    updateTitle(mTime)
                    updateList(mTime)
                } else {
                    val tracsationModels: List<TracsationModel> = TransactionService.INSTANCE.get(mTime)
                    if (tracsationModels.isEmpty()) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun addMany() {
        val intent = Intent(this, TransactionActivity::class.java)
        intent.putExtra(TransactionActivity.EXTTRA_DATA, mTime)
        startActivityForResult(intent, REQUEST_CODE_ADD_MANY_ACTIVITY)
    }

    private fun updateMany(model: TracsationModel) {
        val intent = Intent(this, TransactionActivity::class.java)
        intent.putExtra(TransactionActivity.EXTTRA_MANY_ID, model.id)
        startActivity(intent)
    }

    private fun perfom(model: TracsationModel) {

        TransactionService.INSTANCE.update(model)

        val adapter = mList.adapter as TransactionListAdapter
        model.isDone = !model.isDone
        adapter.update(model)

    }

    private fun move(model: TracsationModel) {
        //ojkhgkjhkj
    }

    private fun createList(time: Long) {
        val items: MutableList<TracsationModel> = ArrayList()
        val adapter = TransactionListAdapter(this, items, MyOnItemClicksListern(this))
        mList.adapter = adapter
    }

    private fun updateTitle(time: Long) {
        val date = Date(time)
        val strMonth: String?
        if (date.month < 9) {
            strMonth = "0" + (date.month + 1)
        } else {
            strMonth = "" + (date.month + 1)
        }

        supportActionBar!!.title = date.date.toString() + "." + strMonth + "." + (date.year + 1900).toString()

    }

    private fun updateList(time: Long) {
        val adapter = mList.adapter as TransactionListAdapter
        val tracsationModels: List<TracsationModel> = TransactionService.INSTANCE.get(time)
        adapter.updateList(tracsationModels)
    }

    private class MyOnItemClicksListern(private val DAY_ACTIVITY: DayActivity) : TransactionListAdapter.OnItemClicksListern {
        override fun onClickDell(adapter: TransactionListAdapter, model: TracsationModel) {
            TransactionService.INSTANCE.dell(model.id)
            adapter.remove(model)
        }

        override fun onCheckedChanged(adapter: TransactionListAdapter, model: TracsationModel, isCheck: Boolean) {
            model.isDone = isCheck
            TransactionService.INSTANCE.update(model)
            adapter.update(model)
        }

        override fun onClickItem(model: TracsationModel) {
            DAY_ACTIVITY.updateMany(model)
        }
    }

}