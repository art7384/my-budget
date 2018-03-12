package com.itechmobile.budget.ui.inpocket

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.CategoryService
import com.itechmobile.budget.logick.service.TransactionService
import com.itechmobile.budget.model.TracsationModel
import com.itechmobile.budget.ui.calendar.CalendarActivity
import java.util.*

/**
 * Created by artem on 28.02.18.
 */
class InPocketActivity : AppCompatActivity(), TextView.OnEditorActionListener {


    private lateinit var mNumEt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_pocket)

        mNumEt = findViewById(R.id.summ)
        mNumEt.setOnEditorActionListener(this)

    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_GO) {

            if (v!!.text.isNotEmpty()) {
                val category = CategoryService.INSTANCE.allIncomeCategorys[0]
                val model = TracsationModel(v.text.toString().toInt(), "В кармане", Date(), category.id)
                model.isDone = true
                TransactionService.INSTANCE.save(model)
                startActivity(Intent(this, CalendarActivity::class.java))
                finish()
                overridePendingTransition(R.anim.for_alpha, R.anim.to_alpha)
            } else {
                mNumEt.setError("Сколько у Вас денег?", null)
            }

            return true
        }
        return false
    }

}