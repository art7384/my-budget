package com.itechmobile.budget.ui.editor.category

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.GridView
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.EmojiService
import com.itechmobile.budget.ui.editor.helpers.EmojiGridAdapter

/**
 * Created by artem on 20.12.17.
 */
class EmojiCategoryActivity : AppCompatActivity() {

    lateinit var mEmojiGrid: GridView

    companion object {
        val EXTRA_EMOJI = "EXTRA_EMOJI"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emoji_category)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mEmojiGrid = findViewById(R.id.activityEmojiCategory_GridView_emojis)
        val items = EmojiService.INSTANCE.emojis
        val itemsList = ArrayList<String>()
        itemsList.addAll(items)
        mEmojiGrid.adapter = EmojiGridAdapter(itemsList)
        mEmojiGrid.setOnItemClickListener { parent, view, position, id ->
            val imojis = EmojiService.INSTANCE.emojis[position]
            setResult(Activity.RESULT_OK, Intent().putExtra(EXTRA_EMOJI, imojis))
            finish()
        }
    }
}