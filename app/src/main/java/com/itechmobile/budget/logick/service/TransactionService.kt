package com.itechmobile.budget.logick.service

import com.itechmobile.budget.logick.datebase.TransactionTableOperation
import com.itechmobile.budget.model.TracsationModel
import java.util.*


/**
 * Created by artem on 25.07.17.
 */

class TransactionService private constructor(){

    private object Holder { val INSTANCE = TransactionService() }

    companion object {
        private val LOG_TAG = "TransactionService"
        val INSTANCE: TransactionService by lazy { Holder.INSTANCE }
    }

    /**
     * <p>Сохраняем транзакцию</p>
     *
     * @param model Мдель транзакции
     * @return модель транзакции
     */
    fun save(model: TracsationModel): TracsationModel {
        return TransactionTableOperation.add(model)
    }

    fun getStartTime(): Long{
        return TransactionTableOperation.getStartTime() * 1000
    }

    fun getDoneSumTo(time: Long):Long{
        val d = Date(time)
        val t = Date(d.year, d.month, d.date).time + 24 * 60 * 60 * 1000
        return TransactionTableOperation.getDoneSumTo(t)
    }

    /**
     * <p>Вычисляем сумму доходов за день</p>
     *
     * @param time день
     * @return сумма доходов за день
     */
    fun getDayPl(time: Long):Int{
        val d = Date(time)
        val startTime = Date(d.year, d.month, d.date).time
        val stopTime = Date(d.year, d.month, d.date).time + 24 * 60 * 60 * 1000
        return TransactionTableOperation.getSumPl(startTime, stopTime)
    }

    /**
     * <p>Вычисляем сумму расходов за день</p>
     *
     * @param time день
     * @return сумма расходов за день
     */
    fun getDayMn(time: Long):Int{
        val d = Date(time)
        val startTime = Date(d.year, d.month, d.date).time
        val stopTime = Date(d.year, d.month, d.date).time + 24 * 60 * 60 * 1000
        return TransactionTableOperation.getSumMn(startTime, stopTime)
    }

    /**
     * <p>Список транзакций за день</p>
     *
     * @param time день
     * @return массив моделей транзакций
     */
    fun getDay(time: Long): List<TracsationModel> {
        val d = Date(time)
        val startTime = Date(d.year, d.month, d.date).time
        val stopTime = Date(d.year, d.month, d.date).time + 24 * 60 * 60 * 1000
        return TransactionTableOperation.get(startTime, stopTime)
    }

    /**
     * <p>Возвращет модель транзакции</p>
     *
     * @param id id транзакции
     * @return модель транзакции
     */
    fun get(id: Long): TracsationModel {
        return TransactionTableOperation.getFoeId(id)
    }

    /**
     * <p>Сумма всех транзакций до даты</p>
     *
     * @param time день
     * @return сумма транзакций
     */
    fun getSumTo(time: Long): Long {
        val d = Date(time)
        val t = Date(d.year, d.month, d.date).time
        return TransactionTableOperation.getSumTo(t)
    }

    /**
     * <p>Сумма на каждую категорию</p>
     *
     * @param startTime начальный день
     * @param stopTime конечный день
     * @return HashMap<category_id, sum>
     */
    fun getCategorySum(startTime: Long, stopTime: Long): HashMap<Long, Long> {
        return TransactionTableOperation.getCategorySum(startTime, stopTime)
    }

    /**
     * <p>Удаляем транзакцию</p>
     *
     * @param id id удоляемой транзакции
     * @return id удаленной транзакции
     */
    fun dell(id: Long) : Int {
        return TransactionTableOperation.dell(id)
    }

    /**
     * <p>Изменение транзакции</p>
     *
     * @param model модель транзакции с изменениями
     * @return модель изменненой транзакции
     */
    fun update(model: TracsationModel) : Int{
        return TransactionTableOperation.update(model)
    }

    /**
     * <p>Список не реализованных транзакций</p>
     * <p>Транзакция может быть запланированна, а через кокоето время реализована</p>
     *
     * @param time день до которого возвращать транзакции
     * @return список транзакций
     */
    fun getNoDone(time: Long) : List<TracsationModel> {
        val d = Date(time)
        val t = Date(d.year, d.month, d.date).time
        return TransactionTableOperation.getNoDone(t)
    }

}
