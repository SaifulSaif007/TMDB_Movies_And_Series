package com.saiful.person.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.saiful.person.databinding.LayoutPersonItemBinding
import com.saiful.person.model.Person
import com.saiful.shared.utils.AppConstants

class PersonDashboardAdapter : RecyclerView.Adapter<PersonDashboardAdapter.PersonViewHolder>() {

    private val differ = AsyncListDiffer(this, DIFF_UTIL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        return PersonViewHolder(
            LayoutPersonItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun submitList(list: List<Person>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    inner class PersonViewHolder(private val binding: LayoutPersonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // on click
        }

        fun bind(person: Person) {
            binding.knownDepartment.text = person.name

            Glide.with(binding.root.context)
                .load(AppConstants.imageBaseUrl + AppConstants.posterSize + person.profilePath)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                //.error(R.drawable.image1)
                .into(binding.posterImage)
        }
    }

    private companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem == newItem
            }
        }
    }

}