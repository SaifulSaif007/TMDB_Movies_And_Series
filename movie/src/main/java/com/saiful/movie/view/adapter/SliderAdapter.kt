package com.saiful.movie.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.saiful.movie.databinding.LayoutImageSliderBinding
import com.saiful.movie.model.ImageSliderItem

class SliderAdapter(
    private val imageList: ArrayList<ImageSliderItem>,
    private val viewPager2: ViewPager2
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = LayoutImageSliderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val item = imageList[position]
        holder.bind(item)
        if (position == imageList.size - 2) {
            viewPager2.post(runnable)
        }
    }

    private val runnable = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = imageList.size

    inner class SliderViewHolder(private val binding: LayoutImageSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageSliderItem) {
            Glide.with(itemView).load(item.imageUrl).into(binding.posterImage)
        }
    }

}