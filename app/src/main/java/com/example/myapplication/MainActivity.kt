package com.example.myapplication


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import androidx.core.content.edit
import com.example.myapplication.module.ClassCustomizer
import com.example.myapplication.module.RoutineDataManager
import com.example.myapplication.module.RoutineListHandler
import com.example.myapplication.module.SecretNumber
import com.example.myapplication.monthly.MonthlyActivity
import org.json.JSONArray
import kotlin.collections.contains


class MainActivity : AppCompatActivity(), RoutineListHandler {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listAdapter: ListAdapter
    var dataArrayList = ArrayList<ListData>()

    val myFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    var mCurrentDate = ""

    fun getThisWeekDays(): Array<String?> {
        val today = LocalDate.now()
        // 取得本週一
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        // 生成週一到週日的列表
        return ((0..6).map { (monday.plusDays(it.toLong())).format(myFormatter) }).toTypedArray()
    }

    enum class DATA_SOURCE {FROM_INIT, FROM_POST}

    enum class USE_CASES {CASE_1_DEFAULT_SELF_USE_byOBJECTS, CASE_2_BOLT_PREVENTION_byOBJECTS }

    val myUseCase = USE_CASES.CASE_1_DEFAULT_SELF_USE_byOBJECTS
    override fun getDataSource(sharedPref: SharedPreferences, dataSource: DATA_SOURCE):ArrayList<ListData>{
        if(dataSource == DATA_SOURCE.FROM_INIT){
            if(myUseCase == USE_CASES.CASE_1_DEFAULT_SELF_USE_byOBJECTS){
                return getInitData_byObject(sharedPref)
            } else if(myUseCase == USE_CASES.CASE_2_BOLT_PREVENTION_byOBJECTS){
                return getInitData_CASE2_BP_byObject(sharedPref)
            } else {
                return getInitData_byObject(sharedPref)
            }
        } else if(dataSource == DATA_SOURCE.FROM_POST){
            return getPostData()
        } else{
            return ArrayList<ListData>()
        }
    }

    public enum class DAYS_OF_WEEK(val n: Int){
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

    // 自用


