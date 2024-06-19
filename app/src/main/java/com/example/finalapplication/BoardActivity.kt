package com.example.finalapplication

import com.google.firebase.firestore.Query
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalapplication.databinding.ActivityBoardBinding
import com.google.api.Distribution.BucketOptions.Linear

class BoardActivity : AppCompatActivity() {
    lateinit var binding : ActivityBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.tool_bar))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        //comments 버튼 클릭시
        binding.mainFab.setOnClickListener {
            if(MyApplication.checkAuth()){
                startActivity(Intent(this, AddActivity::class.java))
            } else {
                Toast.makeText(this, "로그인을 진행해주세요", Toast.LENGTH_LONG).show()
            }
        }
    }

    //firebase database 가지고 와서 연결
    override fun onStart() {
        super.onStart()

        if(MyApplication.checkAuth()){
            MyApplication.db.collection("comments")
                .orderBy("date_time", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    result ->
                    val itemList = mutableListOf<ItemData>()
                    for(document in result){
                        val item = document.toObject(ItemData::class.java)
                        item.docId = document.id
                        itemList.add(item)
                    }
                    binding.recyclerView.layoutManager = LinearLayoutManager(this)
                    binding.recyclerView.adapter = BoardAdapter(this, itemList)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "서버 데이터 획득 실패", Toast.LENGTH_LONG).show()
                }
        }

    }
}