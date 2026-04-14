package com.misw.vinilos.data.repository

import com.misw.vinilos.data.model.Album
import com.misw.vinilos.data.network.ApiClient

class AlbumRepository {

    suspend fun getAlbums(): List<Album> {
        return ApiClient.retrofitService.getAlbums()
    }
}
