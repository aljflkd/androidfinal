package com.example.finalapplication

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

//Dex : Dolvic Executable (최대 64K까지 가능)
//가입정보
class MyApplication : MultiDexApplication() {
    companion object{
        lateinit var auth: FirebaseAuth
        var email:String? = null
        lateinit var db : FirebaseFirestore
        lateinit var storage : FirebaseStorage

        fun checkAuth(): Boolean{
            var currentUser = auth.currentUser
            if(currentUser !=null){
                email = currentUser.email
                return currentUser.isEmailVerified
            }
            return false
        }
    }

    override fun onCreate(){
        super.onCreate()
        // 초기화
        auth = Firebase.auth //로그인
        db = FirebaseFirestore.getInstance() //firebase
        storage = Firebase.storage
    }
}