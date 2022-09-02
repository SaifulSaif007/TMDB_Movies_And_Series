package com.saiful.tvshows.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.saiful.shared.utils.AppConstants.backdropSize
import com.saiful.shared.utils.AppConstants.imageBaseUrl
import com.saiful.tvshows.databinding.LayoutShowsImageSliderBinding
import com.saiful.tvshows.model.TvShows

class ShowsSliderAdapter(
    private val imageList: MutableList<TvShows>,
    private val viewPager2: ViewPager2,
    private val listener: (Int) -> Unit
) : RecyclerView.Adapter<ShowsSliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = LayoutShowsImageSliderBinding.inflate(
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

    inner class SliderViewHolder(private val binding: LayoutShowsImageSliderBinding) :
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

        fun bind(item: TvShows) {
            Glide.with(itemView).load(imageBaseUrl + backdropSize + item.backdropPath)
                .into(binding.posterImage)

            binding.apply {
                showsName.text = item.name
            }
        }
    }

}