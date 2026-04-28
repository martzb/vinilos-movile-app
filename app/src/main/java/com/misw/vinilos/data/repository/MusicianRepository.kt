package com.misw.vinilos.data.repository

import com.misw.vinilos.data.model.Musician
import com.misw.vinilos.data.network.ApiClient

class MusicianRepository {

    suspend fun getMusicians(): List<Musician> {
        return ApiClient.retrofitService.getMusicians()
    }
}
