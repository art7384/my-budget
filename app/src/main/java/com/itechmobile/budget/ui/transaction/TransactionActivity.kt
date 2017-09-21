package com.itechmobile.budget.ui.transaction

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.TransactionService
import com.itechmobile.budget.model.TracsationModel
import java.util.*

/**
 * Created by artem on 29.07.17.
 */
class TransactionActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btDate: Button
    private lateinit var btAdd: Button
    private lateinit var txtMany: EditText
    private lateinit var txtName: EditText
    private lateinit var cbIsDone: CheckBox
    private lateinit var spInterval: Spinner

    private lateinit var mTracsationModel: TracsationModel
    private lateinit var mDate: Date
    private var isAdd = true
    private var isUpdate = false

    companion object {
        private val LOG_TAG = "TransactionActivity"
        val EXTTRA_DATA = "data"
        val EXTTRA_MANY_ID = "many_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        uiInit()

        val time = intent.getLongExtra(EXTTRA_DATA, 0)
        val manyId = intent.getLongExtra(EXTTRA_MANY_ID, -1)

        if(manyId >= 0){
            isAdd = false
            isUpdate = true
            initUpdata(manyId)
        } else {
            isAdd = true
            isUpdate = false
            initAdd(time)
        }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.activityTransaction_Button_date -> {
                showDateDialog()
            }
            R.id.activityTransaction_Button_add -> {

                if(txtMany.text.isEmpty()){
                    toast("Цена не заполнена")
                    return
                }

                val many = txtMany.text.toString().toInt()

                // Проверяем заполнение полей
                if(many == 0){
                    toast("Цена не должна быть 0")
                    return
                }
                if(txtName.text.isEmpty()){
                    toast("Имя не заполнено")
                    return
                }
                if(isAdd) {
                    // Добавляем в базу
                    val model = TracsationModel(many, txtName.text.toString(), mDate.time)
                    model.isDone = cbIsDone.isChecked
                    TransactionService.INSTANCE.save(model)

                    // Возвращаем результат в виде даты (чтоб открылся тот день на который мы добавили новую транзакцию)
                    val intent = Intent()
                    intent.putExtra(EXTTRA_DATA, mDate.time)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else if(isUpdate) {
                    // Обновляем в базе
                    mTracsationModel.many = many
                    mTracsationModel.name = txtName.text.toString()
                    mTracsationModel.time = mDate.time
                    mTracsationModel.isDone = cbIsDone.isChecked
                    TransactionService.INSTANCE.update(mTracsationModel)
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private fun updateDateForButton(date: Date) {
        mDate = date
        var strMonth = "" + (date.month + 1)
        if (strMonth.length < 2) {
            strMonth = "0" + strMonth
        }
        btDate.setText("" + date.date + "." + strMonth + "." + (date.year + 1900))
    }

    private fun initAdd(time : Long){
        mDate = Date(time)
        updateDateForButton(mDate)

        val intervalArr = resources.getStringArray(R.array.interval)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, intervalArr)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spInterval.adapter = adapter
        spInterval.setOnItemSelectedListener(MyOnItemSelectedListener(this))

        val d = Date()
        val date = Date(d.year, d.month, d.date)

        if (mDate.time < date.time) {
            cbIsDone.isChecked = true
            //cbIsDone.isEnabled = false
        } else if (mDate.time > date.time) {
            cbIsDone.isChecked = false
            //cbIsDone.isEnabled = false
        }
    }

    private fun initUpdata(manyId: Long){
        mTracsationModel = TransactionService.INSTANCE.getFoeId(manyId)

        spInterval.visibility = View.GONE
        cbIsDone.isChecked = mTracsationModel.isDone
        updateDateForButton(Date(mTracsationModel.time))
        txtMany.setText("${mTracsationModel.many}")
        txtName.setText(mTracsationModel.name)
        btAdd.text = "UPDATE"

    }

    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun uiInit() {
        btDate = findViewById(R.id.activityTransaction_Button_date)
        btDate.setOnClickListener(this)
        btAdd = findViewById(R.id.activityTransaction_Button_add)
        btAdd.setOnClickListener(this)
        txtMany = findViewById(R.id.activityTransaction_EditText_many)
        txtName = findViewById(R.id.activityTransaction_EditText_name)
        cbIsDone = findViewById(R.id.activityTransaction_CheckBox_done)
        spInterval = findViewById(R.id.activityTransaction_Spinner_interval)

    }

    private fun showDateDialog(){
        DatePickerDialog(this,
                MyOnDateSetListener(this),
                mDate.year + 1900,
                mDate.month,
                mDate.date).show()
    }

    private class MyOnDateSetListener(private val myActivity: TransactionActivity) : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            val date = Date(year - 1900, monthOfYear, dayOfMonth)
            myActivity.initAdd(date.time)
        }
    }

    private class MyOnItemSelectedListener(private val myActivity: TransactionActivity) : OnItemSelectedListener {
        override fun onNothingSelected(arg: AdapterView<*>) {

        }

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

        }

    }

}