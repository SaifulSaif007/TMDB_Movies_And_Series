package com.saiful.movie.view.adapter

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
import com.saiful.movie.R
import com.saiful.movie.databinding.LayoutMovieItemBinding
import com.saiful.movie.model.Movies

class MovieDashboardAdapter (private val listener: (Int) -> Unit):
    RecyclerView.Adapter<MovieDashboardAdapter.MovieDashboardViewHolder>() {

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDashboardViewHolder {
        val binding = LayoutMovieItemBinding.inflate(
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

    fun submitList(list: List<Movies>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    inner class MovieDashboardViewHolder(private val binding: LayoutMovieItemBinding) :
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
        fun bind(item: Movies) {
            Glide.with(binding.root.context)
                .load(imageBaseUrl + posterSize + item.posterPath)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                //.error(R.drawable.image1)
                .into(binding.posterImage)
        }
    }

    private companion object {
        @SuppressLint("DiffUtilEquals")
        private val diffCallback = object : DiffUtil.ItemCallback<Movies>() {
            override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
                return oldItem == newItem
            }
        }
    }
}