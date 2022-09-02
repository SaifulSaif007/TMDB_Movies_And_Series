package com.saiful.movie.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saiful.shared.utils.AppConstants
import com.saiful.movie.databinding.LayoutMovieListItemBinding
import com.saiful.movie.model.Movies

class CollectionAdapter(private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {

    private val differ = AsyncListDiffer(this, DIFF_UTIL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        return CollectionViewHolder(
            LayoutMovieListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<Movies>) = differ.submitList(list)

    inner class CollectionViewHolder(private val binding: LayoutMovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = differ.currentList[position]
                    if (item != null) listener.invoke(item.id)
                }
            }
        }

        fun bind(movies: Movies) {
            binding.apply {
                Glide.with(itemView)
                    .load(AppConstants.imageBaseUrl + AppConstants.backdropSize + movies.posterPath)
                    .into(binding.posterImage)

                movieTitle.text = movies.title
                movieSubtitle.text = movies.overview
                ratingBar.rating = movies.voteAverage?.toFloat() ?: 0f
                movieRating.text = "(" + movies.voteAverage.toString() + ")"
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