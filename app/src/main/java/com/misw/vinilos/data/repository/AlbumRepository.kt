package com.misw.vinilos.data.repository

import com.misw.vinilos.data.model.Album
import com.misw.vinilos.data.network.ApiClient

/**
 * Repositorio de álbumes.
 * Expone los datos del servidor al ViewModel siguiendo el patrón Repository.
 *
 * Issue: [HU01] Consumir endpoint GET /albums #2
 */
class AlbumRepository {

    /**
     * Obtiene la lista completa de álbumes desde el endpoint GET /albums.
     * @return lista de [Album] mapeada desde el JSON del servidor.
     * @throws Exception si hay un error de red o el servidor no responde.
     */
    suspend fun getAlbums(): List<Album> {
        return ApiClient.retrofitService.getAlbums()
    }
}
