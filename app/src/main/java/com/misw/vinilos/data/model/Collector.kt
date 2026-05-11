package com.misw.vinilos.data.model

data class Collector(
    val id: Int,
    val name: String,
    val email: String,
    val telephone: String? = null,
    val favoritePerformers: List<Performer> = emptyList(),
    val collectorAlbums: List<CollectorAlbum> = emptyList()
)
