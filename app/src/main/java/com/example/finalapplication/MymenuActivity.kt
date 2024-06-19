package com.example.finalapplication

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalapplication.databinding.ActivityMymenuBinding
import com.google.api.Distribution.BucketOptions.Linear
import java.io.BufferedReader
import java.io.File
import java.text.SimpleDateFormat

class MymenuActivity : AppCompatActivity() {
    lateinit var binding : ActivityMymenuBinding
    var datas: MutableList<String>? = null
    lateinit var adapter: MyAdapter
    lateinit var sharedPreferences: SharedPreferences //설정값 통해 바꾸기 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMymenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //파일 읽기
        val file = File(filesDir, "mymenu.txt")
        val readstream : BufferedReader = file.reader().buffered()
        binding.lastsaved.text = "마지막 저장시간: " + readstream.readLine()

        adapter = MyAdapter(datas)
        binding.recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager=layoutManager
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        datas = mutableListOf<String>()
        val db = DBHelper(this).readableDatabase
        val cursor = db.rawQuery("select * from menu_tb", null)
        while (cursor.moveToNext()){
            datas?.add(cursor.getString(1))
        }
        db.close()

        val requestLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            it.data!!.getStringExtra("result")?.let{
                if(it !=""){
                    datas?.add(it)
                    adapter.notifyDataSetChanged()

                }
            }
        }

        binding.mainFab.setOnClickListener {
            val intent = Intent(this, AddmymenuActivity::class.java) //AddActivity 불러옴

            val dateFormat = SimpleDateFormat("yyyy-MM-dd") // 년 월 일
            intent.putExtra("today", dateFormat.format(System.currentTimeMillis())) // 현재 시간에 대한 dateformat

            requestLauncher.launch(intent) //requestLauncher 호출(결과를 전달 받고 처리해준 결과 호출)
        }
    }

    // sharedpreferences 통해 설정한 값으로 바꾸기
    override fun onResume() {
        super.onResume()
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val color = sharedPreferences.getString("color", "#00ff00")
        binding.lastsaved.setBackgroundColor(Color.parseColor(color))

        val size = sharedPreferences.getString("textsize", "16.0f")
        binding.lastsaved.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size!!.toFloat())
    }
}