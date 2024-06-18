package com.dicoding.wearshare.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.wearshare.data.response.PantiResponseItem
import com.dicoding.wearshare.databinding.ItemPantiBinding

class PantiAdapter(private val listener: RecyclerViewClickListener) : RecyclerView.Adapter<PantiAdapter.MyViewHolder>() {

    private var items: List<PantiResponseItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPantiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener.onItemClick(it, item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(newItems: List<PantiResponseItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    class MyViewHolder(private val binding: ItemPantiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PantiResponseItem) {
            binding.name.text = item.namaPanti
            binding.location.text = item.lokasi
            Glide.with(binding.root.context)
                .load(item.gambar)
                .into(binding.imgAvatar)
        }
    }

    interface RecyclerViewClickListener {
        fun onItemClick(view: View, item: PantiResponseItem)
    }
}
