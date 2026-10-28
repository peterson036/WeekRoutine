package com.example.myapplication.monthly

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.myapplication.EditRoutineActivity
import com.example.myapplication.ListData
import com.example.myapplication.MainActivity.DAYS_OF_WEEK
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityEditRoutineBinding
import com.example.myapplication.module.RoutineDataManager
import com.example.myapplication.module.RoutineListHandler


class MonthlyEditRoutineActivity : AppCompatActivity() {
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

    enum class LONG_PERIOD(val text:String){
        ORIGINAL("原方案"),
        DAYS_30("30"),
        DAYS_40("40"),
        DAYS_60("60"),
        DAYS_90("90"),
        DAYS_180("180"),
        DAYS_360("360")
    }
    val options = arrayOf(
        LONG_PERIOD.ORIGINAL.text,
        LONG_PERIOD.DAYS_30.text,
        LONG_PERIOD.DAYS_40.text,
        LONG_PERIOD.DAYS_60.text,
        LONG_PERIOD.DAYS_90.text,
        LONG_PERIOD.DAYS_180.text,
        LONG_PERIOD.DAYS_360.text
    )

    lateinit var mListData: ListData
    lateinit var mLongPeriodOriginal:String

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
            val timeDesc = intent.getStringExtra("timeDesc")
            println("Peter Test:timeDesc in MonthlyEditRoutinActivity:" + timeDesc)

            initListData(orderNo, name, time, desc, image, timeDesc)

            binding.editName.setText(name)
            binding.tvTime.text = time
            binding.editDesc.setText(desc)
            binding.detailImage.setImageResource(image)
            binding.tvDaysOfWeek.text = "每天"

            binding.btStoreAndBack.setOnClickListener {
                // 1. 儲存資料
                // 將ListData儲存到SharePreference
                RoutineDataManager.storeMonthlyRoutineData(mListData)

                // 2. 關閉目前 Activity，回上一頁
                setResult(RESULT_OK)
                finish()
            }

            enableEditTextAndSpinner()
            // notEnableEditFunction()
        }
    }

    fun initListData(orderNo: Int, name:String?, time:String?, desc:String?, image: Int, timeDesc:String?){
        mListData.orderNo = orderNo
        mListData.name = name ?: ""
        mListData.time = time ?: ""
        mListData.desc = desc ?: ""
        mListData.image = image
        mListData.timeDesc = timeDesc ?: "30"

        // 設定為預設值
        mListData.showRoutine = false
        mListData.isFinish = false
        mListData.image = R.drawable.pancake
        mListData.routineDaysOfWeek = IntArray(DAYS_OF_WEEK.EVERYDAY.n)


        mLongPeriodOriginal = mListData.timeDesc
    }

    fun enableEditTextAndSpinner(){
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

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            options
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spLongPeriod.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // 使用者選到的文字
                    val selectedItem = parent?.getItemAtPosition(position).toString()

                    if(selectedItem == LONG_PERIOD.ORIGINAL.text){
                        mListData.timeDesc = mLongPeriodOriginal
                    } else if(selectedItem == LONG_PERIOD.DAYS_30.text){
                        mListData.timeDesc = LONG_PERIOD.DAYS_30.text
                    } else if(selectedItem == LONG_PERIOD.DAYS_40.text){
                        mListData.timeDesc = LONG_PERIOD.DAYS_40.text
                    } else if(selectedItem == LONG_PERIOD.DAYS_60.text){
                        mListData.timeDesc = LONG_PERIOD.DAYS_60.text
                    } else if(selectedItem == LONG_PERIOD.DAYS_90.text){
                        mListData.timeDesc = LONG_PERIOD.DAYS_90.text
                    } else if(selectedItem == LONG_PERIOD.DAYS_180.text){
                        mListData.timeDesc = LONG_PERIOD.DAYS_180.text
                    } else if(selectedItem == LONG_PERIOD.DAYS_360.text){
                        mListData.timeDesc = LONG_PERIOD.DAYS_360.text
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        binding.spLongPeriod.adapter = adapter

        hideAndDisableDaysOfWeek()
    }

    fun hideAndDisableDaysOfWeek(){
        binding.llDaysOfWeekSpinner.visibility = View.GONE
        binding.spPeriod.isEnabled = false

        binding.llDaysOfWeek.visibility = View.GONE
        binding.cbWeekDay1.isEnabled = false
        binding.cbWeekDay2.isEnabled = false
        binding.cbWeekDay3.isEnabled = false
        binding.cbWeekDay4.isEnabled = false
        binding.cbWeekDay5.isEnabled = false
        binding.cbWeekDay6.isEnabled = false
        binding.cbWeekDay7.isEnabled = false
    }

}