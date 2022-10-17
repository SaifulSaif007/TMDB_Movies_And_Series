package com.saiful.person.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.saiful.person.databinding.LayoutPersonItemBinding
import com.saiful.person.model.Person
import com.saiful.shared.utils.loadPosterSizeImage

class PersonDashboardAdapter(val listener: (Int) -> Unit) :
    RecyclerView.Adapter<PersonDashboardAdapter.PersonViewHolder>() {

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
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = differ.currentList[position]
                    if (item != null) listener.invoke(item.id)
                }
            }
        }

        fun bind(person: Person) {
            binding.knownDepartment.text = person.name
            binding.posterImage.loadPosterSizeImage(person.profilePath)
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