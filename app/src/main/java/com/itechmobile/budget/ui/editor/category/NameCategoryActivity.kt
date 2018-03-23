package com.itechmobile.budget.ui.editor.category

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import com.itechmobile.budget.R

/**
 * Created by artem on 21.03.18.
 */
class NameCategoryActivity : AppCompatActivity() {

    lateinit var emoji: TextView
    lateinit var name: EditText

    companion object {
        val EXTRA_IMOJI = "EXTRA_IMOJI"
        val EXTRA_IS_INCOME = "EXTRA_IS_INCOME"
        val EXTRA_NAME = "EXTRA_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_category)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        emoji = findViewById(R.id.emoji)
        name = findViewById(R.id.name)

        emoji.text = intent.getStringExtra(EXTRA_IMOJI)
        emoji.setOnClickListener {
            startActivity(Intent(this, EmojiCategoryActivity::class.java))
            finish()
        }

        //CategoryService.INSTANCE.save(CategoryModel(name, imoji, IS_INCOME))

    }

}