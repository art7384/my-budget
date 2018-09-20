package com.itechmobile.budget.logick.parsers

import com.itechmobile.budget.logick.datebase.TransactionTable
import com.itechmobile.budget.model.TracsationModel

/**
 * Created by artem on 05.03.18.
 */
class TransactionParser {

    companion object {
        fun toRealm(list: List<TracsationModel>): ArrayList<TransactionTable> {
            val arrTrTb = ArrayList<TransactionTable>()
            list.map {
                arrTrTb.add(toRealm(it))
            }
            return arrTrTb
        }

        fun toRealm(trMd: TracsationModel): TransactionTable {

            val tt = TransactionTable(trMd.name,
                    trMd.price,
                    trMd.date,
                    trMd.isDone,
                    trMd.idCategory)
            tt.id = trMd.id

            return tt
        }

        fun from(list: List<TransactionTable>): ArrayList<TracsationModel> {
            val arrTrMd = ArrayList<TracsationModel>()
            list.map {
                arrTrMd.add(from(it))
            }
            return arrTrMd
        }

        fun from(trTb: TransactionTable): TracsationModel {
            val trMd = TracsationModel(trTb.money, trTb.name, trTb.date, trTb.categoryId)
            trMd.id = trTb.id
            trMd.isDone = trTb.isDone
            return trMd
        }
    }
}