package com.misw.vinilos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.misw.vinilos.data.model.Album
import com.misw.vinilos.data.model.Musician
import com.misw.vinilos.data.repository.MusicianRepository
import com.misw.vinilos.ui.musician.MusicianDetailViewModel
import com.misw.vinilos.ui.musician.MusicianViewModel
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
class MusicianViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: MusicianRepository

    private fun makeMusician(id: Int = 1) = Musician(
        id = id, name = "Rubén Blades", image = "", description = "Cantante",
        birthDate = "1948-07-16", albums = emptyList()
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

    // --- MusicianViewModel ---

    @Test
    fun `loadMusicians exitoso setea musicians y limpia error`() = runTest {
        val musicians = listOf(makeMusician(1), makeMusician(2))
        coEvery { repository.getMusicians() } returns musicians
        val viewModel = MusicianViewModel(repository)
        advanceUntilIdle()
        assertEquals(musicians, viewModel.musicians.value)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `loadMusicians error setea mensaje y isLoading false`() = runTest {
        coEvery { repository.getMusicians() } throws RuntimeException("Sin red")
        val viewModel = MusicianViewModel(repository)
        advanceUntilIdle()
        assertEquals("No se pudo cargar la lista de artistas. Verifica tu conexión.", viewModel.error.value)
        assertFalse(viewModel.isLoading.value ?: true)
        assertNull(viewModel.musicians.value)
    }

    @Test
    fun `loadMusicians llamado manualmente recarga`() = runTest {
        val musicians = listOf(makeMusician(3))
        coEvery { repository.getMusicians() } returns musicians
        val viewModel = MusicianViewModel(repository)
        advanceUntilIdle()
        viewModel.loadMusicians()
        advanceUntilIdle()
        assertEquals(musicians, viewModel.musicians.value)
    }

    @Test
    fun `isLoading es false tras completar la carga`() = runTest {
        coEvery { repository.getMusicians() } returns emptyList()
        val viewModel = MusicianViewModel(repository)
        advanceUntilIdle()
        assertFalse(viewModel.isLoading.value ?: true)
    }

    // --- MusicianDetailViewModel ---

    @Test
    fun `loadMusicianDetail exitoso setea musician`() = runTest {
        val musician = makeMusician(7)
        coEvery { repository.getMusician(7) } returns musician
        val viewModel = MusicianDetailViewModel(repository)
        viewModel.loadMusicianDetail(7)
        advanceUntilIdle()
        assertEquals(musician, viewModel.musician.value)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `loadMusicianDetail error setea mensaje específico`() = runTest {
        coEvery { repository.getMusician(any()) } throws RuntimeException("No encontrado")
        val viewModel = MusicianDetailViewModel(repository)
        viewModel.loadMusicianDetail(99)
        advanceUntilIdle()
        assertEquals("No se pudo cargar el detalle del artista. Verifica tu conexión.", viewModel.error.value)
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `loadMusicianDetail isLoading false tras completar la carga`() = runTest {
        coEvery { repository.getMusician(1) } returns makeMusician(1)
        val viewModel = MusicianDetailViewModel(repository)
        viewModel.loadMusicianDetail(1)
        advanceUntilIdle()
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `loadMusicianDetail limpia error previo en nuevo intento exitoso`() = runTest {
        coEvery { repository.getMusician(1) } throws RuntimeException("error")
        val viewModel = MusicianDetailViewModel(repository)
        viewModel.loadMusicianDetail(1)
        advanceUntilIdle()
        assertNotNull(viewModel.error.value)

        coEvery { repository.getMusician(2) } returns makeMusician(2)
        viewModel.loadMusicianDetail(2)
        advanceUntilIdle()
        assertNull(viewModel.error.value)
        assertNotNull(viewModel.musician.value)
    }
}
