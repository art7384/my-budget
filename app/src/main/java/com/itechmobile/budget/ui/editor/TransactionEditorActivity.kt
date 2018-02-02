package com.itechmobile.budget.ui.editor

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import com.itechmobile.budget.ui.editor.category.helpers.CategoryAdapter
import java.util.*


/**
 * Created by artem on 16.01.18.
 */
class TransactionEditorActivity : AppCompatActivity() {

    private lateinit var mManyTxt: TextView
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
    private var mIsPl = false
    private var isAdd = true
    private var isUpdate = false

    companion object {
        private val LOG_TAG = "TransactionEditorActivity"
        val EXTTRA_DATA = "EXTTRA_DATA"
        val EXTTRA_MANY_ID = "EXTTRA_MANY_ID"
        val EXTTRA_IS_PL = "EXTTRA_IS_PL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_editor)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        init()
        initKeybord()
        initCategory()

        val d = Date(intent.getLongExtra(EXTTRA_DATA, 0))
        val date = Date(d.year, d.month, d.date)
        val time = date.time
        val manyId = intent.getLongExtra(EXTTRA_MANY_ID, -1)
        mIsPl = intent.getBooleanExtra(EXTTRA_IS_PL, false)

        if (manyId >= 0) {
            isAdd = false
            isUpdate = true
            initUpdata(manyId)
        } else {
            isAdd = true
            isUpdate = false
            initAdd(time)
        }
        val thisData = Date()
        if (thisData.year == date.year && thisData.month == date.month && thisData.date == date.date) {
            mDoneCb.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 222) {
            if (resultCode == Activity.RESULT_OK) {
                val imoji = data!!.getStringExtra(EmojiCategoryActivity.EXTRA_EMOJI)
                val name = data.getStringExtra(EmojiCategoryActivity.EXTRA_NAME)
                CategoryService.INSTANCE.add(CategoryModel(name, imoji, mIsPl))
                updateCategorisList()
            }
        }
    }

    private fun init() {
        mCategoryTxt = findViewById(R.id.activityTransactionEditor_TextView_category)
        mCategory = findViewById(R.id.layautCategory_LinearLayout_content)
        mManyTxt = findViewById(R.id.activityTransactionEditor_TextView_many)
        mDoneCb = findViewById<CheckBox>(R.id.activityTransactionEditor_CheckBox_done)
        mNoteEt = findViewById(R.id.activityTransactionEditor_EditText_note)
        mTitle = findViewById(R.id.activityTransactionEditor_TextView_title)
        mDelete = findViewById(R.id.activityTransactionEditor_ImageButton_delete)
        mDelete.setOnClickListener { dellTransaction() }
        findViewById<ImageButton>(R.id.activityTransactionEditor_ImageButton_dell).setOnClickListener { dellKey() }
        findViewById<ImageButton>(R.id.activityTransactionEditor_ImageButton_today).setOnClickListener { showDateDialog() }
        findViewById<ImageButton>(R.id.activityTransactionEditor_ImageButton_clear).setOnClickListener {
            finish()
        }
        mDoneCb.setOnCheckedChangeListener { buttonView, isChecked ->
            updateTransaction()
        }
        mNoteEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(mNoteEt.windowToken, 0)
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                findViewById<EditText>(R.id.helper).requestFocus()
                updateTransaction()
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
        mCategoryTxt.text = CategoryService.INSTANCE.get(mTracsationModel.idCategory).icoName
        mSelectCategoryBt.text = getString(R.string.change_category)
        updateDate()

        var money = mTracsationModel.money
        if (money < 0) {
            mIsPl = false
            money *= -1
            mDoneCb.setText(R.string.already_spent)
        } else {
            mIsPl = true
            mDoneCb.setText(R.string.already_received)
        }
        mManyTxt.text = money.toString()
        mNoteEt.setText(mTracsationModel.name)
        mDoneCb.visibility = View.VISIBLE
        mCategoryTxt.visibility = View.VISIBLE
        mDelete.visibility = View.VISIBLE
    }

