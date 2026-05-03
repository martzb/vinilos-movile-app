package com.misw.vinilos.ui.album

import com.misw.vinilos.data.model.Album

object AlbumDisplayUtils {

    fun extractArtistName(album: Album): String {
        val performers = album.performers.orEmpty()
        if (performers.isEmpty()) return album.recordLabel
        return performers.first().name
    }

    fun resolveArtistForRecent(album: Album): String {
        val performers = album.performers.orEmpty()
        return if (performers.isNotEmpty()) performers.first().name else album.recordLabel
    }
}
