package com.itechmobile.budget.ui.editor.category

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.itechmobile.budget.R

/**
 * Created by artem on 21.03.18.
 */
class NameCategoryActivity : AppCompatActivity() {

    lateinit var mEmojiTxt: TextView
    lateinit var mNameTxt: EditText

    companion object {
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

        val emoji = intent.getStringExtra(EXTRA_EMOJI)
        if (emoji == null) {
            mEmojiTxt.text = "\uD83D\uDC80"//💀
            startActivityForResult(Intent(this, EmojiCategoryActivity::class.java), REQUEST_CODE_EMOJI)
        } else {
            mEmojiTxt.text = emoji
        }

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