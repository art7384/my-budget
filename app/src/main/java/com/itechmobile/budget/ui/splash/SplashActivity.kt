package com.itechmobile.budget.ui.splash

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.datebase.TransactionTable
import com.itechmobile.budget.ui.inpocket.InPocketActivity
import com.itechmobile.budget.ui.list.ListTracsationActivity
import com.vicpin.krealmextensions.queryAll


/**
 * Created by artem on 28.02.18.
 */

class SplashActivity : AppCompatActivity(), Animation.AnimationListener {

    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mView = findViewById(R.id.view)

    }

    override fun onResume() {
        super.onResume()

        val anim = AnimationUtils.loadAnimation(this, R.anim.splash)
        anim.setAnimationListener(this)
        mView.animation = anim

    }

    override fun onAnimationRepeat(animation: Animation?) {}

    override fun onAnimationEnd(animation: Animation?) {

        mView.layoutParams = ConstraintLayout.LayoutParams(mView.width, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200f, resources.displayMetrics).toInt())

        if (TransactionTable().queryAll().isNotEmpty())
            startActivity(Intent(this, ListTracsationActivity::class.java))
        else
            startActivity(Intent(this, InPocketActivity::class.java))
        finish()
        overridePendingTransition(R.anim.for_alpha, R.anim.to_alpha)

    }

    override fun onAnimationStart(animation: Animation?) {}

}