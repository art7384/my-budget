package com.itechmobile.budget.logick.datebase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.vicpin.krealmextensions.saveAll
import java.util.*


/**
 * Created by artem on 26.07.17.
 *
 * ВНИМАНИЕ!!!
 * С данной базы перешли на Realm
 *
 */
class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {

    companion object {

        private val LOG_TAG = "DBHelper"

        private val DB_VER = 5
        private val DB_NAME = "budgetDB.db"

        private val CREATE_TABLE_TRANSACTION = "create table " + TableName.TRANSACTION + " (" +
                TransactionKey._ID + " integer primary key autoincrement, " +
                TransactionKey.NAME + " text, " +
                TransactionKey.MONEY + " integer, " +
                TransactionKey.TIME + " integer, " +
                TransactionKey.IS_DONE + " integer, " +
                TransactionKey.IS_DELL + " integer, " +
                TransactionKey.IS_SYNCH + " integer, " +
                TransactionKey.TAG_ID + " integer, " +
                TransactionKey.CATEGORY_ID + " integer, " +
                TransactionKey.BUDGET_ID + " integer" +
                ");"

        private val CREATE_TABLE_CATEGORY = "create table " + TableName.CATEGORY + " (" +
                CategoryKey._ID + " integer primary key autoincrement, " +
                CategoryKey.NAME + " text, " +
                CategoryKey.ICO_NAME + " text, " +
                CategoryKey.IS_INCOME + " integer, " +
                CategoryKey.IS_VISIBLE + " integer" +
                ");"
    }

    override fun onCreate(db: SQLiteDatabase) {
//        Log.d(LOG_TAG, "Create table: $CREATE_TABLE_TRANSACTION")
//        db.execSQL(CREATE_TABLE_TRANSACTION)
//        Log.d(LOG_TAG, "Create table: $CREATE_TABLE_CATEGORY")
//        db.execSQL(CREATE_TABLE_CATEGORY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(LOG_TAG, "UPGRADE oldVersion: $oldVersion, newVersion: $newVersion")
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE ${TableName.TRANSACTION} ADD ${TransactionKey.CATEGORY_ID} text default -1")
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE ${TableName.CATEGORY} ADD ${CategoryKey.IS_VISIBLE} integer default 1")
        }
        if (oldVersion < 5) {
            val listTransactionTable = ArrayList<TransactionTable>()
            var c = db.query(DBHelper.TableName.TRANSACTION, null, null, null, null, null, DBHelper.TransactionKey._ID)
            if (c.moveToFirst()) {
                do {
                    // определяем номера столбцов по имени в выборке
                    val idColIndex = c.getColumnIndex(DBHelper.TransactionKey._ID)
                    val manyColIndex = c.getColumnIndex(DBHelper.TransactionKey.MONEY)
                    val nameColIndex = c.getColumnIndex(DBHelper.TransactionKey.NAME)
                    val timeColIndex = c.getColumnIndex(DBHelper.TransactionKey.TIME)
                    val isDoneColIndex = c.getColumnIndex(DBHelper.TransactionKey.IS_DONE)
                    val categoryIdColIndex = c.getColumnIndex(DBHelper.TransactionKey.CATEGORY_ID)

                    listTransactionTable.add(TransactionTable(c.getLong(idColIndex),
                            c.getString(nameColIndex),
                            c.getInt(manyColIndex),
                            Date(c.getLong(timeColIndex) * 1000),
                            c.getInt(isDoneColIndex) == 1,
                            c.getLong(categoryIdColIndex))
                    )

                } while (c.moveToNext())
            }
            c.close()
            if (listTransactionTable.size > 0) {
                listTransactionTable.saveAll()
            }

            val listCategoryTable = ArrayList<CategoryTable>()
            c = db.query(DBHelper.TableName.CATEGORY, null, null, null, null, null, DBHelper.CategoryKey._ID)
            if (c.moveToFirst()) {
                do {
                    val idColIndex = c.getColumnIndex(DBHelper.CategoryKey._ID)
                    val nameColIndex = c.getColumnIndex(DBHelper.CategoryKey.NAME)
                    val icoNameColIndex = c.getColumnIndex(DBHelper.CategoryKey.ICO_NAME)
                    val isIncomeColIndex = c.getColumnIndex(DBHelper.CategoryKey.IS_INCOME)
                    val isVisibleColIndex = c.getColumnIndex(DBHelper.CategoryKey.IS_VISIBLE)

                    listCategoryTable.add(CategoryTable(c.getLong(idColIndex),
                            c.getString(nameColIndex),
                            c.getString(icoNameColIndex),
                            c.getInt(isIncomeColIndex) == 1,
                            c.getInt(isVisibleColIndex) == 1))

                } while (c.moveToNext())
            }
            c.close()
            if (listCategoryTable.size > 0) {
                listCategoryTable.saveAll()
            }
        }
        if (oldVersion < 6) {
            // TODO: удалить таблицы SQLite
        }

    }

    class TableName {
        companion object {
            val USER = "user"
            val BUDGET = "budget"
            val TRANSACTION = "transact"
            val INVITE = "invite"
            val CATEGORY = "category"
        }
    }

    class CategoryKey {
        companion object {
            val _ID = "_id"
            val NAME = "name"
            val ICO_NAME = "ico_name"
            val IS_INCOME = "is_income"
            val IS_VISIBLE = "is_visible"// удаленная категория
        }
    }

    class TransactionKey {
        companion object {
            val _ID = "_id"
            val MONEY = "money"
            val TIME = "time"
            val IS_DONE = "is_done"
            val IS_SYNCH = "is_synch"
            val IS_DELL = "is_dell"
            val TAG_ID = "tag_id"
            val BUDGET_ID = "budget_id"
            val NAME = "name"
            val CATEGORY_ID = "category_id"
        }
    }

}