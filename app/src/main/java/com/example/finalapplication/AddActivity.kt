package com.example.finalapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.finalapplication.databinding.ActivityAddBinding
import java.text.SimpleDateFormat

class AddActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddBinding
    lateinit var uri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvId.text = MyApplication.email
        binding.saveButton.setOnClickListener {
            if(binding.input.text.isNotEmpty()){
                //로그인 이메일, 스타, 한줄평, 입력 시간
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                val data = mapOf(
                    "title" to binding.tvTitle.text.toString(),
                    "email" to MyApplication.email,
                    "materials" to binding.input.text.toString(),
                    "recipe" to binding.inputrec.text.toString(),
                    "date_time" to dateFormat.format(System.currentTimeMillis())
                )
                MyApplication.db.collection("comments")
                    .add(data)
                    .addOnSuccessListener {
                        Toast.makeText(this,"게시물을 올렸습니다.", Toast.LENGTH_LONG).show()
                        uploadImage(it.id)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this,"게시물을 올리지 못했습니다.", Toast.LENGTH_LONG).show()
                        finish()
                    }
            }
            else {
                Toast.makeText(this, "한줄평 입력해주세요", Toast.LENGTH_LONG).show()
            }
        }

        // 이미지 데이터 가져옴
        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == android.app.Activity.RESULT_OK){ //이미지 잘 가져왔으면
                binding.addImageView.visibility = View.VISIBLE
                Glide
                    .with(applicationContext)
                    .load(it.data?.data)
                    .override(200,150)
                    .into(binding.addImageView)
                uri = it.data?.data!!
            }
        }
        binding.uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            requestLauncher.launch(intent)
        }
    }

    //storage와 firestore 연결
    fun uploadImage(docId : String){
        val imageRef = MyApplication.storage.reference.child("images/${docId}.jpg")

        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            Toast.makeText(this, "사진 업로드 성공", Toast.LENGTH_LONG).show()
        }
        uploadTask.addOnFailureListener {
            Toast.makeText(this, "사진 업로드 실패", Toast.LENGTH_LONG).show()
        }
    }
}