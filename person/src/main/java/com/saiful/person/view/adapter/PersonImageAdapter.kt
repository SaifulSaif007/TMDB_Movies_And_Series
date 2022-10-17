package com.saiful.person.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.saiful.person.databinding.LayoutPersonCommonItemBinding
import com.saiful.person.model.Image
import com.saiful.shared.utils.loadPosterSizeImage

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
            binding.posterImage.loadPosterSizeImage(image.filePath)
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