    override fun getInitData_byObject(sharedPref: SharedPreferences):ArrayList<ListData> {

        var rawListData:ArrayList<ListData> = ArrayList<ListData>()

        val default_date = "2026/10/01"
        val default_image = R.drawable.pancake
        val default_showRoutine = true
        val default_isFinish = false
        val default_timeDesc = ""

        rawListData.add(
            ListData(
                1, "洗澡",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.EVERYDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )

        rawListData.add(
            ListData(
                2, "刮鬍子",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.WEEKDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )

        rawListData.add(
            ListData(
                3, "洗衣服",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.WEEKEND.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )

        rawListData.add(
            ListData(
                4, "擦床頭/抖枕頭",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.WEEKEND.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )

        rawListData.add(
            ListData(
                5, "喝牛奶",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.TUESDAY.n,
                    DAYS_OF_WEEK.FRIDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )

        rawListData.add(
            ListData(
                6,  "改善居家環境",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.MONDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )
        rawListData.add(
            ListData(
                7, "為周末預備體力",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.THURSDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )
        rawListData.add(
            ListData(
                8, "掃地拖地_嗎",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.FRIDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )
        rawListData.add(
            ListData(
                9, "修指甲_嗎",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.FRIDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )
        rawListData.add(
            ListData(
                10, "祭祀兄弟_可",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.WEDNESDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )
        rawListData.add(
            ListData(
                11, "月度定期事項",
                default_date, getString(R.string.myMonthlyRoutineDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.WEEKEND.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )
        rawListData.add(
            ListData(
                12, "每日固定事項",
                default_date, getString(R.string.myDailyRoutineDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.EVERYDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )
        rawListData.add(
            ListData(
                13, "給熱水壺加水",
                default_date, getString(R.string.myKettleDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.WEDNESDAY.n,
                    DAYS_OF_WEEK.SATURDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )
        rawListData.add(
            ListData(
                14, "護眼/吃葉黃素凍",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.WEEKDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )
        // Empty Routines
        rawListData.add(
            ListData(
                15, "",
                "", getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.NONE.n),
                false ,false,
                default_timeDesc)
        )
        rawListData.add(
            ListData(
                16, "",
                "", getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.NONE.n),
                false ,false,
                default_timeDesc)
        )
        rawListData.add(
            ListData(
                17, "",
                "", getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.NONE.n),
                false ,false,
                default_timeDesc)
        )
        rawListData.add(
            ListData(
                18, "",
                "", getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.NONE.n),
                false ,false,
                default_timeDesc)
        )
        rawListData.add(
            ListData(
                19, "",
                "", getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.NONE.n),
                false ,false,
                default_timeDesc)
        )
        rawListData.add(
            ListData(
                20, "",
                "", getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.NONE.n),
                false ,false,
                default_timeDesc)
        )

        /*
        val data = ListData(
            16, "TEST",
            default_date, getString(R.string.noDesc),
            default_image, intArrayOf(DAYS_OF_WEEK.EVERYDAY.n),
            default_showRoutine ,default_isFinish,
            default_timeDesc)
        val jsonString = Json.encodeToString(data)
        rawListData.add(Json.decodeFromString<ListData>(jsonString))
        */


        return refineSourceObjects(rawListData)
    }

    // 因為住家或公司附近有大型施工地而造成靜電漏電。
    // 而需要定期改善環境和補充營養，以維持身體健康。
    fun getInitData_CASE2_BP_byObject(sharedPref: SharedPreferences):ArrayList<ListData> {
        var rawListData:ArrayList<ListData> = ArrayList<ListData>()

        val default_date = "2026/10/01"
        val default_image = R.drawable.pancake
        val default_showRoutine = true
        val default_isFinish = false
        val default_timeDesc = ""

        rawListData.add(
            ListData(
                1, "改善辦公環境",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.MONDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )

        rawListData.add(
            ListData(
                2, "吃葉黃素凍(眼)",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.WEEKDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )

        rawListData.add(
            ListData(
                3, "吃護膝食物(膝)",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.WEEKDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )

        rawListData.add(
            ListData(
                4, "吃高鈣食物(牙)",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.WEEKDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )

        rawListData.add(
            ListData(
                5, "吃高麗菜(腸胃)",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.WEEKDAY.n,
                    DAYS_OF_WEEK.FRIDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )

        rawListData.add(
            ListData(
                6,  "是否買魚油(心腦)",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.WEEKEND.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )
        rawListData.add(
            ListData(
                7, "月度中醫調理(腎和膀胱)",
                default_date, getString(R.string.noDesc),
                default_image, intArrayOf(DAYS_OF_WEEK.TUESDAY.n, DAYS_OF_WEEK.FRIDAY.n),
                default_showRoutine ,default_isFinish,
                default_timeDesc)
        )

        return refineSourceObjects(rawListData)


    }

    override fun refineSourceObjects(source: ArrayList<ListData> ): ArrayList<ListData>{
        for(i in source.indices) {
            if (DAYS_OF_WEEK.WEEKDAY.n in source[i].routineDaysOfWeek) {
                source[i].image = R.drawable.pizza
            }
            if (DAYS_OF_WEEK.WEEKEND.n in source[i].routineDaysOfWeek) {
                source[i].image = R.drawable.burger
            }
            if (source[i].orderNo == 7) { // 7 for 月度中醫調理(腎和膀胱)
                source[i].image = R.drawable.fries
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
                if (DAYS_OF_WEEK.WEEKDAY.n in source[i].routineDaysOfWeek && tempDay == DAYS_OF_WEEK.WEEKDAY.n) {
                    source[i].showRoutine = true
                }
                if (DAYS_OF_WEEK.WEEKEND.n in source[i].routineDaysOfWeek && tempDay == DAYS_OF_WEEK.WEEKEND.n) {
                    source[i].showRoutine = true
                }
            }
        }

        for (i in source.indices) {
            if((DAYS_OF_WEEK.WEEKDAY.n in source[i].routineDaysOfWeek) && (source[i].time in thisWeek)){
                source[i].isFinish = true
            } else if((DAYS_OF_WEEK.WEEKEND.n in source[i].routineDaysOfWeek) && (source[i].time in thisWeek)){
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

    /* override fun getShowRoutine(listData:ArrayList<ListData>):ArrayList<Boolean>{
        var myArray = ArrayList<Boolean>()
        for(i in listData.indices){
            myArray.add(listData[i].showRoutine)
        }

        return myArray
    }

     */
    /* override fun getTimeList(listData:ArrayList<ListData>):ArrayList<String>{
        var myArray = ArrayList<String>()
        for(i in listData.indices){
            myArray.add(listData[i].time)
        }

        return myArray
    }

     */


    var mMyInitData:ArrayList<ListData> = ArrayList<ListData>()
    lateinit var mLastTimesList: ArrayList<String>
    lateinit var mListViewMapping: IntArray

    var mIsShowAll: Boolean = false
    var mIsShowEmpty: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mCurrentDate = LocalDate.now().format(myFormatter)
        SecretNumber.init(this)
        RoutineDataManager.init(this)

        val sharedPref = getRoutineSharePreference()

        // TODO: 資料來源可擴充。
        //       改成可以同時相容於1.內建資料 2.遠端資料庫撈取
        // TODO: 可以一次顯示所有事項的最後完成日期。
        //      方便手動更新程式碼內建資料的最後完成日期。
        //      (要安裝在不同手機時會用到。)
        updateListviewToToday(sharedPref)

        binding.ivRefreshToToday.setOnClickListener {
            hideRefreshBtn()
            updateListviewToToday(sharedPref, mIsShowAll, mIsShowEmpty)
        }

        binding.swSelectAll.setOnCheckedChangeListener { buttonView, isChecked ->
                mIsShowAll = isChecked
                updateListviewToToday(sharedPref, mIsShowAll, mIsShowEmpty)
                if(isChecked){
                    binding.tvToday.setText("本週")
                    binding.tvToday.setTextColor(getColor(R.color.purple_500))
                    binding.cbShowEmpty.visibility = View.VISIBLE
                } else {
                    binding.tvToday.setText("今天")
                    binding.tvToday.setTextColor(getColor(R.color.black))
                    binding.cbShowEmpty.visibility = View.INVISIBLE
                }
        }

        binding.cbShowEmpty.setOnCheckedChangeListener { buttonView, isChecked ->
            mIsShowEmpty = isChecked
            updateListviewToToday(sharedPref, mIsShowAll, mIsShowEmpty)
        }
    }

    fun updateListviewToToday(sharedPref: SharedPreferences){
        initData(sharedPref)
        initAdapter(sharedPref, false, false)
    }
    fun updateListviewToToday(sharedPref: SharedPreferences, showAll: Boolean, showEmpty: Boolean ){
        initData(sharedPref)
        initAdapter(sharedPref, showAll, showEmpty)
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

            Log.d("Peter Test", "PUTSTRING:" + jsonString)

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

        Log.d("Peter Test", "GETSTRING:" + savedJson)
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

            val storedListData:ListData = RoutineDataManager.getRoutineData(orderNo)
            if(storedListData.name.isEmpty()){
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

    override fun initAdapter(sharedPref: SharedPreferences, showAll:Boolean, showEmpty:Boolean){
        val today: LocalDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val formattedDate = today.format(formatter)

        val showRoutine = getShowRoutine(mMyInitData)

        mListViewMapping = IntArray(mMyInitData.size){it + 1}

        dataArrayList.clear()

        var j = 0;
        for (i in mLastTimesList.indices) {
            val isEmpty = isItemEmpty(mMyInitData[i])
            val shouldShow = (showAll && !isEmpty) || (showAll && showEmpty && isEmpty)
            if(showRoutine[i] || shouldShow) {
                mListViewMapping[j] = i
                dataArrayList.add(mMyInitData[i])
                j++
            }
        }
        listAdapter = ListAdapter(this@MainActivity, dataArrayList)
        binding.listview.adapter = listAdapter
        binding.listview.isClickable = true
        binding.listview.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->

            val realNo = mListViewMapping[i]
            val data = mMyInitData.get(realNo)

            if(data.name == "月度定期事項"){
                val intent = Intent(this@MainActivity, MonthlyActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@MainActivity, DetailedActivity::class.java)

                intent.putExtra("orderNo", data.orderNo)
                intent.putExtra("name", data.name)
                intent.putExtra("time", data.time)
                intent.putExtra("desc", data.desc)
                intent.putExtra("image", data.image)
                intent.putExtra("isfinish", data.isFinish)
                intent.putExtra("routineDaysOfWeek", data.routineDaysOfWeek)
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
                    tempData.time = formattedDate
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

    private val editItemLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {
                Log.d("Peter Test:", "MainActivity Recieved RESULT_OK")
                val orderNo = result.data?.getIntExtra("orderNo", 0)
                Log.d("Peter Test:", "MainActivity Recieved RESULT_OK:orderNo=" + orderNo)

                if(orderNo != 0) {
                    Log.d("Peter Test:", "MainActivity Recieved RESULT_OK:updateListviewToToday()")
                    updateListviewToToday(getRoutineSharePreference(),mIsShowAll, mIsShowEmpty)
                }
            }
        }

    /* override fun isItemEmpty(listData: ListData):Boolean {
        var isEmpty: Boolean = false
        if(listData.name.isEmpty() && (DAYS_OF_WEEK.NONE.n in listData.routineDaysOfWeek)){
            isEmpty = true
        }
        return isEmpty
    }

     */

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
        binding.ivRefreshToToday.visibility = View.GONE
    }

    fun getRoutineSharePreference(): SharedPreferences{
        return getSharedPreferences(
            getString(R.string.preference_key), Context.MODE_PRIVATE)
    }

}

