package com.misw.vinilos.ui.album

import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.misw.vinilos.R
import com.misw.vinilos.data.model.Album

class AlbumDiffCallback : DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Album, newItem: Album) = oldItem == newItem
}

fun ImageView.loadAlbumCover(url: String) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.ic_vinyl_record)
        .error(R.drawable.ic_vinyl_record)
        .centerCrop()
        .into(this)
}
