package com.saiful.tvshows.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.saiful.shared.utils.floatNumberFormatter
import com.saiful.shared.utils.loadBackDropSizeImage
import com.saiful.tvshows.databinding.LayoutEpisodeItemBinding
import com.saiful.tvshows.model.Episode

class EpisodeListAdapter : RecyclerView.Adapter<EpisodeListAdapter.EpisodeVH>() {

    private val differ = AsyncListDiffer(this, DIFF_UTIL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeVH {
        val binding = LayoutEpisodeItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EpisodeVH(binding)
    }

    override fun onBindViewHolder(holder: EpisodeVH, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<Episode>){
        differ.submitList(list)
    }

    inner class EpisodeVH(private val binding: LayoutEpisodeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(episode: Episode) {
            binding.apply {
                binding.posterImage.loadBackDropSizeImage(url = episode.stillPath)
                airDate.text = episode.airDate
                showsTitle.text = episode.name
                showsOverview.text = episode.overview
                ratingBar.rating = episode.voteAverage.toFloat()
                showsRating.text = "(" + floatNumberFormatter(episode.voteAverage.toFloat())+ ")"
            }
        }
    }

    private companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Episode>() {
            override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem == newItem
            }

        }
    }

}