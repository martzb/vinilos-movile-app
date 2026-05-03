package com.misw.vinilos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.misw.vinilos.data.model.Album
import com.misw.vinilos.data.model.Performer
import com.misw.vinilos.data.model.Track
import com.misw.vinilos.data.repository.AlbumRepository
import com.misw.vinilos.ui.album.AlbumViewModel
import io.mockk.coEvery
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
class AlbumViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: AlbumRepository
    private lateinit var viewModel: AlbumViewModel

    private fun makeAlbum(id: Int = 1) = Album(
        id = id, name = "Buscando América", cover = "", releaseDate = "1984-01-01",
        description = "Desc", genre = "Salsa", recordLabel = "Elektra",
        tracks = listOf(Track(1, "Decisiones", "4:52")),
        performers = listOf(Performer(1, "Rubén Blades", "", "Cantante"))
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadAlbums exitoso setea albums y isLoading false`() = runTest {
        val albums = listOf(makeAlbum(1), makeAlbum(2))
        coEvery { repository.getAlbums() } returns albums
        viewModel = AlbumViewModel(repository)
        advanceUntilIdle()
        assertEquals(albums, viewModel.albums.value)
        assertFalse(viewModel.isLoading.value ?: true)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `loadAlbums error setea mensaje de error y isLoading false`() = runTest {
        coEvery { repository.getAlbums() } throws RuntimeException("Sin red")
        viewModel = AlbumViewModel(repository)
        advanceUntilIdle()
        assertEquals("No se pudo cargar el catálogo. Verifica tu conexión.", viewModel.error.value)
        assertFalse(viewModel.isLoading.value ?: true)
        assertNull(viewModel.albums.value)
    }

    @Test
    fun `loadAlbums llamado manualmente vuelve a cargar`() = runTest {
        val albums = listOf(makeAlbum(1))
        coEvery { repository.getAlbums() } returns albums
        viewModel = AlbumViewModel(repository)
        advanceUntilIdle()
        viewModel.loadAlbums()
        advanceUntilIdle()
        assertEquals(albums, viewModel.albums.value)
    }

    @Test
    fun `loadAlbumDetail exitoso setea albumDetail y limpia error`() = runTest {
        val album = makeAlbum(42)
        coEvery { repository.getAlbums() } returns emptyList()
        coEvery { repository.getAlbumById(42) } returns album
        viewModel = AlbumViewModel(repository)
        advanceUntilIdle()
        viewModel.loadAlbumDetail(42)
        advanceUntilIdle()
        assertEquals(album, viewModel.albumDetail.value)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `loadAlbumDetail error setea mensaje específico`() = runTest {
        coEvery { repository.getAlbums() } returns emptyList()
        coEvery { repository.getAlbumById(any()) } throws RuntimeException("No encontrado")
        viewModel = AlbumViewModel(repository)
        advanceUntilIdle()
        viewModel.loadAlbumDetail(99)
        advanceUntilIdle()
        assertEquals("No se pudo cargar el detalle del álbum.", viewModel.error.value)
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `isLoading es true durante la carga`() = runTest {
        coEvery { repository.getAlbums() } returns emptyList()
        viewModel = AlbumViewModel(repository)
        // Antes de avanzar el dispatcher la coroutine está pendiente
        assertTrue(viewModel.isLoading.value ?: false)
        advanceUntilIdle()
        assertFalse(viewModel.isLoading.value ?: true)
    }
}
