package com.saiful.person.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.saiful.person.databinding.LayoutPersonCommonItemBinding
import com.saiful.person.model.Image
import com.saiful.shared.utils.AppConstants


class PersonImageAdapter : RecyclerView.Adapter<PersonImageAdapter.PersonImageViewHolder>() {

    private val differ = AsyncListDiffer(this, DIFF_UTIL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonImageViewHolder {
        return PersonImageViewHolder(
            LayoutPersonCommonItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PersonImageViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<Image>) {
        differ.submitList(list)
    }

    inner class PersonImageViewHolder(private val binding: LayoutPersonCommonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            //todo -> onclick
        }

        fun bind(image: Image) {
            Glide.with(binding.root.context)
                .load(AppConstants.imageBaseUrl + AppConstants.posterSize + image.filePath)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                //.error(R.drawable.image1)
                .into(binding.posterImage)
        }
    }

    private companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.filePath == newItem.filePath
            }

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem == newItem
            }
        }
    }

}