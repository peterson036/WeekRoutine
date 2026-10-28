package com.example.myapplication.module

import android.content.SharedPreferences
import com.example.myapplication.ListData
import com.example.myapplication.MainActivity.DATA_SOURCE
import com.example.myapplication.MainActivity.DAYS_OF_WEEK

interface RoutineListHandler {
    fun setInitDataIfNeed(sharedPref: SharedPreferences)
    fun getInitData_byObject(sharedPref: SharedPreferences):ArrayList<ListData>
    fun getDataSource(sharedPref: SharedPreferences, dataSource:DATA_SOURCE):ArrayList<ListData>
    fun getCurrentData(sharedPref: SharedPreferences):ArrayList<ListData>
    fun initAdapter(sharedPref: SharedPreferences, showAll:Boolean, showEmpty:Boolean)
    fun refineSourceObjects(source: ArrayList<ListData> ): ArrayList<ListData>
    fun getShowRoutine(listData:ArrayList<ListData>):ArrayList<Boolean>{
        var myArray = ArrayList<Boolean>()
        for(i in listData.indices){
            myArray.add(listData[i].showRoutine)
        }

        return myArray
    }
    fun getTimeList(listData:ArrayList<ListData>):ArrayList<String>{
        var myArray = ArrayList<String>()
        for(i in listData.indices){
            myArray.add(listData[i].time)
        }

        return myArray
    }
    fun isItemEmpty(listData: ListData):Boolean {
        var isEmpty: Boolean = false
        if(listData.name.isEmpty() && (DAYS_OF_WEEK.NONE.n in listData.routineDaysOfWeek)){
            isEmpty = true
        }
        return isEmpty
    }

}