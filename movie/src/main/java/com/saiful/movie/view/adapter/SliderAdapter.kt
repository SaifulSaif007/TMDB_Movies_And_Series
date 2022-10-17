package com.saiful.movie.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.saiful.movie.databinding.LayoutImageSliderBinding
import com.saiful.shared.model.Movies
import com.saiful.shared.utils.loadBackDropSizeImage

class SliderAdapter(
    private val imageList: MutableList<Movies>,
    private val viewPager2: ViewPager2,
    private val listener: (Int) -> Unit
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

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = imageList[position]
                    listener.invoke(item.id)
                }
            }
        }

        fun bind(item: Movies) {
            binding.apply {
                movieName.text = item.title
                posterImage.loadBackDropSizeImage(item.backdropPath)
            }
        }
    }

}