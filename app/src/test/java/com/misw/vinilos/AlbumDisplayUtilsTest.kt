package com.misw.vinilos

import com.misw.vinilos.data.model.Album
import com.misw.vinilos.data.model.Performer
import com.misw.vinilos.ui.album.AlbumDisplayUtils
import org.junit.Assert.*
import org.junit.Test

class AlbumDisplayUtilsTest {

    private fun makeAlbum(performers: List<Performer> = emptyList(), recordLabel: String = "Elektra") = Album(
        id = 1, name = "Test", cover = "", releaseDate = "2000-01-01",
        description = "Desc", genre = "Rock", recordLabel = recordLabel,
        performers = performers
    )

    // --- extractArtistName ---

    @Test
    fun `extractArtistName retorna recordLabel cuando no hay performers`() {
        val album = makeAlbum(performers = emptyList(), recordLabel = "Sony Music")
        assertEquals("Sony Music", AlbumDisplayUtils.extractArtistName(album))
    }

    @Test
    fun `extractArtistName retorna nombre del Performer cuando es instancia de Performer`() {
        val performers = listOf(Performer(1, "Rubén Blades", "", "Cantante"))
        val album = makeAlbum(performers = performers)
        assertEquals("Rubén Blades", AlbumDisplayUtils.extractArtistName(album))
    }

    @Test
    fun `extractArtistName usa el primer performer cuando hay varios`() {
        val performers = listOf(
            Performer(1, "Rubén Blades", "", "Cantante"),
            Performer(2, "Willie Colón", "", "Músico")
        )
        val album = makeAlbum(performers = performers)
        assertEquals("Rubén Blades", AlbumDisplayUtils.extractArtistName(album))
    }

    @Test
    fun `extractArtistName con recordLabel vacío retorna cadena vacía`() {
        val album = makeAlbum(performers = emptyList(), recordLabel = "")
        assertEquals("", AlbumDisplayUtils.extractArtistName(album))
    }

    // --- resolveArtistForRecent ---

    @Test
    fun `resolveArtistForRecent retorna recordLabel cuando no hay performers`() {
        val album = makeAlbum(performers = emptyList(), recordLabel = "EMI")
        assertEquals("EMI", AlbumDisplayUtils.resolveArtistForRecent(album))
    }

    @Test
    fun `resolveArtistForRecent retorna nombre del primer performer`() {
        val performers = listOf(Performer(1, "Carlos Vives", "", "Cantante"))
        val album = makeAlbum(performers = performers)
        assertEquals("Carlos Vives", AlbumDisplayUtils.resolveArtistForRecent(album))
    }

    @Test
    fun `resolveArtistForRecent usa el primer performer cuando hay varios`() {
        val performers = listOf(
            Performer(1, "Carlos Vives", "", "Cantante"),
            Performer(2, "Shakira", "", "Cantante")
        )
        val album = makeAlbum(performers = performers)
        assertEquals("Carlos Vives", AlbumDisplayUtils.resolveArtistForRecent(album))
    }

    @Test
    fun `resolveArtistForRecent con recordLabel vacío retorna cadena vacía`() {
        val album = makeAlbum(performers = emptyList(), recordLabel = "")
        assertEquals("", AlbumDisplayUtils.resolveArtistForRecent(album))
    }

}
