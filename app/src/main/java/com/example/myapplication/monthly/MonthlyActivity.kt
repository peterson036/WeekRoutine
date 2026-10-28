package com.example.myapplication.monthly

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.DetailedActivity
import com.example.myapplication.ListAdapter
import com.example.myapplication.ListData
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import com.example.myapplication.MainActivity.DAYS_OF_WEEK
import com.example.myapplication.MainActivity.DATA_SOURCE
import com.example.myapplication.module.ClassCustomizer
import com.example.myapplication.module.RoutineDataManager
import com.example.myapplication.module.RoutineListHandler
import org.json.JSONArray

class MonthlyActivity : AppCompatActivity(), RoutineListHandler {
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

    // enum class DATA_SOURCE {FROM_INIT, FROM_POST}


    override fun getDataSource(sharedPref: SharedPreferences, dataSource:DATA_SOURCE):ArrayList<ListData>{
        if(dataSource == DATA_SOURCE.FROM_INIT){
            return getInitData_byObject(sharedPref)
        } else if(dataSource == DATA_SOURCE.FROM_POST){
            return getPostData()
        } else{
            return ArrayList<ListData>()
        }
    }

    override fun getInitData_byObject(sharedPref: SharedPreferences):ArrayList<ListData> {
        var rawListData:ArrayList<ListData> = ArrayList<ListData>()

        val default_date = "2026/08/15"
        val default_desc = R.string.noDesc
        val default_routineDay = intArrayOf(DAYS_OF_WEEK.EVERYDAY.n)
        val default_image = R.drawable.chicken_drumstick
        val default_showRoutine = true
        val default_isFinish = false

        rawListData.add(
            ListData(
                1, "洗棉被、換床單、換枕頭套",
                "2026/10/01", getString(default_desc),
                default_image, default_routineDay,
                default_showRoutine ,default_isFinish,
                "30")
        )

        rawListData.add(
            ListData(
                2, "浴缸水龍頭要定期放水"/*[防鐵鏽積水管]*/,
                "2026/08/31", getString(default_desc),
                default_image, default_routineDay,
                default_showRoutine ,default_isFinish,
                "30")
        )

        rawListData.add(
            ListData(
                3, "轉床墊",
                "2026/09/30", getString(default_desc),
                default_image, default_routineDay,
                default_showRoutine ,default_isFinish,
                "37")
        )

        rawListData.add(
            ListData(
                4, "理髮",
                "2026/09/15", getString(default_desc),
                default_image, default_routineDay,
                default_showRoutine ,default_isFinish,
                "40")
        )

        rawListData.add(
            ListData(
                5, "洗牙",
                "2025/05/07", getString(default_desc),
                default_image, default_routineDay,
                default_showRoutine ,default_isFinish,
                "180")
        )

        rawListData.add(
            ListData(
                6, "換濾心",
                "2025/09/13", getString(default_desc),
                default_image, default_routineDay,
                default_showRoutine ,default_isFinish,
                "400")
        )

        // Empty Routines
        rawListData.add(
            ListData(
                7, "",
                "", getString(default_desc),
                default_image, intArrayOf(DAYS_OF_WEEK.NONE.n),
                false ,false,
                "")
        )

        rawListData.add(
            ListData(
                8, "",
                "", getString(default_desc),
                default_image, intArrayOf(DAYS_OF_WEEK.NONE.n),
                false ,false,
                "")
        )

        rawListData.add(
            ListData(
                9, "",
                "", getString(default_desc),
                default_image, intArrayOf(DAYS_OF_WEEK.NONE.n),
                false ,false,
                "")
        )

        rawListData.add(
            ListData(
                10, "",
                "", getString(default_desc),
                default_image, intArrayOf(DAYS_OF_WEEK.NONE.n),
                false ,false,
                "")
        )

        return refineSourceObjects(rawListData)
    }

