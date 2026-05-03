package com.misw.vinilos

import com.misw.vinilos.data.model.Album
import com.misw.vinilos.data.model.Collector
import com.misw.vinilos.data.model.Musician
import com.misw.vinilos.data.network.ApiClient
import com.misw.vinilos.data.network.VinilosApiService
import com.misw.vinilos.data.repository.AlbumRepository
import com.misw.vinilos.data.repository.CollectorRepository
import com.misw.vinilos.data.repository.MusicianRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryTest {

    private lateinit var apiService: VinilosApiService

    @Before
    fun setUp() {
        apiService = mockk()
        mockkObject(ApiClient)
        every { ApiClient.retrofitService } returns apiService
    }

    @After
    fun tearDown() {
        unmockkObject(ApiClient)
    }

    private fun makeAlbum(id: Int = 1) = Album(
        id = id, name = "Álbum $id", cover = "", releaseDate = "2000-01-01",
        description = "Desc", genre = "Rock", recordLabel = "Sony"
    )

    // --- AlbumRepository ---

    @Test
    fun `AlbumRepository getAlbums retorna lista del servicio`() = runTest {
        val albums = listOf(makeAlbum(1), makeAlbum(2))
        coEvery { apiService.getAlbums() } returns albums
        val repo = AlbumRepository()
        assertEquals(albums, repo.getAlbums())
    }

    @Test
    fun `AlbumRepository getAlbumById retorna el album correcto`() = runTest {
        val album = makeAlbum(42)
        coEvery { apiService.getAlbum(42) } returns album
        val repo = AlbumRepository()
        assertEquals(album, repo.getAlbumById(42))
    }

    @Test
    fun `AlbumRepository getAlbums propaga excepción`() = runTest {
        coEvery { apiService.getAlbums() } throws RuntimeException("Sin red")
        val repo = AlbumRepository()
        var caught: Exception? = null
        try { repo.getAlbums() } catch (e: Exception) { caught = e }
        assertNotNull(caught)
    }

    // --- MusicianRepository ---

    @Test
    fun `MusicianRepository getMusicians retorna lista del servicio`() = runTest {
        val musicians = listOf(
            Musician(1, "Rubén Blades", "", "Cantante"),
            Musician(2, "Willie Colón", "", "Músico")
        )
        coEvery { apiService.getMusicians() } returns musicians
        val repo = MusicianRepository()
        assertEquals(musicians, repo.getMusicians())
    }

    @Test
    fun `MusicianRepository getMusician retorna el músico correcto`() = runTest {
        val musician = Musician(7, "Carlos Vives", "", "Cantante")
        coEvery { apiService.getMusician(7) } returns musician
        val repo = MusicianRepository()
        assertEquals(musician, repo.getMusician(7))
    }

    @Test
    fun `MusicianRepository getMusicians propaga excepción`() = runTest {
        coEvery { apiService.getMusicians() } throws RuntimeException("Error de red")
        val repo = MusicianRepository()
        var caught: Exception? = null
        try { repo.getMusicians() } catch (e: Exception) { caught = e }
        assertNotNull(caught)
    }

    // --- CollectorRepository ---

    @Test
    fun `CollectorRepository getCollectors retorna lista del servicio`() = runTest {
        val collectors = listOf(
            Collector(1, "Manolo Bellon", "manolo@gmail.com"),
            Collector(2, "Jaime Zuluaga", "jaime@gmail.com")
        )
        coEvery { apiService.getCollectors() } returns collectors
        val repo = CollectorRepository()
        assertEquals(collectors, repo.getCollectors())
    }

    @Test
    fun `CollectorRepository getCollectors propaga excepción`() = runTest {
        coEvery { apiService.getCollectors() } throws RuntimeException("Sin red")
        val repo = CollectorRepository()
        var caught: Exception? = null
        try { repo.getCollectors() } catch (e: Exception) { caught = e }
        assertNotNull(caught)
    }
}
