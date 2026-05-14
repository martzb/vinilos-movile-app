package com.misw.vinilos.ui.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.misw.vinilos.data.model.Track
import com.misw.vinilos.databinding.ItemAddedTrackBinding

class AddedTrackAdapter : ListAdapter<Track, AddedTrackAdapter.AddedTrackViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedTrackViewHolder {
        val binding = ItemAddedTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddedTrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddedTrackViewHolder, position: Int) {
        holder.bind(getItem(position), position + 1)
    }

    class AddedTrackViewHolder(private val binding: ItemAddedTrackBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(track: Track, number: Int) {
            binding.tvTrackNumber.text = number.toString()
            binding.tvTrackName.text = track.name
            binding.tvTrackDuration.text = track.duration
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Track, newItem: Track) = oldItem == newItem
    }
}
