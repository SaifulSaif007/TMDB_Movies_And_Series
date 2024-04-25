package com.saiful.movie.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.saiful.movie.databinding.LayoutMovieListItemBinding
import com.saiful.shared.model.Movies
import com.saiful.shared.utils.*

class MovieListLoadAdapter(private val listener: (Int) -> Unit) :
    PagingDataAdapter<Movies, MovieListLoadAdapter.MovieViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            LayoutMovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }


    inner class MovieViewHolder(private val binding: LayoutMovieListItemBinding) :
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
        fun bind(movies: Movies) {
            binding.apply {
                binding.posterImage.loadBackDropSizeImage(movies.posterPath)
                movieTitle.text = movies.title
                movieSubtitle.text = movies.overview
                ratingBar.rating = movies.voteAverage?.toFloat() ?: 0f
                movieRating.text = "( ${
                    floatNumberFormatter(
                        movies.voteAverage?.toFloat(),
                        formatPattern = TWO_DECIMAL_FORMAT_PATTERN
                    )
                } )"
            }

        }
    }

    private companion object {
        @SuppressLint("DiffUtilEquals")
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Movies>() {
            override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
                return oldItem == newItem
            }

        }
    }

}