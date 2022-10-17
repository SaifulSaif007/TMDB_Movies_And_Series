package com.saiful.movie.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.saiful.movie.databinding.LayoutTrailerItemBinding
import com.saiful.movie.model.ResultsItem
import com.saiful.shared.utils.loadYoutubeImage

class MovieTrailerAdapter(private val listener: (String) -> Unit) :
    RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder>() {

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieTrailerViewHolder {
        return MovieTrailerViewHolder(
            LayoutTrailerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieTrailerViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    fun submitList(list: List<ResultsItem>) {
        differ.submitList(list)
    }

    inner class MovieTrailerViewHolder(private val binding: LayoutTrailerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = differ.currentList[position]
                    listener.invoke(item.key.toString())
                }
            }
        }

        fun bind(item: ResultsItem) {
            binding.trailerName.text = item.name
            binding.trailerImage.loadYoutubeImage(item.key)
        }
    }

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultsItem>() {
            override fun areItemsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
                return oldItem == newItem
            }

        }
    }

}