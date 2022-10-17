package com.saiful.tvshows.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.saiful.shared.utils.AppConstants.imageBaseUrl
import com.saiful.shared.utils.AppConstants.posterSize
import com.saiful.tvshows.databinding.LayoutShowItemBinding
import com.saiful.shared.model.TvShows
import com.saiful.shared.utils.loadPosterSizeImage

class ShowsDashboardAdapter (private val listener: (Int) -> Unit):
    RecyclerView.Adapter<ShowsDashboardAdapter.MovieDashboardViewHolder>() {

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDashboardViewHolder {
        val binding = LayoutShowItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MovieDashboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieDashboardViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<TvShows>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    inner class MovieDashboardViewHolder(private val binding: LayoutShowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = differ.currentList[position]
                    listener.invoke(item.id)
                }
            }
        }
        fun bind(item: TvShows) {
            binding.posterImage.loadPosterSizeImage(item.posterPath)
        }
    }

    private companion object {
        @SuppressLint("DiffUtilEquals")
        private val diffCallback = object : DiffUtil.ItemCallback<TvShows>() {
            override fun areItemsTheSame(oldItem: TvShows, newItem: TvShows): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TvShows, newItem: TvShows): Boolean {
                return oldItem == newItem
            }
        }
    }
}