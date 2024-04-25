package com.saiful.tvshows.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.saiful.shared.utils.loadPosterSizeImage
import com.saiful.tvshows.databinding.LayoutShowsCastItemBinding
import com.saiful.tvshows.model.Cast

class ShowCastAdapter(private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<ShowCastAdapter.CastViewHolder>() {

    private val differ = AsyncListDiffer(this, DIFF_UTIL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            LayoutShowsCastItemBinding.inflate(
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

    inner class CastViewHolder(private val binding: LayoutShowsCastItemBinding) :
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

        fun bind(item: Cast) {
            binding.apply {
                Glide.with(binding.root.context)
                binding.castImage.loadPosterSizeImage(item.profilePath)
                castName.text = item.originalName
                castCharacterName.text = item.roles.joinToString(", ") { it.character }
            }
        }
    }

    private companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Cast>() {
            override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
                return oldItem == newItem
            }

        }
    }

}