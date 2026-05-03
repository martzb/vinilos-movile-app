package com.misw.vinilos.ui.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.misw.vinilos.data.model.Album
import com.misw.vinilos.databinding.ItemAlbumRecentBinding

class AlbumRecentAdapter(
    private val onItemClick: ((Album) -> Unit)? = null
) : ListAdapter<Album, AlbumRecentAdapter.ViewHolder>(AlbumDiffCallback()) {

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
        private val onItemClick: ((Album) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.tvAlbumName.text = album.name
            binding.tvAlbumArtist.text = AlbumDisplayUtils.resolveArtistForRecent(album)
            binding.ivCover.loadAlbumCover(album.cover)
            binding.root.setOnClickListener { onItemClick?.invoke(album) }
        }
    }
}
