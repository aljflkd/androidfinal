package com.example.finalapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 네트워크 서비스와 직접 연결 시켜주는 부분
//https://apis.data.go.kr/1471000/ChldhMnuInfoService/
class RetrofitConnection {
   companion object{
       private const val BASE_URL = "https://apis.data.go.kr/1471000/ChldhMnuInfoService/"

       val jsonNetServ : NetworkService
       val jsonRetrofit : Retrofit
           get() = Retrofit.Builder()
               .baseUrl(BASE_URL)
               .addConverterFactory(GsonConverterFactory.create())
               .build()


       init {
           jsonNetServ = jsonRetrofit.create(NetworkService::class.java)
       }


   }


}