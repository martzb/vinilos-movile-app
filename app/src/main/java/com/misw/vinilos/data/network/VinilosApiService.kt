package com.misw.vinilos.data.network

import com.misw.vinilos.data.model.Album
import retrofit2.http.GET
import retrofit2.http.Path

interface VinilosApiService {

    @GET("albums")
    suspend fun getAlbums(): List<Album>

    @GET("albums/{id}")
    suspend fun getAlbum(@Path("id") albumId: Int): Album
}
