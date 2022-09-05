package com.saiful.tvshows.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saiful.shared.utils.AppConstants
import com.saiful.tvshows.databinding.LayoutShowsListItemBinding
import com.saiful.tvshows.model.TvShows

class ShowsListLoadAdapter(private val listener: (Int) -> Unit) :
    PagingDataAdapter<TvShows, ShowsListLoadAdapter.MovieViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            LayoutShowsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }


    inner class MovieViewHolder(private val binding: LayoutShowsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) listener.invoke(item.id)
                }
            }
        }

        fun bind(shows: TvShows) {
            binding.apply {
                Glide.with(itemView)
                    .load(AppConstants.imageBaseUrl + AppConstants.backdropSize + shows.posterPath)
                    .into(binding.posterImage)

                showsTitle.text = shows.name
                showsOverview.text = shows.overview
                ratingBar.rating = shows.voteAverage?.toFloat() ?: 0f
                showsRating.text = "(" + shows.voteAverage.toString() + ")"
            }

        }
    }

    private companion object {
        @SuppressLint("DiffUtilEquals")
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<TvShows>() {
            override fun areItemsTheSame(oldItem: TvShows, newItem: TvShows): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TvShows, newItem: TvShows): Boolean {
                return oldItem == newItem
            }

        }
    }

}