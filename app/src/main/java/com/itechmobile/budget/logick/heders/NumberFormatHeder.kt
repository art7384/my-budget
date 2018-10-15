package com.itechmobile.budget.logick.heders

import android.icu.text.NumberFormat
import android.os.Build

class NumberFormatHeder {

    companion object {
        fun currency(obj: Any): String {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val format = NumberFormat.getCurrencyInstance()
                return format.format(obj)
            }
            return obj.toString()
        }
    }
}