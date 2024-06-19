package com.example.finalapplication

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalapplication.MyApplication.Companion.db
import com.example.finalapplication.databinding.ActivityAddmymenuBinding
import java.io.File
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat

class AddmymenuActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddmymenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddmymenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

            var date = intent.getStringExtra("today") //date라는 변수: 나를 부른 intent에서 가져옴(getStringExtra: 스트링형태로 가져오겠다)
            binding.date.text = date //binding의 date에 text를 date로

            supportActionBar?.setDisplayHomeAsUpEnabled(true) //액션바에 표현하겠다

            binding.btnSave.setOnClickListener {
                val edt_str = binding.addEditView.text.toString()
                val intent = intent //변수
                intent.putExtra("result", edt_str) //값을 넣어줌(result변수에,사용자가 입력한 텍스트를 전달)
                setResult(Activity.RESULT_OK, intent) //자신을 불렀던 곳으로 다시 되돌아감

                //db에 저장하기
                val db1 = DBHelper(this).writableDatabase
                db1.execSQL("insert into menu_tb (menu) values (?)", arrayOf<String>(edt_str))
                db1.close()

                // 파일 저장하기
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                val file = File(filesDir, "mymenu.txt")
                val writeStream: OutputStreamWriter = file.writer()
                writeStream.write(dateFormat.format(System.currentTimeMillis())) //내용쓰기
                writeStream.flush() //파일에 추가

                finish()
                true


            }
        }

        override fun onSupportNavigateUp(): Boolean {
            val intent = intent
            setResult(Activity.RESULT_OK, intent)

            finish()
            return true
        }
}