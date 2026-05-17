package com.misw.vinilos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.misw.vinilos.data.model.Album
import com.misw.vinilos.data.model.Track
import com.misw.vinilos.data.model.TrackRequest
import com.misw.vinilos.data.repository.AlbumRepository
import com.misw.vinilos.ui.album.CreateTrackViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateTrackViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: AlbumRepository
    private lateinit var viewModel: CreateTrackViewModel

    private fun makeAlbum(tracks: List<Track> = emptyList()) = Album(
        id = 1, name = "Buscando América", cover = "", releaseDate = "1984-01-01",
        description = "Desc", genre = "Salsa", recordLabel = "Elektra",
        tracks = tracks
    )

    private fun makeTrack(id: Int = 1, name: String = "Decisiones") =
        Track(id = id, name = name, duration = "03:45")

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = CreateTrackViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── fetchAlbum ────────────────────────────────────────────────────────────

    @Test
    fun `fetchAlbum exitoso setea album y tracks`() = runTest {
        val track = makeTrack()
        val album = makeAlbum(tracks = listOf(track))
        coEvery { repository.getAlbumById(1) } returns album

        viewModel.fetchAlbum(1)
        advanceUntilIdle()

        assertEquals(album, viewModel.album.value)
        assertEquals(listOf(track), viewModel.tracks.value)
        assertFalse(viewModel.isLoading.value ?: true)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `fetchAlbum error setea mensaje de error`() = runTest {
        coEvery { repository.getAlbumById(any()) } throws RuntimeException("Sin red")

        viewModel.fetchAlbum(1)
        advanceUntilIdle()

        assertNotNull(viewModel.error.value)
        assertTrue(viewModel.error.value!!.contains("Error al cargar"))
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `fetchAlbum album sin tracks setea lista vacía`() = runTest {
        val album = makeAlbum(tracks = emptyList())
        coEvery { repository.getAlbumById(1) } returns album

        viewModel.fetchAlbum(1)
        advanceUntilIdle()

        assertEquals(emptyList<Track>(), viewModel.tracks.value)
    }

    // ── createTrack ───────────────────────────────────────────────────────────

    @Test
    fun `createTrack exitoso agrega track a la lista y emite isSuccess true`() = runTest {
        val album = makeAlbum(tracks = emptyList())
        val newTrack = makeTrack(id = 10, name = "Nueva canción")
        coEvery { repository.getAlbumById(1) } returns album
        coEvery { repository.createTrack(1, TrackRequest("Nueva canción", "03:00")) } returns newTrack

        viewModel.fetchAlbum(1)
        advanceUntilIdle()

        viewModel.createTrack(1, "Nueva canción", "03:00")
        advanceUntilIdle()

        assertTrue(viewModel.isSuccess.value ?: false)
        assertEquals(listOf(newTrack), viewModel.tracks.value)
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `createTrack agrega track a lista existente`() = runTest {
        val existingTrack = makeTrack(id = 1, name = "Canción existente")
        val album = makeAlbum(tracks = listOf(existingTrack))
        val newTrack = makeTrack(id = 2, name = "Canción nueva")
        coEvery { repository.getAlbumById(1) } returns album
        coEvery { repository.createTrack(1, TrackRequest("Canción nueva", "02:30")) } returns newTrack

        viewModel.fetchAlbum(1)
        advanceUntilIdle()

        viewModel.createTrack(1, "Canción nueva", "02:30")
        advanceUntilIdle()

        assertEquals(2, viewModel.tracks.value?.size)
        assertTrue(viewModel.tracks.value!!.contains(newTrack))
    }

    @Test
    fun `createTrack error setea mensaje de error y no emite isSuccess`() = runTest {
        coEvery { repository.getAlbumById(1) } returns makeAlbum()
        coEvery { repository.createTrack(any(), any()) } throws RuntimeException("Error 500")

        viewModel.fetchAlbum(1)
        advanceUntilIdle()

        viewModel.createTrack(1, "Canción", "03:00")
        advanceUntilIdle()

        assertNotNull(viewModel.error.value)
        assertFalse(viewModel.isSuccess.value ?: false)
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `createTrack llama al repositorio con los datos correctos`() = runTest {
        coEvery { repository.getAlbumById(1) } returns makeAlbum()
        coEvery { repository.createTrack(1, TrackRequest("Mi track", "04:00")) } returns makeTrack()

        viewModel.fetchAlbum(1)
        advanceUntilIdle()

        viewModel.createTrack(1, "Mi track", "04:00")
        advanceUntilIdle()

        coVerify { repository.createTrack(1, TrackRequest("Mi track", "04:00")) }
    }

    // ── resetSuccess ──────────────────────────────────────────────────────────

    @Test
    fun `resetSuccess pone isSuccess en false`() = runTest {
        coEvery { repository.getAlbumById(1) } returns makeAlbum()
        coEvery { repository.createTrack(any(), any()) } returns makeTrack()

        viewModel.fetchAlbum(1)
        advanceUntilIdle()

        viewModel.createTrack(1, "Track", "01:00")
        advanceUntilIdle()

        assertTrue(viewModel.isSuccess.value ?: false)
        viewModel.resetSuccess()
        assertFalse(viewModel.isSuccess.value ?: true)
    }
}
