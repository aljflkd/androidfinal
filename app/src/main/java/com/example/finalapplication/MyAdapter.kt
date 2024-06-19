package com.example.finalapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapplication.databinding.ItemRecyclerviewBinding

class MyViewHolder(val binding: ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)

//우리가 받는 datas가 null일 수 있음 MutableList<String>?
class MyAdapter(val datas: MutableList<String>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){ // 2-1
    override fun getItemCount(): Int {
        //datas 뒤에 ? 붙여줘야함 ?:(값이 null인 경우에는)
        return datas?.size ?:0;     // 2-2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        //datas가 null인 경우에는 연결할 필요X 그러므로 datas가 null이 아닌 경우에만 바인딩하겠다 !!
        binding.itemData.text = datas!![position]      // 2-3
    }
}
