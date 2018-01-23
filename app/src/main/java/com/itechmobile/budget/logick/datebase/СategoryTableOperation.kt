package com.itechmobile.budget.logick.datebase

import android.content.ContentValues
import android.database.Cursor
import com.itechmobile.budget.App
import com.itechmobile.budget.model.CategoryModel

/**
 * Created by artem on 17.11.17.
 */
class СategoryTableOperation {

    companion object {

        fun add(model:CategoryModel): CategoryModel {
            val cv = parsContentValues(model)
            val db = App.instance.database
            model.id = db.insert(DBHelper.TableName.CATEGORY, null, cv)
            return model
        }

        fun getAll(): ArrayList<CategoryModel> = reyuest(null, null)

        fun getAll(isIncome: Boolean): ArrayList<CategoryModel>{
            val selection = DBHelper.CategoryKey.IS_INCOME + " = ?"
            val selectionArgs = arrayOf(if(isIncome) "1" else "0")
            val models = reyuest(selection, selectionArgs)
            return models
        }

        fun get(id: Long): CategoryModel {
            val selection = DBHelper.CategoryKey._ID + " = ?"
            val selectionArgs = arrayOf(id.toString())
            val models = reyuest(selection, selectionArgs)
            return models[0]

        }

        private fun reyuest(selection: String?, selectionArgs: Array<String>?): ArrayList<CategoryModel> {

            val moels = ArrayList<CategoryModel>()
            val db = App.instance.database

            val c = db.query(DBHelper.TableName.CATEGORY, null, selection, selectionArgs, null, null, DBHelper.CategoryKey._ID)
            if (c.moveToFirst()) {
                do {
                    val model = parsModel(c)
                    moels.add(model)
                } while (c.moveToNext())
            }
            c.close()

            return moels
        }

        private fun parsContentValues(model: CategoryModel) : ContentValues {
            val cv = ContentValues()
            cv.put(DBHelper.CategoryKey.ICO_NAME, model.icoName)
            cv.put(DBHelper.CategoryKey.NAME, model.name)
            cv.put(DBHelper.CategoryKey.IS_INCOME, if(model.isIncome) 1 else 0)
            return cv
        }

        private fun parsModel(c : Cursor) : CategoryModel {

            // определяем номера столбцов по имени в выборке
            val idColIndex = c.getColumnIndex(DBHelper.CategoryKey._ID)
            val icoNameColIndex = c.getColumnIndex(DBHelper.CategoryKey.ICO_NAME)
            val nameColIndex = c.getColumnIndex(DBHelper.CategoryKey.NAME)
            val isIncomeIndex = c.getColumnIndex(DBHelper.CategoryKey.IS_INCOME)

            val id = c.getInt(idColIndex)
            val icoName = c.getString(icoNameColIndex)
            val name = c.getString(nameColIndex)
            val isIncome = c.getInt(isIncomeIndex)

            val model = CategoryModel(name, icoName, isIncome == 1)
            model.id = id.toLong()

            return model

        }

    }

}