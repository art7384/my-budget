package com.itechmobile.budget.logick.datebase

import android.content.ContentValues
import android.database.Cursor
import com.itechmobile.budget.App
import com.itechmobile.budget.model.TracsationModel
import java.util.*

/**
 * Created by artem on 29.07.17.
 */
class TableMany {
    companion object {

        private val LOG_TAG = "TableMany"

        fun add(models: List<TracsationModel>) {
            for (model in models){
                add(model)
            }
        }

        fun add(model: TracsationModel): TracsationModel {

            val cv = parsContentValues(model)
            val db = App.instance.database

            model.id = db.insert(DBHelper.TABLE_MANY, null, cv)

            return model
        }

        fun get(time: Long): List<TracsationModel> {

            val selection = DBHelper.ManyKey.TIME + " = ?"
            val selectionArgs = arrayOf(Math.ceil(time.toDouble() / 1000).toString())

            return reyuest(selection, selectionArgs)
        }

        fun getFoeId(id: Long): TracsationModel {
            val selection = DBHelper.ManyKey.ID + " = ?"
            val selectionArgs = arrayOf(id.toString())
            val models = reyuest(selection, selectionArgs)
            return models.get(0)
        }

        fun getNoDone(time: Long): ArrayList<TracsationModel> {

            val selection = DBHelper.ManyKey.TIME + " < ? and " + DBHelper.ManyKey.IS_DONE + " = ?"
            val selectionArgs = arrayOf(Math.ceil(time.toDouble() / 1000).toString(), "0")

            return reyuest(selection, selectionArgs)

        }

        fun getSumTo(time: Long): Long {

            val db = App.instance.database
            val selection = DBHelper.ManyKey.TIME + " <= ?"
            val selectionArgs = arrayOf(Math.ceil(time.toDouble() / 1000).toString())

            val c =  db.query(DBHelper.TABLE_MANY, arrayOf(DBHelper.ManyKey.MANY), selection, selectionArgs, null, null,
                    DBHelper.ManyKey.TIME)

            var sum: Long = 0
            if (c.moveToFirst()) {
                do {
                    sum += c.getInt(0)
                } while (c.moveToNext())
            }
            c.close()
            return sum
        }

        fun dell(id: Long) : Int {
            val db = App.instance.database
            return db.delete(DBHelper.TABLE_MANY, DBHelper.ManyKey.ID + " = " + id, null)
        }

        fun update(model: TracsationModel) : Int {
            val cv = parsContentValues(model)
            val db = App.instance.database
            // обновляем по id
            return db.update(DBHelper.TABLE_MANY, cv, DBHelper.ManyKey.ID + " = ?",
                    arrayOf<String>(model.id.toString()))
        }

        private fun reyuest(selection: String, selectionArgs: Array<String>): ArrayList<TracsationModel> {

            val moels = ArrayList<TracsationModel>()
            val db = App.instance.database

            val c = db.query(DBHelper.TABLE_MANY, null, selection, selectionArgs, null, null, DBHelper.ManyKey.ID)
            if (c.moveToFirst()) {
                do {
                    val model = parsModel(c)
                    moels.add(model)
                } while (c.moveToNext())
            }
            c.close()

            return moels
        }

        private fun parsContentValues(model: TracsationModel) : ContentValues {

            val cv = ContentValues()
            cv.put(DBHelper.ManyKey.MANY, model.many)
            cv.put(DBHelper.ManyKey.NAME, model.name)
            cv.put(DBHelper.ManyKey.TIME, Math.ceil(model.time.toDouble() / 1000)) //в базе храним секунды, а не миллисекунды
            if(model.isDone) {
                cv.put(DBHelper.ManyKey.IS_DONE, 1)
            } else {
                cv.put(DBHelper.ManyKey.IS_DONE, 0)
            }

            return cv
        }

        fun parsModel(c : Cursor) : TracsationModel {

            // определяем номера столбцов по имени в выборке
            val idColIndex = c.getColumnIndex(DBHelper.ManyKey.ID)
            val manyColIndex = c.getColumnIndex(DBHelper.ManyKey.MANY)
            val nameColIndex = c.getColumnIndex(DBHelper.ManyKey.NAME)
            val timeColIndex = c.getColumnIndex(DBHelper.ManyKey.TIME)
            val isDoneColIndex = c.getColumnIndex(DBHelper.ManyKey.IS_DONE)

            val id = c.getInt(idColIndex)
            val many = c.getInt(manyColIndex)
            val name = c.getString(nameColIndex)
            val time = c.getLong(timeColIndex) * 1000
            val isDone = c.getInt(isDoneColIndex) == 1

            val model = TracsationModel(many, name, time)
            model.isDone = isDone
            model.id = id.toLong()

            return model

        }

    }
}