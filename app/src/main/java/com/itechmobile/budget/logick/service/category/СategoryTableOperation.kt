package com.itechmobile.budget.logick.service.category

import com.itechmobile.budget.logick.database.CategoryTable
import com.itechmobile.budget.logick.parsers.СategoryParser
import com.itechmobile.budget.model.CategoryModel
import com.vicpin.krealmextensions.*

/**
 * Created by artem on 17.11.17.
 */
class СategoryTableOperation {

    companion object {

        fun add(models: List<CategoryModel>) {
            var id = CategoryTable().queryAll().maxBy { it.id }?.id ?: 0L
            models.map {
                it.id = ++id
            }
            СategoryParser.toRealm(models).saveAll()
        }

        fun add(model: CategoryModel): Long {
            var id = CategoryTable().queryAll().maxBy { it.id }?.id ?: 0L
            model.id = ++id
            СategoryParser.toRealm(model).save()
            return id
        }

        fun update(model: CategoryModel) {

            CategoryTable().delete {
                this.equalTo("id", model.id)
            }

            val table = CategoryTable()
            table.id = model.id
            table.name = model.name
            table.icoName = model.icoName
            table.isDell = model.isDell
            table.isIncome = model.isIncome
            table.save()
        }

        fun dell(id: Long) = CategoryTable().delete {
            this.equalTo("id", id)
        }

        fun get(): ArrayList<CategoryModel> = СategoryParser.from(CategoryTable().query {
            this.equalTo("isDell", false)
        })

        fun get(isIncome: Boolean): ArrayList<CategoryModel> = СategoryParser.from(CategoryTable().query {
            this.equalTo("isDell", false)
            this.equalTo("isIncome", isIncome)
        })

        fun getAll(): ArrayList<CategoryModel> = СategoryParser.from(CategoryTable().queryAll())

        fun getAll(isIncome: Boolean): ArrayList<CategoryModel> = СategoryParser.from(CategoryTable().query {
            this.equalTo("isIncome", isIncome)
        })

        fun get(id: Long): CategoryModel? {

            val table = CategoryTable().queryFirst {
                this.equalTo("id", id)
            } ?: return null

            return СategoryParser.from(table)

        }

    }

}