    private fun initAdd(time: Long) {
        mTracsationModel = TracsationModel(0, "", time, -1)
        updateDate()

        val intervalArr = resources.getStringArray(R.array.interval)
        val adapterInterval = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, intervalArr)
        adapterInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val myDate = Date(mTracsationModel.time)
        val thisDate = Date()
        val myDateTime = Date(myDate.year, myDate.month, myDate.date).time
        val thisDateTime = Date(thisDate.year, thisDate.month, thisDate.date).time
        mDoneCb.isChecked = myDateTime <= thisDateTime
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
        mSelectCategoryBt.setOnClickListener {
            updateCategorisList()
            selectCategory()
        }
    }

    private fun updateCategorisList() {
        val categorys = if (mIsPl) CategoryService.INSTANCE.incomeCategorys else CategoryService.INSTANCE.expenseCategorys
        (mCategoryList.adapter as CategoryAdapter).update(categorys)
    }

    private fun initCategory() {
        mCategoryList = findViewById(R.id.layautKategory_GridView_emojis)

        findViewById<Button>(R.id.layautKategory_Button_addKategory).setOnClickListener {
            startActivityForResult(Intent(this, EmojiCategoryActivity::class.java), 222)
        }

        val categorys = if (mIsPl) {
            CategoryService.INSTANCE.incomeCategorys
        } else {
            CategoryService.INSTANCE.expenseCategorys
        }

        mCategoryList.adapter = CategoryAdapter(categorys)
        mCategoryList.setOnItemLongClickListener { parent, view, position, id ->
            startDialogDellCategory(CategoryService.INSTANCE.get(id))
            return@setOnItemLongClickListener true
        }
        mCategoryList.setOnItemClickListener { parent, view, position, id ->
            mTracsationModel.idCategory = id
            if (isUpdate) {
                mCategoryTxt.text = CategoryService.INSTANCE.get(id).icoName
                selectKeybord()
                updateTransaction()
            } else {
                save()
            }
        }
    }

    private fun startDialogDellCategory(category: CategoryModel) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("${category.icoName} ${category.name}")
        builder.setMessage(R.string.dialog_dell_category)
        builder.setPositiveButton(R.string.dialog_button_dell, { dialog, which ->
            CategoryService.INSTANCE.dell(category.id)
            updateCategorisList()
        })
        builder.setNegativeButton(R.string.dialog_button_concel, null)
        builder.show()
    }

    private fun updateTransaction() {
        var many = mManyTxt.text.toString().toInt()
        if (many == 0) return
        if (!mIsPl) {
            many *= -1
        }
        mTracsationModel.money = many
        mTracsationModel.name = mNoteEt.text.toString()
        mTracsationModel.isDone = mDoneCb.isChecked
        if (isUpdate) {
            TransactionService.INSTANCE.update(mTracsationModel)
            setResult(Activity.RESULT_OK)
        }
    }

    private fun save() {
        var many = mManyTxt.text.toString().toInt()
        if (!mIsPl) {
            many *= -1
        }
        mTracsationModel.money = many
        mTracsationModel.name = mNoteEt.text.toString()
        mTracsationModel.isDone = mDoneCb.isChecked
        if (isAdd) {
            TransactionService.INSTANCE.save(mTracsationModel)
            // Возвращаем результат в виде даты (чтоб открылся тот день на который мы добавили новую транзакцию)
            val intent = Intent()
            intent.putExtra(EXTTRA_DATA, mTracsationModel.time)
            setResult(Activity.RESULT_OK, intent)
        } else {
            TransactionService.INSTANCE.update(mTracsationModel)
            setResult(Activity.RESULT_OK)
        }
        finish()
    }

    private fun showDateDialog() {
        val date = Date(mTracsationModel.time)
        DatePickerDialog(this,
                MyOnDateSetListener(this),
                date.year + 1900,
                date.month,
                date.date).show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateDate() {
        val date = Date(mTracsationModel.time)
        val days = resources.getStringArray(R.array.days)
        val months = resources.getStringArray(R.array.months)
        mTitle.text = "${days[date.day]}, ${date.date} ${months[date.month]}"
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
        updateTransaction()
    }

    private fun dellKey() {
        val txt = mManyTxt.text
        if (txt.length > 1) {
            mManyTxt.text = txt.substring(0, txt.length - 1)
        } else {
            mManyTxt.text = "0"
        }
        updateTransaction()
    }

    private fun selectKeybord() {
        if (isUpdate) {
            mCategoryTxt.visibility = View.VISIBLE
            mDoneCb.visibility = View.VISIBLE
        }
        mKeybord.visibility = View.VISIBLE
    }

    private fun selectCategory() {
        val many = mManyTxt.text.toString().toInt()
        if (many == 0) {
            mManyTxt.setTextColor(Color.RED)
            Handler().postDelayed({
                mManyTxt.setTextColor(Color.WHITE)
            }, 300)
        } else {
            mCategoryTxt.visibility = View.VISIBLE
            mDoneCb.visibility = View.GONE
            mKeybord.visibility = View.GONE
        }
    }

    private class MyOnDateSetListener(private val myActivityEditor: TransactionEditorActivity) : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            val date = Date(year - 1900, monthOfYear, dayOfMonth)
            myActivityEditor.mTracsationModel.time = date.time
            myActivityEditor.updateDate()
            myActivityEditor.updateTransaction()
        }
    }

}

