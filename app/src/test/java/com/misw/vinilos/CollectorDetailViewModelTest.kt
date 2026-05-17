package com.misw.vinilos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.misw.vinilos.data.model.Album
import com.misw.vinilos.data.model.Collector
import com.misw.vinilos.data.model.CollectorAlbum
import com.misw.vinilos.data.repository.AlbumRepository
import com.misw.vinilos.data.repository.CollectorRepository
import com.misw.vinilos.ui.collector.CollectorDetailViewModel
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
class CollectorDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var collectorRepository: CollectorRepository
    private lateinit var albumRepository: AlbumRepository
    private lateinit var viewModel: CollectorDetailViewModel

    private fun makeAlbum(id: Int = 1) = Album(
        id = id, name = "Álbum $id", cover = "", releaseDate = "2000-01-01",
        description = "Desc", genre = "Rock", recordLabel = "Sony"
    )

    private fun makeCollector(collectorAlbums: List<CollectorAlbum> = emptyList()) = Collector(
        id = 1, name = "Manolo Bellon", email = "manolo@gmail.com",
        collectorAlbums = collectorAlbums
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        collectorRepository = mockk()
        albumRepository = mockk()
        viewModel = CollectorDetailViewModel(collectorRepository, albumRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCollectorDetail exitoso setea collector e isLoading false`() = runTest {
        val collector = makeCollector()
        coEvery { collectorRepository.getCollector(1) } returns collector

        viewModel.getCollectorDetail(1)
        advanceUntilIdle()

        assertEquals(collector, viewModel.collector.value)
        assertFalse(viewModel.isLoading.value ?: true)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `getCollectorDetail sin albums setea albums como lista vacía`() = runTest {
        val collector = makeCollector(collectorAlbums = emptyList())
        coEvery { collectorRepository.getCollector(1) } returns collector

        viewModel.getCollectorDetail(1)
        advanceUntilIdle()

        assertEquals(emptyList<Album>(), viewModel.albums.value)
    }

    @Test
    fun `getCollectorDetail con albums filtra por ids del coleccionista`() = runTest {
        val collectorAlbums = listOf(CollectorAlbum(id = 2, price = 10, status = "Active"))
        val collector = makeCollector(collectorAlbums = collectorAlbums)
        val allAlbums = listOf(makeAlbum(1), makeAlbum(2), makeAlbum(3))
        coEvery { collectorRepository.getCollector(1) } returns collector
        coEvery { albumRepository.getAlbums() } returns allAlbums

        viewModel.getCollectorDetail(1)
        advanceUntilIdle()

        assertEquals(1, viewModel.albums.value?.size)
        assertEquals(2, viewModel.albums.value?.first()?.id)
    }

    @Test
    fun `getCollectorDetail error setea mensaje de error`() = runTest {
        coEvery { collectorRepository.getCollector(any()) } throws RuntimeException("Sin red")

        viewModel.getCollectorDetail(1)
        advanceUntilIdle()

        assertNotNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `getCollectorDetail isLoading es false tras completar`() = runTest {
        coEvery { collectorRepository.getCollector(1) } returns makeCollector()

        viewModel.getCollectorDetail(1)
        advanceUntilIdle()

        assertFalse(viewModel.isLoading.value ?: true)
    }
}
