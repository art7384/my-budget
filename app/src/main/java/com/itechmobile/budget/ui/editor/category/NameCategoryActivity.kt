package com.itechmobile.budget.ui.editor.category

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.CategoryService
import com.itechmobile.budget.model.CategoryModel

/**
 * Created by artem on 21.03.18.
 */
class NameCategoryActivity : AppCompatActivity() {

    lateinit var mEmojiTxt: TextView
    lateinit var mNameTxt: EditText

    private var mCategoryModel: CategoryModel? = null

    companion object {
        val EXTRA_ID = "EXTRA_ID"
        val EXTRA_NAME = "EXTRA_NAME"
        val EXTRA_EMOJI = "EXTRA_EMOJI"
        private val REQUEST_CODE_EMOJI = 300
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_category)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mEmojiTxt = findViewById(R.id.emoji)
        mNameTxt = findViewById(R.id.name)

        mNameTxt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val intent = Intent()
                intent.putExtra(EXTRA_ID, mCategoryModel?.id)
                intent.putExtra(EXTRA_NAME, mNameTxt.text.toString())
                intent.putExtra(EXTRA_EMOJI, mEmojiTxt.text.toString())
                setResult(Activity.RESULT_OK, intent)
                finish()

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        mEmojiTxt.setOnClickListener {
            startActivityForResult(Intent(this, EmojiCategoryActivity::class.java), REQUEST_CODE_EMOJI)
        }

        mCategoryModel = CategoryService.INSTANCE.get(intent.getLongExtra(EXTRA_ID, -1))

        if (mCategoryModel!!.id != -1L) {
            mEmojiTxt.text = mCategoryModel?.icoName
            mNameTxt.setText(mCategoryModel?.name)
        } else {
            val emoji = intent.getStringExtra(EXTRA_EMOJI)
            if (emoji == null) {
                mEmojiTxt.text = "\uD83D\uDC80"//💀
                startActivityForResult(Intent(this, EmojiCategoryActivity::class.java), REQUEST_CODE_EMOJI)
            } else {
                mEmojiTxt.text = emoji
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_EMOJI) {
            if (resultCode == Activity.RESULT_OK) {
                mEmojiTxt.text = data!!.getStringExtra(EmojiCategoryActivity.EXTRA_EMOJI)
            }
        }
    }

}