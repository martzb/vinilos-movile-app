package com.misw.vinilos.ui.musician

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.misw.vinilos.R
import com.misw.vinilos.data.model.Musician
import com.misw.vinilos.databinding.ItemMusicianBinding

class MusicianAdapter : ListAdapter<Musician, MusicianAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMusicianBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemMusicianBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(musician: Musician) {
            binding.tvMusicianName.text = musician.name
            binding.tvMusicianType.text = if (musician.birthDate != null) "Músico" else "Banda"

            Glide.with(binding.ivMusicianPhoto.context)
                .load(musician.image)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .transform(CircleCrop())
                .into(binding.ivMusicianPhoto)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Musician>() {
        override fun areItemsTheSame(oldItem: Musician, newItem: Musician) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Musician, newItem: Musician) = oldItem == newItem
    }
}
