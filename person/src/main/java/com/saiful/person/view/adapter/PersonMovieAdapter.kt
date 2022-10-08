package com.saiful.person.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.saiful.person.R
import com.saiful.person.databinding.LayoutPersonCommonItemBinding
import com.saiful.shared.model.Movies
import com.saiful.shared.utils.AppConstants

class PersonMovieAdapter(private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<PersonMovieAdapter.MovieViewHolder>() {

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutPersonCommonItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<Movies>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(private val binding: LayoutPersonCommonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
           binding.root.setOnClickListener {
               val position = bindingAdapterPosition
               if (position != RecyclerView.NO_POSITION){
                   val item = differ.currentList[position]
                   listener.invoke(item.id)
               }
           }
        }

        fun bind(movies: Movies) {
            Glide.with(binding.root.context)
                .load(AppConstants.imageBaseUrl + AppConstants.posterSize + movies.posterPath)
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