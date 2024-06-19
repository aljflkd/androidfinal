package com.example.finalapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//https://apis.data.go.kr/1471000/ChldhMnuInfoService/getChldhMnuInfoService?serviceKey=fr7mR1A87wi02jn21hbpXMOSybF8Xc%2BWtmdi%2BxOVgyil4bLf%2FZ3AaKmjhRz22VIB3Ewyk3U%2BZlqcaP%2BEmVfJ2Q%3D%3D&pageNo=1&numOfRows=1&type=json&MEAL_NM=%EA%B0%90%EC%9E%90%EB%AF%B8%EC%9D%8C
//?serviceKey=fr7mR1A87wi02jn21hbpXMOSybF8Xc%2BWtmdi%2BxOVgyil4bLf%2FZ3AaKmjhRz22VIB3Ewyk3U%2BZlqcaP%2BEmVfJ2Q%3D%3D
// &pageNo=1
// &numOfRows=1
// &type=json
// &MEAL_NM=%EA%B0%90%EC%9E%90%EB%AF%B8%EC%9D%8C
interface NetworkService {
    @GET("getChldhMnuInfoService")
    fun getJsonList(
        @Query("serviceKey") serviceKey:String,
        @Query("pageNo") pageNo:Int,
        @Query("numOfRows") numOfRows:Int,
        @Query("type") returnType:String,
        @Query("MEAL_NM") mealName:String
    ) : Call<JsonResponse>
}