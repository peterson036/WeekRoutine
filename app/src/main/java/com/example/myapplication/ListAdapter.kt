package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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
        val listTimeDesc = view.findViewById<TextView>(R.id.listTimeDesc)
        val listTime = view.findViewById<TextView>(R.id.listTime)
        listImage.setImageResource(listData!!.image)
        listName.text = listData.name

        if(listData.timeDesc != ""){
            var blankPrefix = ""
            if(listData.timeDesc.length == 1){
                blankPrefix = "  "
            } else {
                blankPrefix = " "
            }
            val percentageText = getPercentage(getDiffDays(listData.time), listData.timeDesc)
            listTimeDesc.text = percentageText + " x " + blankPrefix +  listData.timeDesc + "天"

            val percentageDouble = percentageText.toDouble()
            if(percentageDouble >= 1.3){
                listTimeDesc.setTextColor(Color.RED)
            } else if (percentageDouble > 0.7){
                listTimeDesc.setTextColor(Color.BLUE)
            }
        }




        listTime.text = listData.time

        val today: LocalDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val formattedDate = today.format(formatter)
        if(formattedDate == listData.time || listData.isFinish){
            listName.text = listData.name.plus( "(完成)")
        }


        return view
    }

    fun getPercentage(day1:String, day2:String):String {
        val day1Int =  day1.toInt()
        val day2Int = day2.toInt()
        var percentage:String = "None"
        if(day2Int != 0){
            percentage = "%.2f".format(day1Int.toDouble() / day2Int)
        }
        return percentage
    }
    fun getDiffDays(dateString:String):String {
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

        val targetDate = LocalDate.parse(dateString, formatter)

        val today = LocalDate.now()

        val daysDiff = ChronoUnit.DAYS.between( targetDate, today)

        return daysDiff.toString()
    }
}