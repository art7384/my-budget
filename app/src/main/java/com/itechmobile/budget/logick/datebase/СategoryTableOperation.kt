package com.itechmobile.budget.logick.datebase

import com.itechmobile.budget.logick.parsers.СategoryParser
import com.itechmobile.budget.model.CategoryModel
import com.vicpin.krealmextensions.*

/**
 * Created by artem on 17.11.17.
 */
class СategoryTableOperation {

    companion object {

        fun add(models: List<CategoryModel>) = СategoryParser.toRealm(models).saveAll()

        fun add(model: CategoryModel) = СategoryParser.toRealm(model).save()

        fun update(model: CategoryModel) {

            val table = CategoryTable().queryFirst {
                it.equalTo("id", model.id)
            }
            if (table != null) {
                table.name = model.name
                table.icoName = model.icoName
                table.isDell = model.isDell
                table.isIncome = model.isIncome
                table.save()
            }
        }

        fun dell(id: Long) = CategoryTable().queryFirst {
            it.equalTo("id", id)
        }?.deleteFromRealm()

        fun get(): ArrayList<CategoryModel> = СategoryParser.from(CategoryTable().query {
            it.equalTo("isDell", false)
        })

        fun get(isIncome: Boolean): ArrayList<CategoryModel> = СategoryParser.from(CategoryTable().query {
            it.equalTo("isDell", false)
            it.equalTo("isIncome", isIncome)
        })

        fun getAll(): ArrayList<CategoryModel> = СategoryParser.from(CategoryTable().queryAll())

        fun getAll(isIncome: Boolean): ArrayList<CategoryModel> = СategoryParser.from(CategoryTable().query {
            it.equalTo("isIncome", isIncome)
        })

        fun get(id: Long): CategoryModel? {

            val table = CategoryTable().queryFirst {
                it.equalTo("id", id)
            } ?: return null

            return СategoryParser.from(table)

        }

    }

}

