package com.saiful.tvshows.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.saiful.shared.model.TvShows
import com.saiful.shared.utils.*
import com.saiful.tvshows.databinding.LayoutShowsListItemBinding

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

        @SuppressLint("SetTextI18n")
        fun bind(shows: TvShows) {
            binding.apply {
                binding.posterImage.loadBackDropSizeImage(shows.posterPath)
                showsTitle.text = shows.name
                showsOverview.text = shows.overview
                ratingBar.rating = shows.voteAverage?.toFloat() ?: 0f
                showsRating.text = "( ${
                    floatNumberFormatter(
                        shows.voteAverage?.toFloat(),
                        TWO_DECIMAL_FORMAT_PATTERN
                    )
                } )"
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