package com.example.finalapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalapplication.databinding.ItemMainBinding

class JsonViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)
class JsonAdapter(val context:Context, val datas:MutableList<myJsonItems>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return JsonViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as JsonViewHolder).binding
        val model = datas!![position]

        holder.binding.run {

        binding.name.text = model.MEAL_NM
        binding.issue.text = model.MEAL_CLSF_NM
        binding.recipe.text = model.COOK_MTH_CONT
        binding.material.text = model.MATRL_NM

        Glide.with(binding.root)
            .load(model.MEAL_PICTR_FILE_NM)
            .override(40,30) //이미지 크기 변경
            .into(binding.imageView)

        root.setOnClickListener {
            Intent(context, MainFoodActivity::class.java).apply {
                putExtra("name", name.text)
                putExtra("issue", issue.text)
                putExtra("recipe", recipe.text)
                putExtra("material", material.text)
            }.run {
                context.startActivity(this)
            }
        }
        }

    }
}