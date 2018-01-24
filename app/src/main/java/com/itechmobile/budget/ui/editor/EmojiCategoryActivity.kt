package com.itechmobile.budget.ui.editor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.GridView
import android.widget.Toast
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.EmojiService
import com.itechmobile.budget.ui.editor.helpers.EmojiGridAdapter

/**
 * Created by artem on 20.12.17.
 */
class EmojiCategoryActivity : AppCompatActivity() {

    lateinit var mEmojiGrid: GridView
    lateinit var mName: EditText

    companion object {
        private val LOG_TAG = "EmojiCategoryActivity"
        val EXTRA_EMOJI = "EXTRA_EMOJI"
        val EXTRA_NAME = "EXTRA_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emoji_category)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mEmojiGrid = findViewById(R.id.activityEmojiCategory_GridView_emojis)
        mName = findViewById(R.id.activityEmojiCategory_EditText_name)

        val items = EmojiService.INSTANCE.emojis
        val itemsList = ArrayList<String>()
        itemsList.addAll(items)
        mEmojiGrid.adapter = EmojiGridAdapter(itemsList)
        mEmojiGrid.setOnItemClickListener { parent, view, position, id ->
            val imoji = EmojiService.INSTANCE.emojis[position]
            if(mName.text.isNotEmpty()){
                val name = mName.text.toString()
                setResult(Activity.RESULT_OK, Intent().putExtra(EXTRA_EMOJI, imoji).putExtra(EXTRA_NAME, name))
                finish()
            } else {
                Toast.makeText(this, R.string.no_imput_name_category,
                        Toast.LENGTH_SHORT).show()
            }

        }
    }
}