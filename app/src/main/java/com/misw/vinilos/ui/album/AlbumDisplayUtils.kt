package com.misw.vinilos.ui.album

import com.misw.vinilos.data.model.Album

object AlbumDisplayUtils {

    fun extractArtistName(album: Album): String {
        if (album.performers.isEmpty()) return album.recordLabel
        return album.performers.first().name
    }

    fun resolveArtistForRecent(album: Album): String {
        return if (album.performers.isNotEmpty()) {
            album.performers.first().name
        } else {
            album.recordLabel
        }
    }
}
