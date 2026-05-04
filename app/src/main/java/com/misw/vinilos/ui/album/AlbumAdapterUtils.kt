package com.misw.vinilos.ui.album

import androidx.recyclerview.widget.DiffUtil
import com.misw.vinilos.data.model.Album

class AlbumDiffCallback : DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Album, newItem: Album) = oldItem == newItem
}
