package com.itechmobile.budget.ui.editor

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.CategoryService
import com.itechmobile.budget.logick.service.TransactionService
import com.itechmobile.budget.model.CategoryModel
import com.itechmobile.budget.model.TracsationModel
import com.itechmobile.budget.ui.editor.category.EmojiCategoryActivity
import com.itechmobile.budget.ui.editor.helpers.EmojiGridAdapter
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by artem on 16.01.18.
 */
class TransactionEditorActivity : AppCompatActivity() {

    private lateinit var mManyTxt: TextView
    private lateinit var mZnakTxt: TextView
    private lateinit var mZnakBt: Button
    private lateinit var mSelectCategoryBt: Button
    private lateinit var mDoneCb: CheckBox
    private lateinit var mNoteEt: EditText
    private lateinit var mTitle: TextView
    private lateinit var mDelete: ImageButton
    private lateinit var mKeybord: View
    private lateinit var mCategory: View
    private lateinit var mCategoryList: GridView
    private lateinit var mCategoryTxt: TextView

    private lateinit var mTracsationModel: TracsationModel
    private lateinit var mDate: Date
    private var isAdd = true
    private var isUpdate = false

    companion object {
        val EXTTRA_DATA = "EXTTRA_DATA"
        val EXTTRA_MANY_ID = "EXTTRA_MANY_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_editor)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        init()
        initKeybord()
        initCategory()

        val d = Date(intent.getLongExtra(EXTTRA_DATA, 0))
        val time = Date(d.year, d.month, d.date).time
        val manyId = intent.getLongExtra(EXTTRA_MANY_ID, -1)

        if (manyId >= 0) {
            isAdd = false
            isUpdate = true
            initUpdata(manyId)
        } else {
            isAdd = true
            isUpdate = false
            initAdd(time)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 222){
            if(resultCode == Activity.RESULT_OK){
                val imoji = data!!.getStringExtra(EmojiCategoryActivity.EXTRA_EMOJI)
                CategoryService.INSTANCE.add(CategoryModel("", imoji, mZnakTxt.text == "+"))
                updateCategorisList()
            }
        }
    }

