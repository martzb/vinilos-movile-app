package com.misw.vinilos.ui.album

import com.misw.vinilos.data.model.Album
import com.misw.vinilos.data.model.Performer

object AlbumDisplayUtils {

    fun extractArtistName(album: Album): String {
        if (album.performers.isEmpty()) return album.recordLabel
        val first = album.performers.first()
        return if (first is Map<*, *>) first["name"] as? String ?: "" else first.name
    }

    fun resolveArtistForRecent(album: Album): String {
        return if (album.performers.isNotEmpty()) {
            album.performers.first().name
        } else {
            album.recordLabel
        }
    }
}
