package com.itechmobile.budget.ui.editor.category.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.itechmobile.budget.R
import com.itechmobile.budget.model.CategoryModel

/**
 * Created by artem on 20.12.17.
 */
class CategoryAdapter(var items: ArrayList<CategoryModel>) : BaseAdapter() {

    var onClickPopapMenu: (btView: View, categoryModel:CategoryModel) -> Unit = {_,_->}
    var onClickItem: (btView: View, categoryModel:CategoryModel) -> Unit = {_,_->}

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val category = getItem(position) as CategoryModel

        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_category, parent, false)
        }
        val emogi = view!!.findViewById<TextView>(R.id.itemCategory_TextView_emogi)
        val name = view.findViewById<TextView>(R.id.itemCategory_TextView_name)
        val menuBt = view.findViewById<ImageButton>(R.id.itemCategory_ImageButton_menu)
        view.findViewById<View>(R.id.btItem).setOnClickListener {
            onClickItem(it, category)
        }

        menuBt.setOnClickListener {
            onClickPopapMenu(it, category)
        }

        emogi.text = category.icoName
        name.text = category.name

        return view
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    override fun getCount(): Int {
        return items.size
    }

    fun update(newItems: List<CategoryModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

}
