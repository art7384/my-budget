package com.itechmobile.budget.ui.editor.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.itechmobile.budget.R
import java.util.*


/**
 * Created by artem on 09.01.18.
 */
class EmojiGridAdapter(var items: ArrayList<String>): BaseAdapter() {

//    private var startTime = 0L

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if(view == null){
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_emojis, parent, false)
        }
        val txt = view!!.findViewById<TextView>(R.id.itemEmojis_TextView_txt)
        val category = getItem(position) as String
        txt.text = category

//        txt.setOnTouchListener { v, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN // нажатие
//                -> startTime = Date().time
//                MotionEvent.ACTION_MOVE // движение
//                -> {
//                }
//                MotionEvent.ACTION_UP // отпускание
//                    , MotionEvent.ACTION_CANCEL -> {
//                    val totalTime = Date().time - startTime
//                    if (totalTime >= 3000) {
//                        if (mOnClickButtonListner != null) mOnClickButtonListner.onTouch(position)
//                    } else {
//                        if (mOnClickButtonListner != null) mOnClickButtonListner.onClick(position)
//                    }
//                }
//            }
//            return@setOnTouchListener true
//
//        }

        return view
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

    fun newEmoji(emoges: Array<String>){
        items.clear()
        items.addAll(emoges)
        notifyDataSetChanged()
    }

//    interface OnClickButtonListner {
//        fun onClick(position: Int)
//        fun onTouch(position: Int)
//    }

}