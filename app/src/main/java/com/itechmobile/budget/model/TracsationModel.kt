package com.itechmobile.budget.model

import java.util.*

/**
 * Created by artem on 03.08.17.
 */
data class TracsationModel(var money:Int, var name:String, var date: Date, var idCategory: Long){
    var id: Long = -1
    var isDone = false
}