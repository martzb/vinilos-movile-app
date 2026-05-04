package com.misw.vinilos.ui.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.misw.vinilos.R
import com.misw.vinilos.data.model.Album
import com.misw.vinilos.databinding.ItemAlbumTrendingBinding

class AlbumTrendingAdapter(
    private val onItemClick: (Album) -> Unit
) : ListAdapter<Album, AlbumTrendingAdapter.ViewHolder>(AlbumDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlbumTrendingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemAlbumTrendingBinding,
        private val onItemClick: (Album) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.tvAlbumName.text = album.name
            binding.tvGenre.text = album.genre
            binding.tvAlbumArtist.text = AlbumDisplayUtils.extractArtistName(album)
            Glide.with(binding.ivCover.context)
                .load(album.cover)
                .placeholder(R.drawable.ic_vinyl_record)
                .error(R.drawable.ic_vinyl_record)
                .centerCrop()
                .into(binding.ivCover)
            binding.root.setOnClickListener { onItemClick(album) }
        }
    }
}
