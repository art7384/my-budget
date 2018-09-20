package com.itechmobile.budget.ui.statistick.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.itechmobile.budget.R
import com.itechmobile.budget.logick.service.CategoryService
import com.itechmobile.budget.model.CategoryModel
import com.itechmobile.budget.model.TracsationModel

class TransactionStatickTitleListAdapter(var mCnx: Context) : BaseAdapter() {

    private val items: MutableList<Item> = ArrayList()

    companion object {
        private const val ITEM_VIEW_TYPE = 0
        private const val SECTION_VIEW_TYPE = 1
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val viewType = getItemViewType(position)
        if (viewType == IGNORE_ITEM_VIEW_TYPE) throw IllegalStateException("Failed to get object at position " + position)

        val v: View

        if (viewType == SECTION_VIEW_TYPE) {
            val li: LayoutInflater = mCnx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = li.inflate(R.layout.item_title_statistick, parent, false)
            v.findViewById<TextView>(R.id.title).text = items[position].categoryModel?.name
        } else {
            val li: LayoutInflater = mCnx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = li.inflate(R.layout.item_transaction_statistick, parent, false)

            val txtSumm = v.findViewById<TextView>(R.id.summ)
            val txtDdescription = v.findViewById<TextView>(R.id.description)
            val txtDate = v.findViewById<TextView>(R.id.date)
            val mMarker = v.findViewById<View>(R.id.marker)

            val tm = items[position].tracsationModel!!

            txtDdescription.text = tm.name
            if (tm.price < 0) {
                txtSumm.text = (tm.price * (-1)).toString()
                mMarker.setBackgroundColor(mCnx.resources.getColor(R.color.yellow))
            } else {
                txtSumm.text = tm.price.toString()
                mMarker.setBackgroundColor(mCnx.resources.getColor(R.color.green))
            }

            val arrDays = mCnx.resources.getStringArray(R.array.weekday)

            var numDay = tm.date.day - 1
            if(numDay < 0) numDay = arrDays.size - 1

            txtDate.text = "${arrDays[numDay]}, ${tm.date.date}"
        }

        return v

    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        if (item.tracsationModel != null) return ITEM_VIEW_TYPE
        if (item.categoryModel != null) return SECTION_VIEW_TYPE
        return IGNORE_ITEM_VIEW_TYPE
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

    fun update(transactions: List<TracsationModel>){
        items.clear()
        val group: HashMap<Long, MutableList<TracsationModel>> = HashMap()
        for(tr in transactions){
            val categoryId = tr.idCategory
            if(group[categoryId] == null){
                group[categoryId] = ArrayList()
            }
            group[categoryId]!!.add(tr)
        }
        val keys = group.keys
        for (key in keys) {
            items.add(Item(CategoryService.INSTANCE.get(key), null))
            for(tm in group[key]!!){
                items.add(Item(null, tm))
            }
        }
        this.notifyDataSetChanged()
    }

    private data class Item(val categoryModel: CategoryModel?, val tracsationModel: TracsationModel?)

}