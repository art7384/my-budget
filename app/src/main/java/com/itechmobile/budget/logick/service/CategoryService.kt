package com.itechmobile.budget.logick.service

import com.itechmobile.budget.logick.datebase.СategoryTableOperation
import com.itechmobile.budget.logick.persisten.UserPersisten
import com.itechmobile.budget.model.CategoryModel

/**
 * Created by artem on 17.11.17.
 */
class CategoryService private constructor() {

    private object Holder {
        val INSTANCE = CategoryService()
    }

    companion object {
        private val LOG_TAG = "CategoryService"
        val INSTANCE: CategoryService by lazy { Holder.INSTANCE }

    }

    var category: CategoryModel
        get() = СategoryTableOperation.get(UserPersisten.categoryId)
        set(value) {
            UserPersisten.categoryId = value.id
        }

    val categoryEmojis: Array<String>
    get() {
        val emojis = ArrayList<String>()
        val arr = categorys
        arr.mapTo(emojis) { it.icoName }
        return emojis.toTypedArray()
    }

    val expenseCategory: CategoryModel
        get() {
            if (category.isIncome) category = СategoryTableOperation.getAll(false)[0]
            return category
        }

    val incomeCategory: CategoryModel
        get() {
            if (!category.isIncome) category = СategoryTableOperation.getAll(true)[0]
            return category
        }

    val categorys: ArrayList<CategoryModel>
        get() = СategoryTableOperation.getAll() //arr.add(CategoryModel("Новая категория", "➕", true))

    val expenseCategorys: ArrayList<CategoryModel>
        get() = СategoryTableOperation.getAll(false)

    val incomeCategorys: ArrayList<CategoryModel>
        get() = СategoryTableOperation.getAll(true)

    init {
        if (СategoryTableOperation.getAll(false).size < 1) {
            СategoryTableOperation.add(CategoryModel("Прочие расходы", "\uD83D\uDCB8", false))//💸
            СategoryTableOperation.add(CategoryModel("Продукты", "\uD83C\uDF56", false))//🍖
            СategoryTableOperation.add(CategoryModel("Дом", "\uD83C\uDFE0", false))//🏠
            СategoryTableOperation.add(CategoryModel("Авто", "\uD83D\uDE97", false))//🚗
            СategoryTableOperation.add(CategoryModel("Развлечения", "\uD83C\uDF89", false))//🎉
        }
        if (СategoryTableOperation.getAll(true).size < 1) {
            СategoryTableOperation.add(CategoryModel("Прочие доходы", "\uD83C\uDF81", true))//🎁
            СategoryTableOperation.add(CategoryModel("Зарплата", "\uD83D\uDCB5", true))//💵
            СategoryTableOperation.add(CategoryModel("Премия", "\uD83D\uDCB0", true))//💰
        }
        category = СategoryTableOperation.getAll()[0]
    }

    fun choiceCategory(id: Long): CategoryModel {
        category = СategoryTableOperation.get(id)
        return category
    }

    fun add(categoryModel: CategoryModel){
        СategoryTableOperation.add(categoryModel)//
    }

    fun update(categoryModel: CategoryModel){
        //СategoryTableOperation.update(categoryModel)//
    }

    fun dell(id: Long){
        //СategoryTableOperation.dell(id)
    }

}
