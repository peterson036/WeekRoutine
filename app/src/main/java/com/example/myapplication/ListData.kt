package com.example.myapplication

import kotlinx.serialization.Serializable

@Serializable
data class ListData (
    var orderNo: Int = 0,
    var name: String = "",
    var time: String = "",
    var desc: String = "",
    var image: Int = R.drawable.pancake,
    var routineDaysOfWeek: IntArray = IntArray(MainActivity.DAYS_OF_WEEK.NONE.n),
    var showRoutine: Boolean = false,
    var isFinish: Boolean = false,
    var timeDesc: String = ""
)