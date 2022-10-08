package com.saiful.person.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.saiful.person.databinding.LayoutPersonCommonItemBinding
import com.saiful.shared.model.TvShows
import com.saiful.shared.utils.AppConstants

class PersonShowsAdapter(private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<PersonShowsAdapter.ShowsViewHolder>() {

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        return ShowsViewHolder(
            LayoutPersonCommonItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<TvShows>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    inner class ShowsViewHolder(private val binding: LayoutPersonCommonItemBinding) :
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

        fun bind(shows: TvShows) {
            Glide.with(binding.root.context)
                .load(AppConstants.imageBaseUrl + AppConstants.posterSize + shows.posterPath)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                //.error(R.drawable.image1)
                .into(binding.posterImage)
        }
    }

    private companion object {
        @SuppressLint("DiffUtilEquals")
        private val diffCallback = object : DiffUtil.ItemCallback<TvShows>() {
            override fun areItemsTheSame(oldItem: TvShows, newItem: TvShows): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TvShows, newItem: TvShows): Boolean {
                return oldItem == newItem
            }
        }
    }

}