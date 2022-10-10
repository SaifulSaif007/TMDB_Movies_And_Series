package com.saiful.person.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saiful.person.databinding.LayoutPersonListItemBinding
import com.saiful.person.model.Person
import com.saiful.shared.utils.AppConstants

class PersonListAdapter(private val listener: (Int) -> Unit) :
    PagingDataAdapter<Person, PersonListAdapter.PersonViewHolder>(DIFF_UTIL) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PersonListAdapter.PersonViewHolder {
        val binding = LayoutPersonListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonListAdapter.PersonViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }


    inner class PersonViewHolder(private val binding: LayoutPersonListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) listener.invoke(item.id)
                }
            }
        }

        fun bind(persons: Person) {
            binding.apply {
                Glide.with(itemView)
                    .load(AppConstants.imageBaseUrl + AppConstants.backdropSize + persons.profilePath)
                    .into(binding.personImage)

                personDepartment.text = persons.knownForDepartment
                personName.text = persons.name
                personKnownFor.text = persons.knownFor?.joinToString(", ") { (it.title ?: it.name).toString() }
            }
        }
    }

    private companion object {
        @SuppressLint("DiffUtilEquals")
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem == newItem
            }

        }
    }

}