package com.misw.vinilos

import com.misw.vinilos.data.model.Album
import com.misw.vinilos.data.model.Performer
import com.misw.vinilos.data.model.Track
import org.junit.Assert.*
import org.junit.Test

class AlbumModelTest {

    private fun makeAlbum(
        id: Int = 1,
        name: String = "Buscando América",
        performers: List<Performer> = emptyList(),
        tracks: List<Track> = emptyList()
    ) = Album(
        id = id,
        name = name,
        cover = "https://example.com/cover.jpg",
        releaseDate = "1984-08-01T00:00:00.000Z",
        description = "Descripción",
        genre = "Salsa",
        recordLabel = "Elektra",
        tracks = tracks,
        performers = performers
    )

    @Test
    fun `album tiene valores correctos`() {
        val album = makeAlbum(id = 1, name = "Buscando América")
        assertEquals(1, album.id)
        assertEquals("Buscando América", album.name)
        assertEquals("Elektra", album.recordLabel)
    }

    @Test
    fun `album sin performers tiene lista vacía por defecto`() {
        val album = makeAlbum()
        assertTrue(album.performers.isEmpty())
    }

    @Test
    fun `album con performers retorna el performer correcto`() {
        val performer = Performer(1, "Rubén Blades", "", "Cantante")
        val album = makeAlbum(performers = listOf(performer))
        assertEquals(1, album.performers.size)
        assertEquals("Rubén Blades", album.performers.first().name)
    }

    @Test
    fun `album con tracks retorna el track correcto`() {
        val track = Track(1, "Decisiones", "4:52")
        val album = makeAlbum(tracks = listOf(track))
        assertEquals(1, album.tracks.size)
        assertEquals("Decisiones", album.tracks.first().name)
    }

    @Test
    fun `dos albums con mismo id son iguales`() {
        val a1 = makeAlbum(id = 1, name = "Álbum A")
        val a2 = makeAlbum(id = 1, name = "Álbum A")
        assertEquals(a1, a2)
    }

    @Test
    fun `dos albums con distinto id no son iguales`() {
        val a1 = makeAlbum(id = 1)
        val a2 = makeAlbum(id = 2)
        assertNotEquals(a1, a2)
    }
}
