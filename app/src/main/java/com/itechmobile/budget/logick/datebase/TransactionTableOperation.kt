package com.itechmobile.budget.logick.datebase

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.itechmobile.budget.App
import com.itechmobile.budget.model.TracsationModel
import java.util.*

/**
 * Created by artem on 22.08.17.
 */
class TransactionTableOperation private constructor() {

    companion object {

        private val LOG_TAG = "TransactionTableOperation"

        fun add(models: List<TracsationModel>) {
            for (model in models) {
                add(model)
            }
        }

        fun add(model: TracsationModel): TracsationModel {

            val cv = parsContentValues(model)
            val db = App.instance.database

            model.id = db.insert(DBHelper.TableName.TRANSACTION, null, cv)

            return model
        }

        fun getSumPl(startTime: Long, stopTime: Long): Int {
            val startTimeStr = Math.ceil(startTime.toDouble() / 1000).toString()
            val stopTimeStr = Math.ceil(stopTime.toDouble() / 1000).toString()

            val selection = "${DBHelper.TransactionKey.TIME} >= ? and ${DBHelper.TransactionKey.TIME} < ? and ${DBHelper.TransactionKey.MONEY} > 0"
            val selectionArgs = arrayOf(startTimeStr, stopTimeStr)

            return reyuestSum(selection, selectionArgs)
        }

        fun getSumMn(startTime: Long, stopTime: Long): Int {
            val startTimeStr = Math.ceil(startTime.toDouble() / 1000).toString()
            val stopTimeStr = Math.ceil(stopTime.toDouble() / 1000).toString()

            val selection = "${DBHelper.TransactionKey.TIME} >= ? and ${DBHelper.TransactionKey.TIME} < ? and ${DBHelper.TransactionKey.MONEY} < 0"
            val selectionArgs = arrayOf(startTimeStr, stopTimeStr)

            return reyuestSum(selection, selectionArgs)
        }

        fun get(startTime: Long, stopTime: Long): List<TracsationModel> {

            val startTimeStr = Math.ceil(startTime.toDouble() / 1000).toString()
            val stopTimeStr = Math.ceil(stopTime.toDouble() / 1000).toString()

            val selection = "${DBHelper.TransactionKey.TIME} >= ? and ${DBHelper.TransactionKey.TIME} < ?"
            val selectionArgs = arrayOf(startTimeStr, stopTimeStr)

            return reyuest(selection, selectionArgs)
        }

        fun get(time: Long): List<TracsationModel> {

            val selection = DBHelper.TransactionKey.TIME + " = ?"
            val selectionArgs = arrayOf(Math.ceil(time.toDouble() / 1000).toString())

            return reyuest(selection, selectionArgs)
        }

        fun getFoeId(id: Long): TracsationModel {
            val selection = DBHelper.TransactionKey._ID + " = ?"
            val selectionArgs = arrayOf(id.toString())
            val models = reyuest(selection, selectionArgs)
            return models.get(0)
        }

        fun getNoDone(time: Long): ArrayList<TracsationModel> {

            val selection = DBHelper.TransactionKey.TIME + " < ? and " + DBHelper.TransactionKey.IS_DONE + " = ?"
            val selectionArgs = arrayOf(Math.ceil(time.toDouble() / 1000).toString(), "0")

            return reyuest(selection, selectionArgs)

        }

        fun getDoneSumTo(time: Long): Long {

            val db = App.instance.database
            val selection = DBHelper.TransactionKey.TIME + " < ? and " + DBHelper.TransactionKey.IS_DONE + " = ?"
            val selectionArgs = arrayOf(Math.ceil(time.toDouble() / 1000).toString(), "1")

            val c = db.query(DBHelper.TableName.TRANSACTION, arrayOf("SUM(${DBHelper.TransactionKey.MONEY})"), selection, selectionArgs, null, null, null)
            var sum = 0L
            if (c.moveToFirst()) {
                sum = c.getLong(0)
            }
            c.close()
            return sum
        }

        fun getSumTo(time: Long): Long {

            val db = App.instance.database
            val selection = DBHelper.TransactionKey.TIME + " <= ?"
            val selectionArgs = arrayOf(Math.ceil(time.toDouble() / 1000).toString())

            val c = db.query(DBHelper.TableName.TRANSACTION, arrayOf("SUM(${DBHelper.TransactionKey.MONEY})"), selection, selectionArgs, null, null, null)
            var sum = 0L
            if (c.moveToFirst()) {
                sum = c.getLong(0)
            }
            c.close()
            return sum
        }

        fun dell(id: Long): Int {
            val db = App.instance.database
            return db.delete(DBHelper.TableName.TRANSACTION, DBHelper.TransactionKey._ID + " = " + id, null)
        }

        fun update(model: TracsationModel): Int {
            val cv = parsContentValues(model)
            val db = App.instance.database
            // обновляем по id
            return db.update(DBHelper.TableName.TRANSACTION, cv, DBHelper.TransactionKey._ID + " = ?",
                    arrayOf<String>(model.id.toString()))
        }

        private fun reyuestSum(selection: String, selectionArgs: Array<String>): Int {

            val db = App.instance.database
            val c = db.query(DBHelper.TableName.TRANSACTION, arrayOf("SUM(${DBHelper.TransactionKey.MONEY})"), selection, selectionArgs, null, null, null)
            var sum = 0
            if (c.moveToFirst()) {
                sum = c.getInt(0)
            }

            Log.d("Sum", "${c.count} ${c.getInt(0)}")
            c.close()
            return sum
        }

        private fun reyuest(selection: String, selectionArgs: Array<String>): ArrayList<TracsationModel> {

            val moels = ArrayList<TracsationModel>()
            val db = App.instance.database

            val c = db.query(DBHelper.TableName.TRANSACTION, null, selection, selectionArgs, null, null, DBHelper.TransactionKey._ID)
            if (c.moveToFirst()) {
                do {
                    val model = parsModel(c)
                    moels.add(model)
                } while (c.moveToNext())
            }
            c.close()

            return moels
        }

        private fun parsContentValues(model: TracsationModel): ContentValues {

            val cv = ContentValues()
            cv.put(DBHelper.TransactionKey.MONEY, model.money)
            cv.put(DBHelper.TransactionKey.NAME, model.name)
            cv.put(DBHelper.TransactionKey.TIME, Math.ceil(model.time.toDouble() / 1000)) //в базе храним секунды, а не миллисекунды
            cv.put(DBHelper.TransactionKey.CATEGORY, model.category)
            if (model.isDone) {
                cv.put(DBHelper.TransactionKey.IS_DONE, 1)
            } else {
                cv.put(DBHelper.TransactionKey.IS_DONE, 0)
            }

            return cv
        }

        private fun parsModel(c: Cursor): TracsationModel {

            // определяем номера столбцов по имени в выборке
            val idColIndex = c.getColumnIndex(DBHelper.TransactionKey._ID)
            val manyColIndex = c.getColumnIndex(DBHelper.TransactionKey.MONEY)
            val nameColIndex = c.getColumnIndex(DBHelper.TransactionKey.NAME)
            val timeColIndex = c.getColumnIndex(DBHelper.TransactionKey.TIME)
            val isDoneColIndex = c.getColumnIndex(DBHelper.TransactionKey.IS_DONE)
            val categoryColIndex = c.getColumnIndex(DBHelper.TransactionKey.CATEGORY)

            val id = c.getInt(idColIndex)
            val many = c.getInt(manyColIndex)
            val name = c.getString(nameColIndex)
            val time = c.getLong(timeColIndex) * 1000
            val isDone = c.getInt(isDoneColIndex) == 1
            val category = c.getString(categoryColIndex)

            val model = TracsationModel(many, name, time, category)
            model.isDone = isDone
            model.id = id.toLong()

            return model

        }

    }
}