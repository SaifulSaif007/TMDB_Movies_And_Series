package com.saiful.tvshows.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.saiful.shared.utils.loadPosterSizeImage
import com.saiful.tvshows.databinding.LayoutSeasonItemBinding
import com.saiful.tvshows.model.Season

class ShowSeasonAdapter(private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<ShowSeasonAdapter.SeasonViewHolder>() {

    private val differ = AsyncListDiffer(this, DIFF_UTIL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonViewHolder {
        return SeasonViewHolder(
            LayoutSeasonItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<Season>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    inner class SeasonViewHolder(private val binding: LayoutSeasonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = differ.currentList[position]
                    listener.invoke(item.seasonNumber)
                }
            }
        }

        fun bind(item: Season) {
            binding.apply {
                binding.seasonImage.loadPosterSizeImage(item.posterPath)
                seasonNo.text = "Season ${item.seasonNumber}"

            }
        }
    }

    private companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Season>() {
            override fun areItemsTheSame(oldItem: Season, newItem: Season): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Season, newItem: Season): Boolean {
                return oldItem == newItem
            }
        }
    }

}