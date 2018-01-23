package com.itechmobile.budget.ui.editor

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.itechmobile.budget.R


/**
 * Created by artem on 22.12.17.
 */
class ManyEditorActivity: AppCompatActivity() {

    lateinit var mZnakTxt: TextView
    lateinit var mManyTxt: TextView

    companion object {
        val EXTRA_MANY = "EXTRA_MANY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_many_editor)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mManyTxt = findViewById(R.id.activityMainEditor_TextView_many)
        mZnakTxt = findViewById(R.id.activityMainEditor_TextView_znak)

        findViewById<Button>(R.id.activityManyEditor_Button_1).setOnClickListener { add(1) }
        findViewById<Button>(R.id.activityManyEditor_Button_2).setOnClickListener { add(2) }
        findViewById<Button>(R.id.activityManyEditor_Button_3).setOnClickListener { add(3) }
        findViewById<Button>(R.id.activityManyEditor_Button_4).setOnClickListener { add(4) }
        findViewById<Button>(R.id.activityManyEditor_Button_5).setOnClickListener { add(5) }
        findViewById<Button>(R.id.activityManyEditor_Button_6).setOnClickListener { add(6) }
        findViewById<Button>(R.id.activityManyEditor_Button_7).setOnClickListener { add(7) }
        findViewById<Button>(R.id.activityManyEditor_Button_8).setOnClickListener { add(8) }
        findViewById<Button>(R.id.activityManyEditor_Button_9).setOnClickListener { add(9) }
        findViewById<Button>(R.id.activityManyEditor_Button_0).setOnClickListener { add(0) }
        findViewById<ImageButton>(R.id.activityManyEditor_ImageButton_del).setOnClickListener { dell() }
        val btZn = findViewById<ImageButton>(R.id.activityManyEditor_ImageButton_p)
        btZn.setOnClickListener {
            if(mZnakTxt.text != "+"){
                mZnakTxt.text = "+"
                btZn.setImageResource(R.mipmap.ic_remove_black_24dp)
            } else {
                mZnakTxt.text = "-"
                btZn.setImageResource(R.mipmap.ic_add_black_24dp)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.many_editor, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
                return true
            }
            R.id.action_done -> {
                var many = mManyTxt.text.toString().toInt()
                if(mZnakTxt.text.toString() == "-"){
                    many *= -1
                }
                setResult(Activity.RESULT_OK, Intent().putExtra(EXTRA_MANY, many))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private fun add(i:Int){
        if(mManyTxt.text == "0"){
            mManyTxt.text = "$i"
        } else {
            mManyTxt.text = "${mManyTxt.text}$i"
        }
    }

    private fun dell(){
        val txt = mManyTxt.text
        if(txt.length > 1){
            mManyTxt.text = txt.substring(0, txt.length - 1)
        } else {
            mManyTxt.text = "0"
        }
    }
}