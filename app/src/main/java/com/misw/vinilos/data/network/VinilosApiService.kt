package com.misw.vinilos.data.network

import com.misw.vinilos.data.model.Album
import com.misw.vinilos.data.model.Collector
import com.misw.vinilos.data.model.Musician
import retrofit2.http.GET
import retrofit2.http.Path

interface VinilosApiService {

    @GET("albums")
    suspend fun getAlbums(): List<Album>

    @GET("albums/{id}")
    suspend fun getAlbum(@Path("id") albumId: Int): Album

    @GET("musicians")
    suspend fun getMusicians(): List<Musician>

    @GET ("musicians/{id}")
    suspend fun getMusician(@Path("id") musicianId: Int): Musician

    @GET("collectors")
    suspend fun getCollectors(): List<Collector>
}