    private fun init() {
        mCategoryTxt = findViewById(R.id.activityTransactionEditor_TextView_category)
        mCategory = findViewById(R.id.layautCategory_LinearLayout_content)
        mManyTxt = findViewById(R.id.activityTransactionEditor_TextView_many)
        mZnakTxt = findViewById(R.id.activityTransactionEditor_TextView_znak)
        mDoneCb = findViewById<CheckBox>(R.id.activityTransactionEditor_CheckBox_done)
        mNoteEt = findViewById(R.id.activityTransactionEditor_EditText_note)
        mTitle = findViewById(R.id.activityTransactionEditor_TextView_title)
        mDelete = findViewById(R.id.activityTransactionEditor_ImageButton_delete)
        mDelete.setOnClickListener { dellTransaction() }
        findViewById<ImageButton>(R.id.activityTransactionEditor_ImageButton_dell).setOnClickListener { dellKey() }
        findViewById<ImageButton>(R.id.activityTransactionEditor_ImageButton_today).setOnClickListener { showDateDialog() }
        findViewById<ImageButton>(R.id.activityTransactionEditor_ImageButton_clear).setOnClickListener { finish() }

        mNoteEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(mNoteEt.windowToken, 0)
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                findViewById<EditText>(R.id.helper).requestFocus()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        mCategoryTxt.setOnClickListener {
            updateCategorisList()
            selectCategory()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initUpdata(manyId: Long) {
        mTracsationModel = TransactionService.INSTANCE.getFoeId(manyId)
        mDoneCb.isChecked = mTracsationModel.isDone
        mCategoryTxt.text = mTracsationModel.category
        mSelectCategoryBt.text = getString(R.string.change_category)
        updateDate(mTracsationModel.time)
        var money = mTracsationModel.money
        if (money < 0) {
            mZnakTxt.text = "-"
            mZnakBt.setText(R.string.income)
            money *= -1
            mDoneCb.setText(R.string.already_spent)
        } else {
            mZnakTxt.text = "+"
            mZnakBt.setText(R.string.consumption)
            mDoneCb.setText(R.string.already_received)
        }
        mManyTxt.text = "$money"
        mNoteEt.setText(mTracsationModel.name)
        mDoneCb.visibility = View.VISIBLE
        mCategoryTxt.visibility = View.VISIBLE
        mDelete.visibility = View.VISIBLE
    }

    private fun initAdd(time: Long) {
        updateDate(time)

        val intervalArr = resources.getStringArray(R.array.interval)
        val adapterInterval = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, intervalArr)
        adapterInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val d = Date()
        val date = Date(d.year, d.month, d.date)

        if (mDate.time < date.time) {
            mDoneCb.isChecked = true
        } else if (mDate.time > date.time) {
            mDoneCb.isChecked = false
        }
    }

    private fun initKeybord() {
        mKeybord = findViewById(R.id.layautKeyboard_LinearLayout_cont)
        findViewById<Button>(R.id.layautKeybord_Button_1).setOnClickListener { addKey(1) }
        findViewById<Button>(R.id.layautKeybord_Button_2).setOnClickListener { addKey(2) }
        findViewById<Button>(R.id.layautKeybord_Button_3).setOnClickListener { addKey(3) }
        findViewById<Button>(R.id.layautKeybord_Button_4).setOnClickListener { addKey(4) }
        findViewById<Button>(R.id.layautKeybord_Button_5).setOnClickListener { addKey(5) }
        findViewById<Button>(R.id.layautKeybord_Button_6).setOnClickListener { addKey(6) }
        findViewById<Button>(R.id.layautKeybord_Button_7).setOnClickListener { addKey(7) }
        findViewById<Button>(R.id.layautKeybord_Button_8).setOnClickListener { addKey(8) }
        findViewById<Button>(R.id.layautKeybord_Button_9).setOnClickListener { addKey(9) }
        findViewById<Button>(R.id.layautKeybord_Button_0).setOnClickListener { addKey(0) }
        findViewById<Button>(R.id.layautKeybord_Button_selectCategory)
        mSelectCategoryBt = findViewById(R.id.layautKeybord_Button_selectCategory)
        mZnakBt = findViewById(R.id.layautKeybord_Button_znak)
        mSelectCategoryBt.setOnClickListener {
            updateCategorisList()
            selectCategory()
//            if (isUpdate) {
//                //save(mCategoryTxt.text.toString())
//                updateCategorisList()
//                selectCategory()
//            } else {
//                updateCategorisList()
//                selectCategory()
//            }
        }
        mZnakBt.setOnClickListener { clickZnak() }
    }

    private fun updateCategorisList() {
        mCategoryList
        val categorys = if (mZnakTxt.text == "+") {
            CategoryService.INSTANCE.incomeCategorys
        } else {
            CategoryService.INSTANCE.expenseCategorys
        }
        val emojis = ArrayList<String>()
        categorys.mapTo(emojis) { it.icoName }

        (mCategoryList.adapter as EmojiGridAdapter).newEmoji(emojis.toTypedArray())

        mCategoryList.setOnItemClickListener { parent, view, position, id ->
            if (isUpdate) {
                mCategoryTxt.text = categorys[position].icoName
                selectKeybord()
            } else {
                save(categorys[position].icoName)
            }

        }
    }

    private fun initCategory() {
        mCategoryList = findViewById(R.id.layautKategory_GridView_emojis)

        findViewById<Button>(R.id.layautKategory_Button_addKategory).setOnClickListener {
            startActivityForResult(Intent(this, EmojiCategoryActivity::class.java), 222)
        }

        val categorys = if (mZnakTxt.text == "+") {
            CategoryService.INSTANCE.incomeCategorys
        } else {
            CategoryService.INSTANCE.expenseCategorys
        }
        val emojis = ArrayList<String>()
        categorys.mapTo(emojis) { it.icoName }

        mCategoryList.adapter = EmojiGridAdapter(emojis)
        mCategoryList.setOnItemClickListener { parent, view, position, id ->
            if (isUpdate) {
                mCategoryTxt.text = categorys[position].icoName
                selectKeybord()
            } else {
                save(categorys[position].icoName)
            }

        }
    }

    private fun save(category: String) {
        var many = mManyTxt.text.toString().toInt()
        if (mZnakTxt.text != "+") {
            many *= -1
        }
        val model = TracsationModel(many,
                mNoteEt.text.toString(),
                mDate.time,
                category)
        model.isDone = mDoneCb.isChecked
        if (isAdd) {
            TransactionService.INSTANCE.save(model)
            // Возвращаем результат в виде даты (чтоб открылся тот день на который мы добавили новую транзакцию)
            val intent = Intent()
            intent.putExtra(EXTTRA_DATA, mDate.time)
            setResult(Activity.RESULT_OK, intent)
        } else {
            model.id = mTracsationModel.id
            TransactionService.INSTANCE.update(model)
            setResult(Activity.RESULT_OK)
        }
        finish()
    }

    private fun showDateDialog() {
        DatePickerDialog(this,
                MyOnDateSetListener(this),
                mDate.year + 1900,
                mDate.month,
                mDate.date).show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateDate(time: Long) {
        mDate = Date(time)
        val days = resources.getStringArray(R.array.days)
        val months = resources.getStringArray(R.array.months)
        mTitle.text = "${days[mDate.day]}, ${mDate.date} ${months[mDate.month]}"
    }

    private fun dellTransaction() {
        TransactionService.INSTANCE.dell(mTracsationModel.id)
        finish()
    }

    @SuppressLint("SetTextI18n")
    private fun addKey(i: Int) {
        if (mManyTxt.text == "0") {
            mManyTxt.text = "$i"
        } else {
            mManyTxt.text = "${mManyTxt.text}$i"
        }
    }

    private fun dellKey() {
        val txt = mManyTxt.text
        if (txt.length > 1) {
            mManyTxt.text = txt.substring(0, txt.length - 1)
        } else {
            mManyTxt.text = "0"
        }
    }

    private fun selectKeybord() {
        if (isUpdate) {
            mCategoryTxt.visibility = View.VISIBLE
            mDoneCb.visibility = View.VISIBLE
        }
        mKeybord.visibility = View.VISIBLE
    }

    private fun selectCategory() {
        mCategoryTxt.visibility = View.VISIBLE
        mDoneCb.visibility = View.GONE
        val many = mManyTxt.text.toString().toInt()
        if (many == 0) {
            mManyTxt.setTextColor(resources.getColor(R.color.colorAccent))
            mZnakTxt.setTextColor(resources.getColor(R.color.colorAccent))
            Handler().postDelayed({
                mManyTxt.setTextColor(Color.WHITE)
                mZnakTxt.setTextColor(Color.WHITE)
            }, 300)
        } else {
            mKeybord.visibility = View.GONE
        }
    }

    private fun clickZnak() {
        if (mZnakTxt.text != "+") {
            mZnakTxt.text = "+"
            mZnakBt.setText(R.string.consumption)
            mDoneCb.setText(R.string.already_received)
        } else {
            mZnakTxt.text = "-"
            mZnakBt.setText(R.string.income)
            mDoneCb.setText(R.string.already_spent)
        }
    }

    private class MyOnDateSetListener(private val myActivityEditor: TransactionEditorActivity) : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            val date = Date(year - 1900, monthOfYear, dayOfMonth)
            myActivityEditor.updateDate(date.time)
        }
    }

}