    override fun refineSourceObjects(source: ArrayList<ListData> ): ArrayList<ListData>{
        for(i in source.indices) {
            if (MainActivity.DAYS_OF_WEEK.WEEKDAY.n in source[i].routineDaysOfWeek) {
                source[i].image = R.drawable.pizza
            }
            if (MainActivity.DAYS_OF_WEEK.WEEKEND.n in source[i].routineDaysOfWeek) {
                source[i].image = R.drawable.burger
            }
        }


        val thisWeek = getThisWeekDays()
        val today: LocalDate = LocalDate.now()
        val formattedDate = today.format(myFormatter)

        val mDayOfWeek = today.dayOfWeek.value

        for (i in source.indices) {
            source[i].showRoutine = false
            if (mDayOfWeek in source[i].routineDaysOfWeek) {
                source[i].showRoutine = true
            } else {
                var tempDay = DAYS_OF_WEEK.NONE.n
                when (mDayOfWeek) {
                    in 1..5 -> tempDay = DAYS_OF_WEEK.WEEKDAY.n
                    in 6..7 -> tempDay = DAYS_OF_WEEK.WEEKEND.n
                }
                if (DAYS_OF_WEEK.EVERYDAY.n in source[i].routineDaysOfWeek) {
                    source[i].showRoutine = true
                }
                if (DAYS_OF_WEEK.WEEKDAY.n in source[i].routineDaysOfWeek && tempDay == MainActivity.DAYS_OF_WEEK.WEEKDAY.n) {
                    source[i].showRoutine = true
                }
                if (DAYS_OF_WEEK.WEEKEND.n in source[i].routineDaysOfWeek && tempDay == MainActivity.DAYS_OF_WEEK.WEEKEND.n) {
                    source[i].showRoutine = true
                }
            }
        }

        for (i in source.indices) {
            if((MainActivity.DAYS_OF_WEEK.WEEKDAY.n in source[i].routineDaysOfWeek) && (source[i].time in thisWeek)){
                source[i].isFinish = true
            } else if((MainActivity.DAYS_OF_WEEK.WEEKEND.n in source[i].routineDaysOfWeek) && (source[i].time in thisWeek)){
                source[i].isFinish = true
            } else if(source[i].time == formattedDate) {
                source[i].isFinish = true
            }
        }
        return source;
    }

        fun getPostData():ArrayList<ListData> {
        return ArrayList<ListData>()
    }

    override fun getShowRoutine(listData:ArrayList<ListData>):ArrayList<Boolean>{
        var myArray = ArrayList<Boolean>()
        for(i in listData.indices){
            myArray.add(listData[i].showRoutine)
        }

        return myArray
    }
    override fun getTimeList(listData:ArrayList<ListData>):ArrayList<String>{
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

        val sharedPref = getMonthlyRoutineSharePreference()

        // TODO: 資料來源可擴充。
        //       改成可以同時相容於1.內建資料 2.遠端資料庫撈取
        // TODO: 可以一次顯示所有事項的最後完成日期。
        //      方便手動更新程式碼內建資料的最後完成日期。
        //      (要安裝在不同手機時會用到。)
        updateListviewToToday(sharedPref)


    }

    fun updateListviewToToday(sharedPref: SharedPreferences){
        initData(sharedPref)
        initAdapter(sharedPref, false, false)
    }
    fun updateListviewToToday(sharedPref: SharedPreferences, isShowAll:Boolean, isShowEmpty:Boolean){
        initData(sharedPref)
        initAdapter(sharedPref, isShowAll, isShowEmpty)
    }

    fun initData(sharedPref: SharedPreferences){
        setInitDataIfNeed(sharedPref)
        mMyInitData = getCurrentData(sharedPref)
        mLastTimesList = getTimeList(mMyInitData)

    }

    override fun setInitDataIfNeed(sharedPref: SharedPreferences){
        val newest_initdata_version = "1.0.0"
        val pf_had_init = sharedPref.getBoolean(getString(R.string.pf_had_init),false)
        val pf_initdata_version = sharedPref.getString(getString(R.string.pf_initdata_version),"")

        if(!pf_had_init || !pf_initdata_version.equals(newest_initdata_version)) {
            var initData:ArrayList<ListData> = ArrayList<ListData>()
            initData = getDataSource(sharedPref, DATA_SOURCE.FROM_INIT)

            val jsonString = ClassCustomizer.Json.encodeToString(initData)

            sharedPref.edit {
                putString(
                    getString(R.string.pf_init_data_in_string),
                    jsonString
                )
            }

            println("PUTSTRING:" + jsonString)

            sharedPref.edit {
                putBoolean(
                    getString(R.string.pf_had_init),
                    true
                )
            }

            sharedPref.edit {
                putString(
                    getString(R.string.pf_initdata_version),
                    newest_initdata_version
                )
            }
        }
    }

