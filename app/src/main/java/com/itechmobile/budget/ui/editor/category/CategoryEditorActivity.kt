package com.itechmobile.budget.ui.editor.category

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.CategoryService
import com.itechmobile.budget.logick.service.EmojiService
import com.itechmobile.budget.model.CategoryModel

/**
 * Created by artem on 20.12.17.
 */
class CategoryEditorActivity : AppCompatActivity() {

    lateinit var mEmojiTxt: TextView
    lateinit var mExpensesRb: RadioButton
    lateinit var mCategoryEt: EditText

    companion object {
        private val REQUEST_CODE = 13
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor_category_transaction)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mEmojiTxt = findViewById(R.id.activityEditorCategory_TextView_emoji)
        mEmojiTxt.setOnClickListener {
            startActivityForResult(Intent(this, EmojiCategoryActivity::class.java), REQUEST_CODE)
        }
        mEmojiTxt.text = EmojiService.INSTANCE.random
        mExpensesRb = findViewById(R.id.activityEditorCategory_RadioButton_expenses)
        mCategoryEt = findViewById(R.id.activityEditorCategory_EditText_category)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val emoji = data?.getStringExtra(EmojiCategoryActivity.EXTRA_EMOJI)
                mEmojiTxt.text = emoji.toString()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.category_editor, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
                return true
            }
            R.id.category_ok -> {
                if(checkFilling()) {
                    saveCategory()
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveCategory(){
        val categoryModel = CategoryModel(mCategoryEt.text.toString(), mEmojiTxt.text.toString(), !mExpensesRb.isChecked)
        CategoryService.INSTANCE.add(categoryModel)
    }

    private fun checkFilling(): Boolean {
        if (mCategoryEt.text.isEmpty()) {
            toast("Название категории не заполненно")
            return false
        }
        return true
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}