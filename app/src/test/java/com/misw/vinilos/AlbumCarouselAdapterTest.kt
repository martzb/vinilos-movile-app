package com.misw.vinilos

import com.misw.vinilos.data.model.Album
import com.misw.vinilos.ui.album.AlbumDiffCallback
import org.junit.Assert.*
import org.junit.Test

class AlbumCarouselAdapterTest {

    private fun makeAlbum(id: Int = 1, name: String = "Álbum") = Album(
        id = id,
        name = name,
        cover = "https://example.com/cover.jpg",
        releaseDate = "1984-08-01T00:00:00.000Z",
        description = "Desc",
        genre = "Salsa",
        recordLabel = "Elektra"
    )

    private val diffCallback = AlbumDiffCallback()

    @Test
    fun `areItemsTheSame retorna true cuando los ids son iguales`() {
        val a1 = makeAlbum(id = 1, name = "Nombre A")
        val a2 = makeAlbum(id = 1, name = "Nombre B")
        assertTrue(diffCallback.areItemsTheSame(a1, a2))
    }

    @Test
    fun `areItemsTheSame retorna false cuando los ids son distintos`() {
        val a1 = makeAlbum(id = 1)
        val a2 = makeAlbum(id = 2)
        assertFalse(diffCallback.areItemsTheSame(a1, a2))
    }

    @Test
    fun `areContentsTheSame retorna true cuando los albums son iguales`() {
        val a1 = makeAlbum(id = 1, name = "Buscando América")
        val a2 = makeAlbum(id = 1, name = "Buscando América")
        assertTrue(diffCallback.areContentsTheSame(a1, a2))
    }

    @Test
    fun `areContentsTheSame retorna false cuando los albums difieren`() {
        val a1 = makeAlbum(id = 1, name = "Buscando América")
        val a2 = makeAlbum(id = 1, name = "Otro Álbum")
        assertFalse(diffCallback.areContentsTheSame(a1, a2))
    }
}
