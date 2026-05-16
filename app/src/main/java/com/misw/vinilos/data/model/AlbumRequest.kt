package com.misw.vinilos.data.model

/**
 * Payload para POST /albums.
 *
 * Se usa exclusivamente para crear un álbum nuevo.
 * Solo incluye los campos que el backend requiere/acepta:
 * name, cover, releaseDate, description, genre, recordLabel.
 *
 * No incluye id, tracks, performers ni comments, que el backend
 * genera o gestiona por su cuenta.
 */
data class AlbumRequest(
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String
)
