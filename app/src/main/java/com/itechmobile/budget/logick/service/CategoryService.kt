package com.itechmobile.budget.logick.service

import com.itechmobile.budget.logick.datebase.TransactionTableOperation
import com.itechmobile.budget.logick.datebase.СategoryTableOperation
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

    val visibleCategorys: ArrayList<CategoryModel>
        get() = СategoryTableOperation.get() //arr.save(CategoryModel("Новая категория", "➕", true))

    val visibleExpenseCategorys: ArrayList<CategoryModel>
        get() = СategoryTableOperation.get(false)

    val visibleIncomeCategorys: ArrayList<CategoryModel>
        get() = СategoryTableOperation.get(true)

    val allCategorys: ArrayList<CategoryModel>
        get() = СategoryTableOperation.getAll() //arr.save(CategoryModel("Новая категория", "➕", true))

    val allExpenseCategorys: ArrayList<CategoryModel>
        get() = СategoryTableOperation.getAll(false)

    val allIncomeCategorys: ArrayList<CategoryModel>
        get() = СategoryTableOperation.getAll(true)

    init {
        if (СategoryTableOperation.getAll(false).size < 1) {
            СategoryTableOperation.add(CategoryModel("Прочие расходы", "\uD83D\uDCB8", false))//💸
            СategoryTableOperation.add(CategoryModel("Продукты", "\uD83C\uDF56", false))//🍖
            СategoryTableOperation.add(CategoryModel("Дом", "\uD83C\uDFE0", false))//🏠
            СategoryTableOperation.add(CategoryModel("Авто", "\uD83D\uDE97", false))//🚗
            СategoryTableOperation.add(CategoryModel("Развлечения", "\uD83C\uDFB3", false))//🎳
        }
        if (СategoryTableOperation.getAll(true).size < 1) {
            СategoryTableOperation.add(CategoryModel("Прочие доходы", "\uD83D\uDCB5️", true))//💵️
            СategoryTableOperation.add(CategoryModel("Зароботок", "\uD83D\uDCB5", true))//💰
        }
        //category = СategoryTableOperation.getAll()[0]
    }

    fun get(id: Long): CategoryModel = СategoryTableOperation.get(id)?: CategoryModel("???", "\uD83D\uDC7B", true)//👻

    fun save(categoryModel: CategoryModel): Long = СategoryTableOperation.add(categoryModel)

    fun update(categoryModel: CategoryModel) = СategoryTableOperation.update(categoryModel)

    /**
     * <p>"Удаление" категории</p>
     * <p>Проверяем связана ли категория с транзакциями, если не связана то удаляем,
     * иначе ставим флаг "isDell"=true</p>
     */
    fun dell(id: Long) {
        val model = get(id) ?: return
        if (TransactionTableOperation.getForCategorySize(id) > 0) {
            model.isDell = true
            СategoryTableOperation.update(model)
        } else {
            СategoryTableOperation.dell(id)
        }
    }

}
