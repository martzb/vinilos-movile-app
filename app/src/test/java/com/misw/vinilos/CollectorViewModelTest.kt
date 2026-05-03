package com.misw.vinilos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.misw.vinilos.data.model.Collector
import com.misw.vinilos.data.repository.CollectorRepository
import com.misw.vinilos.ui.collector.CollectorViewModel
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
class CollectorViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: CollectorRepository

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
    fun `loadCollectors exitoso setea collectors y limpia error`() = runTest {
        val collectors = listOf(
            Collector(1, "Manolo Bellon", "manolo@gmail.com"),
            Collector(2, "Jaime Zuluaga", "jaime@gmail.com")
        )
        coEvery { repository.getCollectors() } returns collectors
        val viewModel = CollectorViewModel(repository)
        advanceUntilIdle()
        assertEquals(collectors, viewModel.collectors.value)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `loadCollectors error setea mensaje y isLoading false`() = runTest {
        coEvery { repository.getCollectors() } throws RuntimeException("Sin red")
        val viewModel = CollectorViewModel(repository)
        advanceUntilIdle()
        assertEquals(
            "No se pudo cargar la lista de coleccionistas. Verifica tu conexión.",
            viewModel.error.value
        )
        assertFalse(viewModel.isLoading.value ?: true)
        assertNull(viewModel.collectors.value)
    }

    @Test
    fun `loadCollectors llamado manualmente recarga`() = runTest {
        val collectors = listOf(Collector(1, "Manolo Bellon", "manolo@gmail.com"))
        coEvery { repository.getCollectors() } returns collectors
        val viewModel = CollectorViewModel(repository)
        advanceUntilIdle()
        viewModel.loadCollectors()
        advanceUntilIdle()
        assertEquals(collectors, viewModel.collectors.value)
    }

    @Test
    fun `isLoading es false tras completar la carga`() = runTest {
        coEvery { repository.getCollectors() } returns emptyList()
        val viewModel = CollectorViewModel(repository)
        advanceUntilIdle()
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `lista vacía se setea correctamente`() = runTest {
        coEvery { repository.getCollectors() } returns emptyList()
        val viewModel = CollectorViewModel(repository)
        advanceUntilIdle()
        assertEquals(emptyList<Collector>(), viewModel.collectors.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `error previo se limpia en carga exitosa`() = runTest {
        coEvery { repository.getCollectors() } throws RuntimeException("error")
        val viewModel = CollectorViewModel(repository)
        advanceUntilIdle()
        assertNotNull(viewModel.error.value)

        coEvery { repository.getCollectors() } returns listOf(Collector(1, "Test", "t@t.com"))
        viewModel.loadCollectors()
        advanceUntilIdle()
        assertNull(viewModel.error.value)
    }
}
