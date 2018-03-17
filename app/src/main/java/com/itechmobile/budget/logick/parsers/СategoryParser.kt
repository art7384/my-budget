package com.itechmobile.budget.logick.parsers

import com.itechmobile.budget.logick.datebase.CategoryTable
import com.itechmobile.budget.model.CategoryModel

/**
 * Created by artem on 12.03.18.
 */
class СategoryParser {

    companion object {

        fun toRealm(list: List<CategoryModel>): ArrayList<CategoryTable> {
            val arrTable = ArrayList<CategoryTable>()
            list.map {
                arrTable.add(toRealm(it))
            }
            return arrTable
        }

        fun toRealm(model: CategoryModel): CategoryTable {
            val ct = CategoryTable(model.name,
                    model.icoName,
                    model.isIncome,
                    model.isDell)
            ct.id = model.id
            return ct
        }

        fun from(list: List<CategoryTable>): ArrayList<CategoryModel> {
            val arrModel = ArrayList<CategoryModel>()
            list.map {
                arrModel.add(from(it))
            }
            return arrModel
        }

        fun from(table: CategoryTable): CategoryModel {
            val model = CategoryModel(table.name, table.icoName, table.isIncome)
            model.id = table.id
            model.isDell = table.isDell
            return model
        }
    }
}