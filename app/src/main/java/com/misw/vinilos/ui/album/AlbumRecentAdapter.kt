package com.misw.vinilos.ui.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.misw.vinilos.R
import com.misw.vinilos.data.model.Album
import com.misw.vinilos.databinding.ItemAlbumRecentBinding

class AlbumRecentAdapter(
    private val onItemClick: (Album) -> Unit
) : ListAdapter<Album, AlbumRecentAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlbumRecentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemAlbumRecentBinding,
        private val onItemClick: (Album) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.tvAlbumName.text = album.name

            val artistName = if (album.performers.isNotEmpty()) {
                val first = album.performers.first()
                if (first is Map<*, *>) first["name"] as? String ?: "" else ""
            } else {
                album.recordLabel
            }
            binding.tvAlbumArtist.text = artistName

            Glide.with(binding.ivCover.context)
                .load(album.cover)
                .placeholder(R.drawable.ic_vinyl_record)
                .error(R.drawable.ic_vinyl_record)
                .centerCrop()
                .into(binding.ivCover)

            // Click
            binding.root.setOnClickListener {
                onItemClick(album)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Album, newItem: Album) = oldItem == newItem
    }
}

