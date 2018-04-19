package com.itechmobile.budget.ui.regular

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.RegularService
import com.itechmobile.budget.ui.regular.helpers.RegularAdapter


class RegularActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView

    companion object {
        private const val LOG_TAG = "RegularActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regular)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = resources.getString(R.string.title_statistic)
        supportActionBar?.elevation = 0f

        mRecyclerView = findViewById(R.id.listRegular)

        val arrRegulars = RegularService.INSTANCE.getAll()
        val adapter = RegularAdapter()
        adapter.update(arrRegulars)
        adapter.onClickItemListner = {
            Log.d(LOG_TAG, it.toString())
        }
        adapter.onClickTitleListner = { isMn, title ->
            Log.d(LOG_TAG, "$title isMn:$isMn")
        }
        mRecyclerView.adapter = adapter

    }

}