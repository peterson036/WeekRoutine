package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.myapplication.MainActivity.DAYS_OF_WEEK
import com.example.myapplication.databinding.ActivityEditRoutineBinding
import com.example.myapplication.module.RoutineDataManager
import com.example.myapplication.module.RoutineListHandler


class EditRoutineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditRoutineBinding

    fun getNamesFromDays(n: Int):String {

        val name = when(n){
            0 -> "NONE(無)"
            1 -> "MONDAY(週一)"
            2 -> "TUESDAY(週二)"
            3 -> "WEDNESDAY(週三)"
            4 -> "THURSDAY(週四)"
            5 -> "FRIDAY(週五)"
            6 -> "SATURDAY(週六)"
            7 -> "SUNDAY(週日)"
            8 -> "WEEKDAY(平日)"
            9 -> "WEEKEND(週末)"
            10 -> "EVERYDAY(每天)"
            else -> "NONE(無)"
        }
        return name
    }

    enum class WEEK_PERIOD(val text:String){
        ORIGINAL("原方案"),
        EVERYDAY("每天"),
        WEEKDAY("平日"),
        WEEKEND("週末"),
        DAYS_OF_WEEK("星期幾")
    }
    val options = arrayOf(
        WEEK_PERIOD.ORIGINAL.text,
        WEEK_PERIOD.EVERYDAY.text,
        WEEK_PERIOD.WEEKDAY.text,
        WEEK_PERIOD.WEEKEND.text,
        WEEK_PERIOD.DAYS_OF_WEEK.text
    )

    lateinit var mListData:ListData
    var mPeriodOriginal:IntArray = IntArray(MainActivity.DAYS_OF_WEEK.NONE.n)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditRoutineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mListData = ListData()
        val intent = this.intent
        if (intent != null) {
            val orderNo = intent.getIntExtra("orderNo", 0)
            val name = intent.getStringExtra("name")
            val time = intent.getStringExtra("time")
            val desc = intent.getStringExtra("desc")
            val image = intent.getIntExtra("image", R.drawable.pancake)
            val routineDaysOfWeek = intent.getIntArrayExtra("routineDaysOfWeek")
            var routines:ArrayList<String>  = ArrayList<String>()
            routineDaysOfWeek?.forEach {
                routines.add(getNamesFromDays(it))
            }

            initListData(orderNo, name, time, desc, image, routineDaysOfWeek,)

            binding.editName.setText(name)
            binding.tvTime.text = time
            binding.editDesc.setText(desc)
            binding.detailImage.setImageResource(image)
            binding.tvDaysOfWeek.text = routines.joinToString(separator = "\n")

            val adapter = ArrayAdapter(
                this,
                R.layout.spinner_item,
                options
            )

            adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )

            binding.spPeriod.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        // 使用者選到的文字
                        val selectedItem = parent?.getItemAtPosition(position).toString()

                        if(selectedItem.equals(WEEK_PERIOD.DAYS_OF_WEEK.text)){
                            binding.llDaysOfWeek.visibility = View.VISIBLE
                        } else {
                            binding.llDaysOfWeek.visibility = View.INVISIBLE
                        }

                        if(selectedItem == WEEK_PERIOD.ORIGINAL.text){
                            mListData.routineDaysOfWeek = mPeriodOriginal
                        } else if(selectedItem == WEEK_PERIOD.EVERYDAY.text){
                            mListData.routineDaysOfWeek = intArrayOf(DAYS_OF_WEEK.EVERYDAY.n)
                        } else if(selectedItem == WEEK_PERIOD.WEEKEND.text){
                            mListData.routineDaysOfWeek = intArrayOf(DAYS_OF_WEEK.WEEKEND.n)
                        } else if(selectedItem == WEEK_PERIOD.WEEKDAY.text){
                            mListData.routineDaysOfWeek = intArrayOf(DAYS_OF_WEEK.WEEKDAY.n)
                        } else if(selectedItem == WEEK_PERIOD.DAYS_OF_WEEK.text){
                            mListData.routineDaysOfWeek = getCheckedDaysOfWeek()
                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

            binding.spPeriod.adapter = adapter

            binding.btStoreAndBack.setOnClickListener {
                // 1. 儲存資料
                // 將ListData儲存到SharePreference
                RoutineDataManager.storeRoutineData(mListData)

                // 2. 關閉目前 Activity，回上一頁
                setResult(RESULT_OK)
                finish()
            }

            enableEditTextAndCheckBoxes()
            // notEnableEditFunction()
        }
    }

    fun initListData(orderNo: Int, name:String?, time:String?, desc:String?, image: Int, routineDaysOfWeek:IntArray?){
        mListData.orderNo = orderNo
        mListData.name = name ?: ""
        mListData.time = time ?: ""
        mListData.desc = desc ?: ""
        mListData.image = image
        mListData.routineDaysOfWeek = routineDaysOfWeek ?: IntArray(MainActivity.DAYS_OF_WEEK.NONE.n)

        // 設定為預設值
        mListData.showRoutine = false
        mListData.isFinish = false
        mListData.image = R.drawable.pancake

        mPeriodOriginal = mListData.routineDaysOfWeek
    }

    fun enableEditTextAndCheckBoxes(){
        binding.editName.doAfterTextChanged { text ->
            mListData.name = text.toString()
        }
        binding.editDesc.doAfterTextChanged { text ->
            mListData.desc = text.toString()
        }

        binding.editDesc.setOnFocusChangeListener { view, hasFocus ->
            val softKeyboardMargin = 1000
            if (hasFocus) {
                binding.svPage.smoothScrollBy(0, softKeyboardMargin)
            } else {
                binding.svPage.smoothScrollBy(0, (-1) * softKeyboardMargin)
            }
        }

        setViewsForDaysOfWeek()
        hideViewsForTimeDesc()
    }

    fun getCheckedDaysOfWeek():IntArray {
        var checkedDaysOfWeek = IntArray(0)
        if(binding.cbWeekDay1.isChecked){
            checkedDaysOfWeek.plus(DAYS_OF_WEEK.MONDAY.n)
        }
        if(binding.cbWeekDay2.isChecked){
            checkedDaysOfWeek.plus(DAYS_OF_WEEK.TUESDAY.n)
        }
        if(binding.cbWeekDay1.isChecked){
            checkedDaysOfWeek.plus(DAYS_OF_WEEK.WEDNESDAY.n)
        }
        if(binding.cbWeekDay1.isChecked){
            checkedDaysOfWeek.plus(DAYS_OF_WEEK.THURSDAY.n)
        }
        if(binding.cbWeekDay1.isChecked){
            checkedDaysOfWeek.plus(DAYS_OF_WEEK.FRIDAY.n)
        }
        if(binding.cbWeekDay1.isChecked){
            checkedDaysOfWeek.plus(DAYS_OF_WEEK.SATURDAY.n)
        }
        if(binding.cbWeekDay1.isChecked){
            checkedDaysOfWeek.plus(DAYS_OF_WEEK.SUNDAY.n)
        }
        return checkedDaysOfWeek
    }
    fun setViewsForDaysOfWeek(){
        binding.cbWeekDay1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mListData.routineDaysOfWeek = mListData.routineDaysOfWeek.plus(DAYS_OF_WEEK.MONDAY.n)
            } else {
                mListData.routineDaysOfWeek = mListData.routineDaysOfWeek.filter { it != DAYS_OF_WEEK.MONDAY.n }.toIntArray()
            }
        }
        binding.cbWeekDay2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mListData.routineDaysOfWeek = mListData.routineDaysOfWeek.plus(DAYS_OF_WEEK.TUESDAY.n)
            } else {
                mListData.routineDaysOfWeek = mListData.routineDaysOfWeek.filter { it != DAYS_OF_WEEK.TUESDAY.n }.toIntArray()
            }
        }
        binding.cbWeekDay3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mListData.routineDaysOfWeek = mListData.routineDaysOfWeek.plus(DAYS_OF_WEEK.WEDNESDAY.n)
            } else {
                mListData.routineDaysOfWeek = mListData.routineDaysOfWeek.filter { it != DAYS_OF_WEEK.WEDNESDAY.n }.toIntArray()
            }
        }
        binding.cbWeekDay4.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mListData.routineDaysOfWeek = mListData.routineDaysOfWeek.plus(DAYS_OF_WEEK.THURSDAY.n)
            } else {
                mListData.routineDaysOfWeek = mListData.routineDaysOfWeek.filter { it != DAYS_OF_WEEK.THURSDAY.n }.toIntArray()
            }
        }
        binding.cbWeekDay5.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mListData.routineDaysOfWeek = mListData.routineDaysOfWeek.plus(DAYS_OF_WEEK.FRIDAY.n)
            } else {
                mListData.routineDaysOfWeek = mListData.routineDaysOfWeek.filter { it != DAYS_OF_WEEK.FRIDAY.n }.toIntArray()
            }
        }
        binding.cbWeekDay6.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mListData.routineDaysOfWeek = mListData.routineDaysOfWeek.plus(DAYS_OF_WEEK.SATURDAY.n)
            } else {
                mListData.routineDaysOfWeek = mListData.routineDaysOfWeek.filter { it != DAYS_OF_WEEK.SATURDAY.n }.toIntArray()
            }
        }
        binding.cbWeekDay7.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mListData.routineDaysOfWeek = mListData.routineDaysOfWeek.plus(DAYS_OF_WEEK.SUNDAY.n)
            } else {
                mListData.routineDaysOfWeek = mListData.routineDaysOfWeek.filter { it != DAYS_OF_WEEK.SUNDAY.n }.toIntArray()
            }
        }
    }

    fun hideViewsForTimeDesc(){
        binding.llTimeDescSpinner.visibility = View.GONE
    }

}