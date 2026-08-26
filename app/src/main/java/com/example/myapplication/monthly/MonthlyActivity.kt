package com.example.myapplication.monthly

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.DetailedActivity
import com.example.myapplication.ListAdapter
import com.example.myapplication.ListData
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class MonthlyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var listAdapter: ListAdapter
    private lateinit var listData: ListData
    var dataArrayList = ArrayList<ListData>()

    val myFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    var mCurrentDate = LocalDate.now().format(myFormatter)

    fun getThisWeekDays(): Array<String?> {
        val today = LocalDate.now()
        // 取得本週一
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        // 生成週一到週日的列表
        return ((0..6).map { (monday.plusDays(it.toLong())).format(myFormatter) }).toTypedArray()
    }

    enum class DATA_SOURCE {FROM_INIT, FROM_POST}

    fun getDataSource(sharedPref: SharedPreferences, dataSource:DATA_SOURCE):ArrayList<ListData>{
        if(dataSource == DATA_SOURCE.FROM_INIT){
            return getInitData(sharedPref)
        } else if(dataSource == DATA_SOURCE.FROM_POST){
            return getPostData()
        } else{
            return ArrayList<ListData>()
        }
    }

    enum class DAYS_OF_WEEK(val n: Int){
        NONE(0),
        MONDAY(1),
        TUESDAY(2),
        WEDNESDAY(3),
        THURSDAY(4),
        FRIDAY(5),
        SATURDAY(6),
        SUNDAY(7),
        WEEKDAY(8),
        WEEKEND(9),
        EVERYDAY(10)
    }
    fun getInitData(sharedPref: SharedPreferences):ArrayList<ListData> {

        val rawOrderNoList = intArrayOf(
            1, 2, 3,
            4, 5, 6,
            7, 8, 9,
            10, 11, 12,
            13, 14
        )

        val rawImageList = intArrayOf(
            R.drawable.chicken_drumstick,
            R.drawable.chicken_drumstick,
            R.drawable.chicken_drumstick,

            R.drawable.chicken_drumstick,
            R.drawable.chicken_drumstick,
            R.drawable.chicken_drumstick,

            R.drawable.chicken_drumstick,
            R.drawable.chicken_drumstick,
            R.drawable.chicken_drumstick,

            R.drawable.chicken_drumstick,
            R.drawable.chicken_drumstick,
            R.drawable.chicken_drumstick,

            R.drawable.chicken_drumstick,
            R.drawable.chicken_drumstick
        )

        val rawDescList = intArrayOf(
            R.string.noDesc,
            R.string.noDesc,
            R.string.noDesc,

            R.string.noDesc,
            R.string.noDesc,
            R.string.noDesc,

            R.string.noDesc,
            R.string.noDesc,
            R.string.noDesc,

            R.string.noDesc,
            R.string.myMonthlyRoutineDesc,
            R.string.myDailyRoutineDesc,

            R.string.myKettleDesc,
            R.string.noDesc
        )


        val rawNameList = arrayOf(
            "洗棉被、換床單、換枕頭套",
            "浴缸水龍頭要定期放水(每30天)"/*[防鐵鏽積水管]*/,
            "轉床墊",

            "理髮",
            "洗牙",
            "換濾心",

            "(EMPTY)", "(EMPTY)", "(EMPTY)",
            "(EMPTY)", "(EMPTY)", "(EMPTY)",
            "(EMPTY)", "(EMPTY)")



        /*
        * routineDaysOfWeek:
        * 1 for Mon
        * 2 for Tue
        * 3 for Wed
        * 4 for Thr
        * 5 for Fri
        * 6 for Sat
        * 7 for Sun
        * 8 for Weekday
        * 9 for Weekend
        * 10 for everyday
        * */



        val rawRoutineDaysOfWeek = arrayOf(
            intArrayOf(DAYS_OF_WEEK.EVERYDAY.n),
            intArrayOf(DAYS_OF_WEEK.EVERYDAY.n),
            intArrayOf(DAYS_OF_WEEK.EVERYDAY.n),

            intArrayOf(DAYS_OF_WEEK.EVERYDAY.n),
            intArrayOf(DAYS_OF_WEEK.EVERYDAY.n),
            intArrayOf(DAYS_OF_WEEK.EVERYDAY.n),

            intArrayOf(DAYS_OF_WEEK.MONDAY.n),
            intArrayOf(DAYS_OF_WEEK.MONDAY.n),
            intArrayOf(DAYS_OF_WEEK.MONDAY.n),

            intArrayOf(DAYS_OF_WEEK.MONDAY.n),
            intArrayOf(DAYS_OF_WEEK.MONDAY.n),
            intArrayOf(DAYS_OF_WEEK.MONDAY.n),

            intArrayOf(DAYS_OF_WEEK.MONDAY.n),
            intArrayOf(DAYS_OF_WEEK.MONDAY.n)
        )

        val today: LocalDate = LocalDate.now()
        val formattedDate = today.format(myFormatter)

        val mDayOfWeek = today.dayOfWeek.value

        var rawShowRoutine = booleanArrayOf(
            true, true, true,
            true, true, true,
            true, true, true,
            true, true, true,
            true, true)

        for (i in rawShowRoutine.indices) {
            rawShowRoutine[i] = false
            if(mDayOfWeek in rawRoutineDaysOfWeek[i]){
                rawShowRoutine[i] = true
            } else {
                var tempDay = DAYS_OF_WEEK.NONE.n
                when (mDayOfWeek) {
                    in 1..5 -> tempDay = DAYS_OF_WEEK.WEEKDAY.n
                    in 6 .. 7 -> tempDay = DAYS_OF_WEEK.WEEKEND.n
                }
                if(DAYS_OF_WEEK.EVERYDAY.n in rawRoutineDaysOfWeek[i]){
                    rawShowRoutine[i] = true
                }
                if(DAYS_OF_WEEK.WEEKDAY.n in rawRoutineDaysOfWeek[i] && tempDay == DAYS_OF_WEEK.WEEKDAY.n){
                    rawShowRoutine[i] = true
                }
                if(DAYS_OF_WEEK.WEEKEND.n in rawRoutineDaysOfWeek[i] && tempDay == DAYS_OF_WEEK.WEEKEND.n){
                    rawShowRoutine[i] = true
                }
            }

        }

        for(i in rawImageList.indices) {
            if (DAYS_OF_WEEK.WEEKDAY.n in rawRoutineDaysOfWeek[i]) {
                rawImageList[i] = R.drawable.chicken_drumstick
            }
            if (DAYS_OF_WEEK.WEEKEND.n in rawRoutineDaysOfWeek[i]) {
                rawImageList[i] = R.drawable.chicken_drumstick
            }
            if (rawOrderNoList[i] == 12) { //12 for 每日固定事項
                rawImageList[i] = R.drawable.chicken_drumstick
            }
        }


        var rawTimeList = arrayOf(
            "2026/04/05", "2026/03/24", "2026/05/07",
            "2026/03/22", "2025/05/07", "2025/09/13",
            "2025/01/01", "2025/01/01", "2025/01/01",
            "2025/01/01", "2025/01/01", "2025/01/01",
            "2025/01/01", "2025/01/01")

        // val timeList = arrayOf("30 mins", "2 mins", "45 mins", "10 mins", "60 mins", "45 mins", "30 mins")

        val backup_last_time = sharedPref.getString(getString(R.string.pf_backup_last_time),"") ?: ""

        if(backup_last_time != "") {
            val lastTimes = backup_last_time.split(",").toTypedArray()
            for(i in lastTimes.indices){
                rawTimeList[i] = lastTimes[i]
            }
        }

        // 本週平日日期的陣列和本週假日日期的陣列，再加上上次完成日期。就可以得出isFinish陣列

        var rawIsFinishList = booleanArrayOf(
            false, false, false,
            false, false, false,
            false, false, false,
            false, false, false,
            false, false)

        val thisWeek = getThisWeekDays()

        for (i in rawIsFinishList.indices) {
            if((DAYS_OF_WEEK.WEEKDAY.n in rawRoutineDaysOfWeek[i]) && (rawTimeList[i] in thisWeek)){
                rawIsFinishList[i] = true
            } else if((DAYS_OF_WEEK.WEEKEND.n in rawRoutineDaysOfWeek[i]) && (rawTimeList[i] in thisWeek)){
                rawIsFinishList[i] = true
            } else if(rawTimeList[i] == formattedDate) {
                rawIsFinishList[i] = true
            }
        }

        val rawTimeDescList = arrayOf(
            "30", "30", "37",
            "40", "180", "360",
            "", "", "",
            "", "", "",
            "", "")

        var rawListData:ArrayList<ListData> = ArrayList<ListData>()

        for (i in rawImageList.indices) {

            rawListData.add(
                ListData(
                    rawOrderNoList[i], rawNameList[i],
                    rawTimeList[i], rawDescList[i],
                    rawImageList[i], rawRoutineDaysOfWeek[i],
                    rawShowRoutine[i] ,rawIsFinishList[i],
                    rawTimeDescList[i])
            )


        }



        return rawListData

    }

    fun getPostData():ArrayList<ListData> {
        return ArrayList<ListData>()
    }

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


    var mMyInitData:ArrayList<ListData> = ArrayList<ListData>()
    lateinit var mLastTimesList: ArrayList<String>
    lateinit var mListViewMapping: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences(
            getString(R.string.preference_key_monthly), Context.MODE_PRIVATE)


        // TODO: 資料來源可擴充。
        //       改成可以同時相容於1.內建資料 2.遠端資料庫撈取
        // TODO: 可以一次顯示所有事項的最後完成日期。
        //      方便手動更新程式碼內建資料的最後完成日期。
        //      (要安裝在不同手機時會用到。)
        updateListviewToToday(sharedPref)


    }

    fun updateListviewToToday(sharedPref: SharedPreferences){
        initData(sharedPref)
        initAdapter(sharedPref)
    }

    fun initData(sharedPref: SharedPreferences){
        mMyInitData = getDataSource(sharedPref, DATA_SOURCE.FROM_INIT)
        mLastTimesList = getTimeList(mMyInitData)

    }
    fun initAdapter(sharedPref: SharedPreferences){
        val today: LocalDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val formattedDate = today.format(formatter)

        val showRoutine = getShowRoutine(mMyInitData)

        mListViewMapping = IntArray(mMyInitData.size){it + 1}

        dataArrayList.clear()

        var j = 0;
        for (i in mLastTimesList.indices) {
            if(showRoutine[i]) {
                mListViewMapping[j] = i
                dataArrayList.add(mMyInitData[i])
                j++
            }
        }
        listAdapter = ListAdapter(this@MonthlyActivity, dataArrayList)
        binding.listview.adapter = listAdapter
        binding.listview.isClickable = true

        binding.listview.onItemLongClickListener = AdapterView.OnItemLongClickListener{ parent, view, position, id ->
            var tempData = dataArrayList.get(position)

            if(tempData.time != formattedDate) {
                tempData.time = formattedDate

                val dataIndex = tempData.orderNo.minus(1) ?: 0
                if (dataIndex >= 0 && dataIndex < mLastTimesList.size) {
                    mLastTimesList[dataIndex] = formattedDate
                    tempData.time =  formattedDate
                    tempData.isFinish = true
                    mMyInitData.set(dataIndex, tempData)
                }

                sharedPref.edit {
                    putString(
                        getString(R.string.pf_backup_last_time),
                        mLastTimesList.joinToString(",")
                    )
                }
                dataArrayList.set(position, tempData)
                listAdapter.notifyDataSetChanged()
            }
            true
        }

    }

    override fun onResume() {
        super.onResume()

        showRefreshBtnIfDateChanged()
    }

    fun showRefreshBtnIfDateChanged(){
        val today: LocalDate = LocalDate.now()
        val resumeFormattedDate = today.format(myFormatter)
        if(resumeFormattedDate != mCurrentDate){
            binding.ivRefreshToToday.visibility = View.VISIBLE
        }
    }

    fun hideRefreshBtn(){
        binding.ivRefreshToToday.visibility = View.INVISIBLE

    }
}