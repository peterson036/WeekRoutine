package com.example.myapplication


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import kotlin.arrayOf
import androidx.core.content.edit


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listAdapter: ListAdapter
    private lateinit var listData: ListData
    var dataArrayList = ArrayList<ListData?>()

    val myFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    var mCurrentDate = LocalDate.now().format(myFormatter)

    fun getThisWeekDays(): Array<String?> {
        val today = LocalDate.now()
        // 取得本週一
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        // 生成週一到週日的列表
        return ((0..6).map { (monday.plusDays(it.toLong())).format(myFormatter) }).toTypedArray()
    }
    val thisWeek = getThisWeekDays()

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
            R.drawable.pancake,
            R.drawable.pancake,
            R.drawable.pancake,

            R.drawable.pancake,
            R.drawable.pancake,
            R.drawable.pancake,

            R.drawable.pancake,
            R.drawable.pancake,
            R.drawable.pancake,

            R.drawable.pancake,
            R.drawable.pancake,
            R.drawable.pancake,

            R.drawable.pancake,
            R.drawable.pancake
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
            "洗澡", "刮鬍子", "洗衣服",
            "擦床頭/抖枕頭", "喝牛奶", "改善居家環境",
            "為周末預備體力", "掃地拖地_嗎", "修指甲_嗎",
            "祭祀兄弟_可", "月度定期事項", "每日固定事項",
            "給熱水壺加水", "吃葉黃素凍")

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
            intArrayOf(DAYS_OF_WEEK.WEEKDAY.n),
            intArrayOf(DAYS_OF_WEEK.WEEKEND.n),

            intArrayOf(DAYS_OF_WEEK.WEEKEND.n),
            intArrayOf(DAYS_OF_WEEK.TUESDAY.n,
                       DAYS_OF_WEEK.FRIDAY.n),
            intArrayOf(DAYS_OF_WEEK.MONDAY.n),

            intArrayOf(DAYS_OF_WEEK.THURSDAY.n),
            intArrayOf(DAYS_OF_WEEK.FRIDAY.n),
            intArrayOf(DAYS_OF_WEEK.FRIDAY.n),

            intArrayOf(DAYS_OF_WEEK.WEDNESDAY.n),
            intArrayOf(DAYS_OF_WEEK.SUNDAY.n),
            intArrayOf(DAYS_OF_WEEK.EVERYDAY.n),

            intArrayOf(DAYS_OF_WEEK.WEDNESDAY.n,
                       DAYS_OF_WEEK.SATURDAY.n),
            intArrayOf(DAYS_OF_WEEK.WEEKDAY.n)
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
                rawImageList[i] = R.drawable.pizza
            }
            if (DAYS_OF_WEEK.WEEKEND.n in rawRoutineDaysOfWeek[i]) {
                rawImageList[i] = R.drawable.burger
            }
            if (rawOrderNoList[i] == 12) { //12 for 每日固定事項
                rawImageList[i] = R.drawable.fries
            }
        }


        var rawTimeList = arrayOf(
            "2026/03/02", "2026/03/02", "2026/03/01",
            "2026/02/22", "2026/02/27", "2026/03/02",
            "2026/02/26", "2026/02/14", "2026/02/22",
            "2026/02/08", "2026/02/09", "2026/03/04",
            "2026/03/14", "2026/03/17")

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

        for (i in rawIsFinishList.indices) {
            if((DAYS_OF_WEEK.WEEKDAY.n in rawRoutineDaysOfWeek[i]) && (rawTimeList[i] in thisWeek)){
                rawIsFinishList[i] = true
            } else if((DAYS_OF_WEEK.WEEKEND.n in rawRoutineDaysOfWeek[i]) && (rawTimeList[i] in thisWeek)){
                rawIsFinishList[i] = true
            } else if(rawTimeList[i] == formattedDate) {
                rawIsFinishList[i] = true
            }
        }

        var rawListData:ArrayList<ListData> = ArrayList<ListData>()

        for (i in rawImageList.indices) {

            rawListData.add(
                    ListData(
                        rawOrderNoList[i], rawNameList[i],
                        rawTimeList[i], rawDescList[i],
                        rawImageList[i], rawRoutineDaysOfWeek[i],
                        rawShowRoutine[i] ,rawIsFinishList[i])
            )


        }
        return rawListData

    }

    fun getPostData():ArrayList<ListData> {
        return ArrayList<ListData>()
    }

    fun getOrderList(listData:ArrayList<ListData>):ArrayList<Int>{
        var myArray = ArrayList<Int>()
        for(i in listData.indices){
            myArray.add(listData[i].orderNo)
        }

        return myArray
    }

    fun getImageList(listData:ArrayList<ListData>):ArrayList<Int>{
        var myArray = ArrayList<Int>()
        for(i in listData.indices){
            myArray.add(listData[i].image)
        }

        return myArray
    }

    fun getDescList(listData:ArrayList<ListData>):ArrayList<Int>{
        var myArray = ArrayList<Int>()
        for(i in listData.indices){
            myArray.add(listData[i].desc)
        }

        return myArray
    }

    fun getNameList(listData:ArrayList<ListData>):ArrayList<String>{
        var myArray = ArrayList<String>()
        for(i in listData.indices){
            myArray.add(listData[i].name)
        }

        return myArray
    }

    fun getRoutineDaysOfWeek(listData:ArrayList<ListData>):ArrayList<IntArray>{
        var myArray = ArrayList<IntArray>()
        for(i in listData.indices){
            myArray.add(listData[i].routineDaysOfWeek)
        }

        return myArray
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
    fun getIsFinishList(listData:ArrayList<ListData>):ArrayList<Boolean>{
        var myArray = ArrayList<Boolean>()
        for(i in listData.indices){
            myArray.add(listData[i].isFinish)
        }

        return myArray
    }


    var mMyInitData:ArrayList<ListData> = ArrayList<ListData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val sharedPref = getSharedPreferences(
            getString(R.string.preference_key), Context.MODE_PRIVATE)

        // TODO: 資料來源可擴充。
        //       改成可以同時相容於1.內建資料 2.遠端資料庫撈取
        // TODO: 可以一次顯示所有事項的最後完成日期。
        //      方便手動更新程式碼內建資料的最後完成日期。
        //      (要安裝在不同手機時會用到。)

        mMyInitData = getDataSource(sharedPref, DATA_SOURCE.FROM_INIT)

        val orderNoList = getOrderList(mMyInitData)
        val imageList = getImageList(mMyInitData)
        val descList = getDescList(mMyInitData)
        val nameList = getNameList(mMyInitData)
        var routineDaysOfWeek = getRoutineDaysOfWeek(mMyInitData)

        val today: LocalDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val formattedDate = today.format(formatter)

        var showRoutine = getShowRoutine(mMyInitData)
        var timeList = getTimeList(mMyInitData)
        var isFinishList = getIsFinishList(mMyInitData)

        var listViewMapping = IntArray(mMyInitData.size){it + 1}

        var j = 0;
        for (i in imageList.indices) {
            if(showRoutine[i]) {
                listViewMapping[j] = i
                dataArrayList.add(mMyInitData[i])
                j++
            }
        }
        listAdapter = ListAdapter(this@MainActivity, dataArrayList)
        binding.listview.adapter = listAdapter
        binding.listview.isClickable = true
        binding.listview.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this@MainActivity, DetailedActivity::class.java)
            val realNo = listViewMapping[i]

            intent.putExtra("orderNo", realNo)
            intent.putExtra("name", nameList[realNo])
            intent.putExtra("time", timeList[realNo])
            intent.putExtra("desc", descList[realNo])
            intent.putExtra("image", imageList[realNo])
            intent.putExtra("isfinish", isFinishList[realNo])
            startActivity(intent)
        }

        binding.listview.onItemLongClickListener = AdapterView.OnItemLongClickListener{ parent, view, position, id ->
            var tempData = dataArrayList.get(position)

            if(tempData?.time != formattedDate) {
                tempData?.time = formattedDate

                var lastTimesList = timeList

                val dataIndex = tempData?.orderNo?.minus(1) ?: 0
                if (dataIndex >= 0 && dataIndex < timeList.size) {
                    lastTimesList[dataIndex] = formattedDate
                    timeList[dataIndex] = formattedDate
                    isFinishList[dataIndex] = true
                }

                sharedPref.edit {
                    putString(
                        getString(R.string.pf_backup_last_time),
                        lastTimesList.joinToString(",")
                    )
                }
                dataArrayList.set(position, tempData)
                listAdapter.notifyDataSetChanged()
            }
            true
        }

        binding.ivRefreshToToday.setOnClickListener {
            reopenApp()
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
    fun reopenApp(){
            val intent = getIntent()
            finish()
            startActivity(intent)
    }

}

