package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ListAdapter(context: Context, dataArrayList: ArrayList<ListData>) :
    ArrayAdapter<ListData?>(context, R.layout.list_item, dataArrayList!!) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var view = view
        val listData = getItem(position)
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }
        val listImage = view!!.findViewById<ImageView>(R.id.listImage)
        val listName = view.findViewById<TextView>(R.id.listName)
        val listTime = view.findViewById<TextView>(R.id.listTime)
        listImage.setImageResource(listData!!.image)
        listName.text = listData.name
        listTime.text = listData.time

        val today: LocalDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val formattedDate = today.format(formatter)
        if(formattedDate == listData.time || listData.isFinish){
            listName.text = listData.name.plus( "(完成)")
        }


        return view
    }
}