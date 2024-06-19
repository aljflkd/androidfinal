package com.example.finalapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalapplication.databinding.ActivityMainBinding
import com.example.finalapplication.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //검색하기
        binding.btnSearch.setOnClickListener {
            val name = binding.edtFood.text.toString()
            Log.d("mobileApp", name)

            val call: Call<JsonResponse> = RetrofitConnection.jsonNetServ.getJsonList(
                "fr7mR1A87wi02jn21hbpXMOSybF8Xc+Wtmdi+xOVgyil4bLf/Z3AaKmjhRz22VIB3Ewyk3U+ZlqcaP+EmVfJ2Q==",
                1,
                10,
                "json",
                name
            )

            call?.enqueue(object : Callback<JsonResponse>{
                override fun onResponse( call: Call<JsonResponse>, response: Response<JsonResponse>) {
                    if(response.isSuccessful){
                        Log.d("mobileApp", "$response")
                        Log.d("mobileApp", "${response.body()}")
                        binding.jsonRecyclerView.adapter = JsonAdapter(this@SearchActivity, response.body()?.body!!.items)
                        binding.jsonRecyclerView.layoutManager=LinearLayoutManager(this@SearchActivity)
                        binding.jsonRecyclerView.addItemDecoration(DividerItemDecoration(this@SearchActivity, LinearLayoutManager.VERTICAL))

                    }
                }

                override fun onFailure(call: Call<JsonResponse>, t: Throwable) {
                    Log.d("mobileApp", "onFaillure")
                }

            })

        }


    }
}