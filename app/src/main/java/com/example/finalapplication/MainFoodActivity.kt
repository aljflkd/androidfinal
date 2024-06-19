package com.example.finalapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalapplication.databinding.ActivityMainFoodBinding

class MainFoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvName.text = intent.getStringExtra("name")
        binding.tvAge.text = intent.getStringExtra("issue")
        binding.tvRecipe.text = intent.getStringExtra("recipe")
        binding.tvMate.text = intent.getStringExtra("material")
    }
}