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
import com.itechmobile.budget.logick.service.TransactionService
import com.itechmobile.budget.ui.calendar.CalendarActivity
import com.itechmobile.budget.ui.inpocket.InPocketActivity


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

        if (TransactionService.INSTANCE.size > 0)
            startActivity(Intent(this, CalendarActivity::class.java))
        else
            startActivity(Intent(this, InPocketActivity::class.java))
        finish()
        overridePendingTransition(R.anim.for_alpha, R.anim.to_alpha)

    }

    override fun onAnimationStart(animation: Animation?) {}

}