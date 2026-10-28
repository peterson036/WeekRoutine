package com.example.myapplication.module

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.myapplication.ListData

object RoutineDataManager {
    val spKey = "preference_key"
    val spKeyMonthly = "preference_key_monthly"
    val rld = "routine_list_data"
    val rldMonthly = "routine_list_data_monthly"

    private lateinit var sp: SharedPreferences
    private lateinit var spMonthly: SharedPreferences

    fun init(context: Context) {
        sp = context.applicationContext.getSharedPreferences(
            spKey,
            Context.MODE_PRIVATE
        )
        spMonthly = context.applicationContext.getSharedPreferences(
            spKeyMonthly,
            Context.MODE_PRIVATE
        )
    }
    fun storeRoutineData(ld: ListData){
        val jsonString = ClassCustomizer.Json.encodeToString(ld)

        sp.edit {
            putString(
                rld + ld.orderNo.toString(),
                jsonString
            )
        }
    }
    fun storeMonthlyRoutineData(ld: ListData){
        val jsonString = ClassCustomizer.Json.encodeToString(ld)

        spMonthly.edit {
            putString(
                rldMonthly + ld.orderNo.toString(),
                jsonString
            )
        }
    }
    fun getRoutineData(orderNo: Int): ListData{
        val savedJsonString = sp.getString(rld + orderNo.toString(), "") ?: ""
        Log.d("Peter Test", "savedJsonString" + savedJsonString)
        var listData:ListData = ListData()
        listData.orderNo = orderNo
        if(!savedJsonString.isEmpty()) {
            try {
                val listDataFromString = stringToListData(savedJsonString)
                listData = listDataFromString
            } catch(e: Exception){

            }
        }
        return listData
    }
    fun getMonthlyRoutineData(orderNo: Int): ListData{
        val savedJsonString = spMonthly.getString(rldMonthly + orderNo.toString(), "") ?: ""
        Log.d("Peter Test", "savedJsonStringMonthly" + savedJsonString)
        var listData:ListData = ListData()
        listData.orderNo = orderNo
        if(!savedJsonString.isEmpty()) {
            try {
                val listDataFromString = stringToListData(savedJsonString)
                listData = listDataFromString
            } catch(e: Exception){

            }
        }
        return listData
    }
    fun stringToListData(jsonString: String): ListData {
        val ld:ListData = ClassCustomizer.Json.decodeFromString<ListData>(jsonString)
        return ld
    }
}