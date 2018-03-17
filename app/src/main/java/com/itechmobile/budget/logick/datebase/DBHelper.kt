package com.itechmobile.budget.logick.datebase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.vicpin.krealmextensions.deleteAll
import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.save
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
                CategoryKey.IS_INCOME + " integer,  " +
                CategoryKey.IS_VISIBLE + " integer" +
                ");"

        /**
         * <p>Переписываем базу на Realm</p>
         */
        fun sqlMigrationReal(db: SQLiteDatabase) {
            dellRealm()
            //В Realm строчки будут иметь другие id, необходимо сопоставить id катекгорий с транзакциями
            val hm = categorySqlMigrationReal(db)
            categorySqlMigrationReal(db, hm)
        }

        private fun dellRealm() {
            TransactionTable().deleteAll()
            CategoryTable().deleteAll()
        }

        private fun categorySqlMigrationReal(db: SQLiteDatabase): HashMap<Long, Long> {
            val hashMapIdCategory = java.util.HashMap<Long, Long>() //HashMap<id был, id стал>

            val c = db.query(DBHelper.TableName.CATEGORY, null, null, null, null, null, DBHelper.CategoryKey._ID)
            if (c.moveToFirst()) {
                do {
                    val idColIndex = c.getColumnIndex(DBHelper.CategoryKey._ID)
                    val nameColIndex = c.getColumnIndex(DBHelper.CategoryKey.NAME)
                    val icoNameColIndex = c.getColumnIndex(DBHelper.CategoryKey.ICO_NAME)
                    val isIncomeColIndex = c.getColumnIndex(DBHelper.CategoryKey.IS_INCOME)
                    val isVisibleColIndex = c.getColumnIndex(DBHelper.CategoryKey.IS_VISIBLE)

                    val ct = CategoryTable(c.getString(nameColIndex),
                            c.getString(icoNameColIndex),
                            c.getInt(isIncomeColIndex) == 1,
                            c.getInt(isVisibleColIndex) == 0)
                    // запоминаем изменение id
                    ct.id = CategoryTable().queryAll().size.toLong()
                    ct.save()
                    // запоминаем изменение id
                    hashMapIdCategory[c.getLong(idColIndex)] = ct.id

                } while (c.moveToNext())
            }
            c.close()

            return hashMapIdCategory
        }

        private fun categorySqlMigrationReal(db: SQLiteDatabase, idMigrationCategorys: HashMap<Long, Long>) {

            val c = db.query(DBHelper.TableName.TRANSACTION, null, null, null, null, null, DBHelper.TransactionKey._ID)
            if (c.moveToFirst()) {
                do {
                    // определяем номера столбцов по имени в выборке
                    val idColIndex = c.getColumnIndex(DBHelper.TransactionKey._ID)
                    val manyColIndex = c.getColumnIndex(DBHelper.TransactionKey.MONEY)
                    val nameColIndex = c.getColumnIndex(DBHelper.TransactionKey.NAME)
                    val timeColIndex = c.getColumnIndex(DBHelper.TransactionKey.TIME)
                    val isDoneColIndex = c.getColumnIndex(DBHelper.TransactionKey.IS_DONE)
                    val categoryIdColIndex = c.getColumnIndex(DBHelper.TransactionKey.CATEGORY_ID)

                    val tt = TransactionTable(c.getString(nameColIndex),
                            c.getInt(manyColIndex),
                            Date(c.getLong(timeColIndex) * 1000),
                            c.getInt(isDoneColIndex) == 1,
                            idMigrationCategorys[c.getLong(categoryIdColIndex)] ?: 0)
                    tt.id = TransactionTable().queryAll().size.toLong()
                    tt.save()

                } while (c.moveToNext())
            }
            c.close()

        }

    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(LOG_TAG, "Create table: $CREATE_TABLE_TRANSACTION")
        db.execSQL(CREATE_TABLE_TRANSACTION)
        Log.d(LOG_TAG, "Create table: $CREATE_TABLE_CATEGORY")
        db.execSQL(CREATE_TABLE_CATEGORY)
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
            sqlMigrationReal(db)
        }
        if (oldVersion < 6) {
            // TODO: меняем таблицы SQLite
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