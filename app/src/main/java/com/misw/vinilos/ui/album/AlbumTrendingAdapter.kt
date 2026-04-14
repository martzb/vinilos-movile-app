package com.misw.vinilos.ui.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.misw.vinilos.R
import com.misw.vinilos.data.model.Album
import com.misw.vinilos.databinding.ItemAlbumTrendingBinding

class AlbumTrendingAdapter : ListAdapter<Album, AlbumTrendingAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlbumTrendingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemAlbumTrendingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.tvAlbumName.text = album.name
            binding.tvGenre.text = album.genre

            val artistName = extractArtistName(album)
            binding.tvAlbumArtist.text = artistName

            Glide.with(binding.ivCover.context)
                .load(album.cover)
                .placeholder(R.drawable.ic_vinyl_record)
                .error(R.drawable.ic_vinyl_record)
                .centerCrop()
                .into(binding.ivCover)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Album, newItem: Album) = oldItem == newItem
    }
}

private fun extractArtistName(album: Album): String {
    if (album.performers.isEmpty()) return album.recordLabel
    val first = album.performers.first()
    return if (first is Map<*, *>) first["name"] as? String ?: "" else ""
}
