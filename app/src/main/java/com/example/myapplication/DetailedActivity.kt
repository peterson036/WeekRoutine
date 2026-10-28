package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDetailedBinding
import com.example.myapplication.module.RoutineDataManager

class DetailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedBinding

    private lateinit var mListData:ListData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mListData = ListData()

        val intent = this.intent
        if (intent != null) {
            mListData.name = intent.getStringExtra("name") ?: ""
            mListData.time = intent.getStringExtra("time") ?: ""
            mListData.desc = intent.getStringExtra("desc") ?: ""
            mListData.image = intent.getIntExtra("image", R.drawable.pancake)
            mListData.orderNo = intent.getIntExtra("orderNo", 0)
            mListData.isFinish = intent.getBooleanExtra("isfinish", false)
            mListData.showRoutine = intent.getBooleanExtra("showRoutine", false)
            mListData.routineDaysOfWeek = intent.getIntArrayExtra("routineDaysOfWeek") ?: intArrayOf(
                MainActivity.DAYS_OF_WEEK.NONE.n)

            refreshView()

            binding.btBack.setOnClickListener {
                if(mIsEdited) {
                    Log.d("Peter Test", "Detailed Activity return Edited Status to MainActivity")
                    val intent = Intent().apply {
                        putExtra("orderNo", mListData.orderNo)
                    }
                    Log.d("Peter Test", "Detailed Activity: orderNo=" + intent.getIntExtra("orderNo", 0).toString())
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    finish()
                }
            }

            binding.btEdit.setOnClickListener {
                val intent = Intent(this@DetailedActivity, EditRoutineActivity::class.java)

                intent.putExtra("orderNo", mListData.orderNo)
                intent.putExtra("name", mListData.name)
                intent.putExtra("time", mListData.time)
                intent.putExtra("desc", mListData.desc)
                intent.putExtra("image", mListData.image)
                intent.putExtra("isfinish", mListData.isFinish)
                intent.putExtra("showRoutine", mListData.showRoutine)
                intent.putExtra("routineDaysOfWeek", mListData.routineDaysOfWeek)
                editItemLauncher.launch(intent)
            }
        }


    }

    private var mIsEdited:Boolean = false
    private val editItemLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {
                // 更新資料
                updateDataAfterEdit()
                mIsEdited = true
            }
        }
    fun updateDataAfterEdit(){
        mListData = RoutineDataManager.getRoutineData(mListData.orderNo)
        refreshView()
    }
    fun refreshView(){
        binding.detailName.text = mListData.name
        binding.detailTime.text = mListData.time
        binding.detailDesc.text = mListData.desc
        binding.detailImage.setImageResource(mListData.image)
    }
}