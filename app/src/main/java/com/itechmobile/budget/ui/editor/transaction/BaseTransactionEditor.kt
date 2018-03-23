package com.itechmobile.budget.ui.editor.transaction

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
import com.itechmobile.budget.ui.editor.category.EmojiCategoryActivity
import com.itechmobile.budget.ui.editor.category.helpers.CategoryAdapter
import java.util.*

/**
 * Created by artem on 02.03.18.
 */
abstract class BaseTransactionEditor : AppCompatActivity() {

    abstract val IS_INCOME: Boolean
    private var isAddTransaction = true
    private lateinit var mTracsationModel: TracsationModel

    private lateinit var mSummTxt: TextView
    private lateinit var mSelectCategoryBt: Button
    private lateinit var mDescriptionEt: EditText
    private lateinit var mTitle: TextView
    private lateinit var mDelete: ImageButton
    private lateinit var mKeybord: View
    private lateinit var mCategory: View
    private lateinit var mCategoryList: GridView

    companion object {
        private val LOG_TAG = "BaseTransactionEditor"
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
        val date = Date(d.year, d.month, d.date)
        val transactionId = intent.getLongExtra(EXTTRA_MANY_ID, -1)

        isAddTransaction = transactionId < 0
        if (isAddTransaction) {
            initAddTransaction(date)
        } else {
            initUpdataTransaction(transactionId)
        }

    }

    private fun init() {
        mCategory = findViewById(R.id.layautCategory_LinearLayout_content)
        mSummTxt = findViewById(R.id.summ)
        mDescriptionEt = findViewById(R.id.description)
        mTitle = findViewById(R.id.title)
        mDelete = findViewById(R.id.delete)
        mDelete.setOnClickListener { dellTransaction() }
        findViewById<ImageButton>(R.id.clear).setOnClickListener { finish() }
        mDescriptionEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(mDescriptionEt.windowToken, 0)
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                findViewById<EditText>(R.id.helper).requestFocus()
                updateTransaction()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun initUpdataTransaction(manyId: Long) {
        mTracsationModel = TransactionService.INSTANCE.get(manyId)
        mSelectCategoryBt.text = getString(R.string.change_category)
        updateDate()

        mSummTxt.text = mTracsationModel.money.toString()
        mDescriptionEt.setText(mTracsationModel.name)
        mDelete.visibility = View.VISIBLE
    }

    private fun initAddTransaction(date: Date) {

        mTracsationModel = TracsationModel(0, "", date, -1)
        updateDate()

        val intervalArr = resources.getStringArray(R.array.interval)
        val adapterInterval = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, intervalArr)
        adapterInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }


    private fun initKeybord() {
        mKeybord = findViewById(R.id.contener)
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
        findViewById<Button>(R.id.c).setOnClickListener { dellKey() }
        mSelectCategoryBt = findViewById(R.id.selectCategory)
        mSelectCategoryBt.setOnClickListener {
            updateCategorisList()
            selectCategory()
        }
    }

    private fun updateCategorisList() {
        val categorys = if (IS_INCOME) CategoryService.INSTANCE.visibleIncomeCategorys else CategoryService.INSTANCE.visibleExpenseCategorys
        (mCategoryList.adapter as CategoryAdapter).update(categorys)
    }

    private fun initCategory() {
        mCategoryList = findViewById(R.id.layautKategory_GridView_emojis)

        findViewById<Button>(R.id.layautKategory_Button_addKategory).setOnClickListener {
            startActivity(Intent(this, EmojiCategoryActivity::class.java).putExtra(EmojiCategoryActivity.EXTRA_IS_INCOME, IS_INCOME))
        }

        val categorys = if (IS_INCOME) {
            CategoryService.INSTANCE.visibleIncomeCategorys
        } else {
            CategoryService.INSTANCE.visibleExpenseCategorys
        }

        mCategoryList.adapter = CategoryAdapter(categorys)
        mCategoryList.setOnItemLongClickListener { parent, view, position, id ->
            startDialogDellCategory(CategoryService.INSTANCE.get(id))
            return@setOnItemLongClickListener true
        }
        mCategoryList.setOnItemClickListener { parent, view, position, id ->
            mTracsationModel.idCategory = id
            if (isAddTransaction) {
                save()
            } else {
//                mCategoryTxt.text = CategoryService.INSTANCE.get(id).icoName
                selectKeybord()
                updateTransaction()
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
        var many = mSummTxt.text.toString().toInt()
        if (many == 0) return
        if (!IS_INCOME) {
            many *= -1
        }
        mTracsationModel.money = many
        mTracsationModel.name = mDescriptionEt.text.toString()
        if (!isAddTransaction) {
            TransactionService.INSTANCE.update(mTracsationModel)
            setResult(Activity.RESULT_OK)
        }
    }

    private fun save() {
        var many = mSummTxt.text.toString().toInt()
        if (!IS_INCOME) {
            many *= -1
        }
        mTracsationModel.money = many
        mTracsationModel.name = mDescriptionEt.text.toString()
        if (isAddTransaction) {
            TransactionService.INSTANCE.save(mTracsationModel)
            // Возвращаем результат в виде даты (чтоб открылся тот день на который мы добавили новую транзакцию)
            val intent = Intent()
            intent.putExtra(EXTTRA_DATA, mTracsationModel.date.time)
            setResult(Activity.RESULT_OK, intent)
        } else {
            TransactionService.INSTANCE.update(mTracsationModel)
            setResult(Activity.RESULT_OK)
        }
        finish()
    }

    private fun showDateDialog() {
        DatePickerDialog(this,
                MyOnDateSetListener(this),
                mTracsationModel.date.year + 1900,
                mTracsationModel.date.month,
                mTracsationModel.date.date).show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateDate() {
        val days = resources.getStringArray(R.array.days)
        val months = resources.getStringArray(R.array.months)
        mTitle.text = "${days[mTracsationModel.date.day]}, ${mTracsationModel.date.date} ${months[mTracsationModel.date.month]}"
    }

    private fun dellTransaction() {
        TransactionService.INSTANCE.dell(mTracsationModel.id)
        finish()
    }

    @SuppressLint("SetTextI18n")
    private fun addKey(i: Int) {
        if (mSummTxt.text == "0") {
            mSummTxt.text = "$i"
        } else {
            mSummTxt.text = "${mSummTxt.text}$i"
        }
        updateTransaction()
    }

    private fun dellKey() {
        val txt = mSummTxt.text
        if (txt.length > 1) {
            mSummTxt.text = txt.substring(0, txt.length - 1)
        } else {
            mSummTxt.text = "0"
        }
        updateTransaction()
    }

    private fun selectKeybord() {
        mKeybord.visibility = View.VISIBLE
    }

    private fun selectCategory() {
        val many = mSummTxt.text.toString().toInt()
        if (many == 0) {
            mSummTxt.setTextColor(Color.RED)
            Handler().postDelayed({
                mSummTxt.setTextColor(Color.WHITE)
            }, 300)
        } else {
            mKeybord.visibility = View.GONE
        }
    }

    private class MyOnDateSetListener(private val activity: BaseTransactionEditor) : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            val date = Date(year - 1900, monthOfYear, dayOfMonth)
            activity.mTracsationModel.date = date
            activity.updateDate()
            activity.updateTransaction()
        }
    }


}