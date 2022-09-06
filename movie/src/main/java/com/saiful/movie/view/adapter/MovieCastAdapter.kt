package com.saiful.movie.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.saiful.shared.utils.AppConstants
import com.saiful.movie.R
import com.saiful.movie.databinding.LayoutCastItemBinding
import com.saiful.movie.model.Cast

class MovieCastAdapter() : RecyclerView.Adapter<MovieCastAdapter.CastViewHolder>() {

    private val differ = AsyncListDiffer(this, DIFF_UTIL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            LayoutCastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<Cast>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    inner class CastViewHolder(private val binding: LayoutCastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            //todo -> onclick
        }

        fun bind(item: Cast) {
            binding.apply {
                Glide.with(binding.root.context)
                    .load(AppConstants.imageBaseUrl + AppConstants.posterSize + item.profilePath)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    //.error(R.drawable.image1)
                    .into(binding.castImage)

                castName.text = item.originalName
                castCharacterName.text = item.character
            }
        }
    }

    private companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Cast>() {
            override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
                return oldItem.castId == newItem.castId
            }

            override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
                return oldItem == newItem
            }

        }
    }

}