    override fun getCurrentData(sharedPref: SharedPreferences):ArrayList<ListData>{
        var initData:ArrayList<ListData> = ArrayList<ListData>()

        val savedJson = sharedPref.getString(getString(R.string.pf_init_data_in_string), "[]")

        println("GETSTRING:" + savedJson)
        val jsonArray = JSONArray(savedJson)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val orderNo = obj.getInt("orderNo")
            val name = obj.getString("name")
            val time = obj.getString("time")
            val desc = obj.getString("desc")
            val image = obj.getInt("image")
            val daysArray = obj.getJSONArray("routineDaysOfWeek")
            val days = IntArray(daysArray.length()) { index ->
                daysArray.getInt(index)
            }
            val showRoutine = obj.getBoolean("showRoutine")
            val isFinish = obj.getBoolean("isFinish")
            val timeDesc = obj.getString("timeDesc")

            val storedListData:ListData = RoutineDataManager.getMonthlyRoutineData(orderNo)
            if(storedListData.name.isEmpty()) {
                initData.add(
                    ListData(
                        orderNo = orderNo,
                        name = name,
                        time = time,
                        desc = desc,
                        image = image,
                        routineDaysOfWeek = days,
                        showRoutine = showRoutine,
                        isFinish = isFinish,
                        timeDesc = timeDesc
                    )
                )
            } else {
                initData.add(storedListData)
            }
        }

        val backup_last_time = sharedPref.getString(getString(R.string.pf_backup_last_time),"") ?: ""
        if(backup_last_time != "") {
            val lastTimes = backup_last_time.split(",").toTypedArray()
            for(i in lastTimes.indices){
                initData[i].time = lastTimes[i]
            }
        }
        return refineSourceObjects(initData)
    }
    var mIsShowAll: Boolean = false
    var mIsShowEmpty: Boolean = false
    override fun initAdapter(sharedPref: SharedPreferences, showAll:Boolean, showEmpty:Boolean){
        val today: LocalDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val formattedDate = today.format(formatter)

        val showRoutine = getShowRoutine(mMyInitData)

        mListViewMapping = IntArray(mMyInitData.size){it + 1}

        dataArrayList.clear()

        var j = 0;
        for (i in mLastTimesList.indices) {
            val alwaysShowAll = true || showAll
            val isEmpty = isItemEmpty(mMyInitData[i])
            val shouldShow = (alwaysShowAll && !isEmpty) || (alwaysShowAll && showEmpty && isEmpty)

            if(showRoutine[i] || shouldShow) {
                mListViewMapping[j] = i
                dataArrayList.add(mMyInitData[i])
                j++
            }
        }
        listAdapter = ListAdapter(this@MonthlyActivity, dataArrayList)
        binding.listview.adapter = listAdapter
        binding.listview.isClickable = true

        binding.listview.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->

                val realNo = mListViewMapping[i]
                val data = mMyInitData.get(realNo)

                val isMonthly = true
                if(isMonthly){
                    val intent = Intent(this@MonthlyActivity, MonthlyDetailedActivity::class.java)

                    intent.putExtra("orderNo", data.orderNo)
                    intent.putExtra("name", data.name)
                    intent.putExtra("time", data.time)
                    intent.putExtra("desc", data.desc)
                    intent.putExtra("image", data.image)
                    intent.putExtra("isfinish", data.isFinish)
                    intent.putExtra("timeDesc", data.timeDesc)
                    editItemLauncher.launch(intent)
                }

            }

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

        binding.swSelectAll.visibility = View.INVISIBLE
        binding.cbShowEmpty.visibility = View.VISIBLE
        binding.tvToday.visibility = View.INVISIBLE

        binding.cbShowEmpty.setOnCheckedChangeListener { buttonView, isChecked ->
            mIsShowEmpty = isChecked
            updateListviewToToday(sharedPref, mIsShowAll, mIsShowEmpty)
        }

        binding.btBack.visibility = View.VISIBLE
        binding.btBack.setOnClickListener {
            finish()
        }

    }

    private val editItemLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {
                Log.d("Peter Test:", "MonthlyActivity Recieved RESULT_OK")
                val orderNo = result.data?.getIntExtra("orderNo", 0)
                Log.d("Peter Test:", "MonthlyActivity Recieved RESULT_OK:orderNo=" + orderNo)

                if(orderNo != 0) {
                    Log.d("Peter Test:", "MonthlyActivity Recieved RESULT_OK:updateListviewToToday()")
                    updateListviewToToday(getMonthlyRoutineSharePreference(), mIsShowAll, mIsShowEmpty)
                }
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

    fun getMonthlyRoutineSharePreference(): SharedPreferences{
        return getSharedPreferences(
            getString(R.string.preference_key_monthly), Context.MODE_PRIVATE)
    }
}