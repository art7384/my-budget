package com.itechmobile.budget.model

/**
 * Created by artem on 17.11.17.
 */
data class CategoryModel(var name: String, var icoName: String, var isIncome: Boolean) {
    constructor() : this("", "", false)

    var id: Long = -1
    var isDell = false// удаленная категория
}