package com.example.finalapplication

data class myJsonItems(val MEAL_CLSF_NM:String, val MEAL_NM:String, val COOK_MTH_CONT:String, val MATRL_NM:String,
    val CALORIE_QY:String, val CARBOH_QY:String
    ,val MEAL_PICTR_FILE_NM:String)
data class myJsonBody(val items: MutableList<myJsonItems>)
data class JsonResponse(val body: myJsonBody)

