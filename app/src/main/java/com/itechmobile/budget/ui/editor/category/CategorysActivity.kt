package com.itechmobile.budget.ui.editor.category

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.CategoryService
import com.itechmobile.budget.ui.editor.category.helpers.CategoryAdapter

/**
 * Created by artem on 21.12.17.
 */
class CategorysActivity : AppCompatActivity() {

    lateinit var mCategorysList: ListView

    companion object {
        val EXTRA_MANY = "EXTRA_MANY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorys)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mCategorysList = findViewById<ListView>(R.id.activityCategorys_ListView_categorys)
        mCategorysList.adapter = CategoryAdapter(ArrayList())
        mCategorysList.setOnItemClickListener { parent, view, position, id ->
            CategoryService.INSTANCE.choiceCategory(id)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        val many = intent.getIntExtra(EXTRA_MANY, 0)
        val arrCategories = when {
            many < 0 -> CategoryService.INSTANCE.expenseCategorys
            many > 0 -> CategoryService.INSTANCE.incomeCategorys
            else -> CategoryService.INSTANCE.categorys
        }

        (mCategorysList.adapter as CategoryAdapter).update(arrCategories)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.category, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
                return true
            }
            R.id.category_add -> {
                startActivity(Intent(this, CategoryEditorActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}