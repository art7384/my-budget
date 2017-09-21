package com.itechmobile.budget.model

/**
 * Created by artem on 03.08.17.
 */
data class TracsationModel(var many:Int, var name:String, var time:Long){
    var id: Long = -1
    var isDone = false
}