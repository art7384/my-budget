package com.itechmobile.budget.logick.service.transaction

import com.itechmobile.budget.model.TracsationModel
import java.util.*


/**
 * Created by artem on 25.07.17.
 */

class TransactionService private constructor() {

    private object Holder {
        val INSTANCE = TransactionService()
    }

    companion object {
        private val LOG_TAG = "TransactionService"
        val INSTANCE: TransactionService by lazy { Holder.INSTANCE }
    }

    /**
     * <p>Количество записей в базе</p>
     */
//    val size: Int = TransactionTableOperation.size

    /**
     * <p>Возвращаем дату первой транзакции</p>
     */
    val startDate: Date = TransactionTableOperation.startDate

    /**
     * <p>Сохраняем транзакцию</p>
     */
    fun save(model: TracsationModel): Long = TransactionTableOperation.add(model)

    /**
     * <p>Сохраняем транзакциb</p>
     */
    fun save(models: List<TracsationModel>) = TransactionTableOperation.add(models)

    /**
     * <p>Вычисляем сумму доходов за день</p>
     */
    fun getDayPl(date: Date): Int = TransactionTableOperation.getSumPl(
            Date(date.year, date.month, date.date),
            Date(Date(date.year, date.month, date.date).time + 24 * 60 * 60 * 1000))

    /**
     * <p>Вычисляем сумму расходов за день</p>
     */
    fun getDayMn(date: Date): Int = TransactionTableOperation.getSumMn(
            Date(date.year, date.month, date.date),
            Date(Date(date.year, date.month, date.date).time + 24 * 60 * 60 * 1000))

    /**
     * <p>Список расходов за период, по категории</p>
     */
    fun getDayMn(idCategory: Long, start: Date, stop: Date): List<TracsationModel> = TransactionTableOperation.get(
            idCategory,
            Date(start.year, start.month, start.date),
            Date(stop.year, stop.month, stop.date),
            false)

    /**
     * <p>Список расходов за период</p>
     */
    fun getDayMn(start: Date, stop: Date): List<TracsationModel> = TransactionTableOperation.get(
            Date(start.year, start.month, start.date),
            Date(stop.year, stop.month, stop.date),
            false)

    /**
     * <p>Список доходов за период</p>
     */
    fun getDayPl(start: Date, stop: Date): List<TracsationModel> = TransactionTableOperation.get(
            Date(start.year, start.month, start.date),
            Date(stop.year, stop.month, stop.date),
            true)


    /**
     * <p>Список транзакций за период</p>
     */
    fun getForMonth(date: Date): List<TracsationModel> {
        val startDate = Date(date.year, date.month, 1)
        val endCal = GregorianCalendar(date.year + 1900, date.month, 1)
        endCal.add(Calendar.MONTH, 1)
        endCal.add(Calendar.DATE, -1)

        return TransactionTableOperation.get(startDate, endCal.time)
    }

    /**
     * <p>Список транзакций за день</p>
     */
    fun getDay(date: Date): List<TracsationModel> = TransactionTableOperation.get(
            Date(date.year, date.month, date.date),
            Date(Date(date.year, date.month, date.date).time + 24 * 60 * 60 * 1000))

    /**
     * <p>Возвращет модель транзакции</p>
     */
    fun get(id: Long): TracsationModel = TransactionTableOperation.getId(id)
            ?: TracsationModel(0, "???", Date(), -1)

    /**
     * <p>Сумма всех транзакций до даты</p>
     */
    fun getSumTo(date: Date): Int = TransactionTableOperation.getSumTo(
            Date(Date(date.year, date.month, date.date).time + 24 * 60 * 60 * 1000))

    /**
     * <p>Сумма на каждую категорию</p>
     *
     * @return HashMap<category_id, sum>
     */
    fun getCategorySum(startDate: Date, stopDate: Date): HashMap<Long, Int> = TransactionTableOperation.getCategorySum(startDate, stopDate)

    /**
     * <p>Удаляем транзакцию</p>
     */
    fun dell(id: Long) = TransactionTableOperation.dell(id)

    /**
     * <p>Изменение транзакции</p>
     */
    fun update(model: TracsationModel) = TransactionTableOperation.update(model)

    /**
     * <p>Список не реализованных транзакций</p>
     * <p>Транзакция может быть запланированна, а через кокоето время реализована.
     * Тоесть пользователь планирует покупку (не реализованная транзакция), а когда приходит
     * время покупает (реализует транзакцию)</p>
     *
     * @param date день до которого возвращать транзакции
     *
     */
    fun getNoDone(date: Date): List<TracsationModel> = TransactionTableOperation.getNoDone(Date(date.year, date.month, date.date))

}
