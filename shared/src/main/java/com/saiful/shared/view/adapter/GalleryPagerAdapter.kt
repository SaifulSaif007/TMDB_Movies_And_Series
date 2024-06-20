package com.saiful.shared.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saiful.shared.databinding.LayoutGalleryItemBinding
import com.saiful.shared.model.Image
import com.saiful.shared.utils.loadBackDropSizeImage

class GalleryPagerAdapter(private val items: List<Image>) :
    RecyclerView.Adapter<GalleryPagerAdapter.PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding = LayoutGalleryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PagerViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item.filePath)
    }

    inner class PagerViewHolder(private val binding: LayoutGalleryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.personImage.loadBackDropSizeImage(item)
        }
    }
}