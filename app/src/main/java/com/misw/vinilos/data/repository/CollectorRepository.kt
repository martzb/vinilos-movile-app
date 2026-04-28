package com.misw.vinilos.data.repository

import com.misw.vinilos.data.model.Collector
import com.misw.vinilos.data.network.ApiClient

class CollectorRepository {

    suspend fun getCollectors(): List<Collector> {
        return ApiClient.retrofitService.getCollectors()
    }
}
