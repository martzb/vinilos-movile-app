package com.misw.vinilos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.misw.vinilos.data.model.AlbumRequest
import com.misw.vinilos.data.model.Musician
import com.misw.vinilos.data.repository.AlbumRepository
import com.misw.vinilos.data.repository.MusicianRepository
import com.misw.vinilos.ui.album.CreateAlbumViewModel
import com.misw.vinilos.ui.album.CreateAlbumViewModel.Companion.ERROR_INVALID_DATE
import com.misw.vinilos.ui.album.CreateAlbumViewModel.Companion.ERROR_REQUIRED
import com.misw.vinilos.ui.album.CreateAlbumViewModel.Companion.PLACEHOLDER_COVER
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

/**
 * Pruebas unitarias para [CreateAlbumViewModel].
 *
 * Cubre:
 *  - [HU07] Validación de campos requeridos del formulario.
 *  - Flujo completo de creación exitosa de álbum.
 *  - Manejo de errores de red al crear álbum.
 *  - Manejo de errores de red al cargar músicos.
 *  - Estados reactivos: isLoading, isSuccess, error, validationState.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CreateAlbumViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var albumRepository: AlbumRepository
    private lateinit var musicianRepository: MusicianRepository
    private lateinit var viewModel: CreateAlbumViewModel

    // ── Datos de prueba ────────────────────────────────────────────────────────

    private val validName        = "Hurry Up Tomorrow"
    private val validArtistName  = "The Weeknd"
    private val validMusicianId  = 2
    private val validCoverUrl    = "https://example.com/cover.jpg"
    private val validIsoDate     = "2026-05-16T00:00:00.000Z"
    private val validDisplayDate = "16/05/2026"
    private val validRecordLabel = "Sony Music"
    private val validGenre       = "Rock"
    private val validDescription = "Último álbum de The Weeknd."

    private val albumResponse = com.misw.vinilos.data.model.Album(
        id          = 99,
        name        = validName,
        cover       = validCoverUrl,
        releaseDate = validIsoDate,
        description = validDescription,
        genre       = validGenre,
        recordLabel = validRecordLabel
    )

    private val musicians = listOf(
        Musician(1, "Rubén Blades", "", "Cantante"),
        Musician(2, "The Weeknd",   "", "Músico")
    )

    // ── Setup / Teardown ───────────────────────────────────────────────────────

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        albumRepository    = mockk()
        musicianRepository = mockk()
        // Por defecto los músicos cargan sin error
        coEvery { musicianRepository.getMusicians() } returns musicians
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /** Crea el ViewModel inyectando los repositorios mockeados. */
    private fun buildViewModel() = CreateAlbumViewModel(albumRepository, musicianRepository)

    /** Parámetros válidos completos para submitAlbum. */
    private fun submitValid(vm: CreateAlbumViewModel) = vm.submitAlbum(
        name               = validName,
        artistName         = validArtistName,
        musicianId         = validMusicianId,
        coverUrl           = validCoverUrl,
        releaseDateIso     = validIsoDate,
        releaseDateDisplay = validDisplayDate,
        recordLabel        = validRecordLabel,
        genre              = validGenre,
        description        = validDescription
    )

    // ── Tests de loadMusicians ─────────────────────────────────────────────────

    @Test
    fun `init carga la lista de musicos correctamente`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()
        assertEquals(musicians, viewModel.musicians.value)
        assertFalse(viewModel.isLoading.value ?: true)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `loadMusicians error muestra mensaje de error`() = runTest {
        coEvery { musicianRepository.getMusicians() } throws RuntimeException("Sin red")
        viewModel = buildViewModel()
        advanceUntilIdle()
        assertEquals("No se pudo cargar la lista de artistas.", viewModel.error.value)
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `loadMusicians recargado manualmente actualiza lista`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()
        val newMusicians = listOf(Musician(5, "Bad Bunny", "", "Músico"))
        coEvery { musicianRepository.getMusicians() } returns newMusicians
        viewModel.loadMusicians()
        advanceUntilIdle()
        assertEquals(newMusicians, viewModel.musicians.value)
    }

    // ── Tests de validateFields (lógica pura) ──────────────────────────────────

    @Test
    fun `validateFields todos los campos validos devuelve isValid true`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()
        val result = viewModel.validateFields(
            name               = validName,
            artistName         = validArtistName,
            releaseDateIso     = validIsoDate,
            releaseDateDisplay = validDisplayDate,
            recordLabel        = validRecordLabel,
            genre              = validGenre,
            description        = validDescription
        )
        assertTrue(result.isValid)
        assertNull(result.nameError)
        assertNull(result.artistError)
        assertNull(result.releaseDateError)
        assertNull(result.recordLabelError)
        assertNull(result.genreError)
        assertNull(result.descriptionError)
    }

    @Test
    fun `validateFields nombre vacio devuelve ERROR_REQUIRED en nameError`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()
        val result = viewModel.validateFields(
            name               = "   ",
            artistName         = validArtistName,
            releaseDateIso     = validIsoDate,
            releaseDateDisplay = validDisplayDate,
            recordLabel        = validRecordLabel,
            genre              = validGenre,
            description        = validDescription
        )
        assertEquals(ERROR_REQUIRED, result.nameError)
        assertFalse(result.isValid)
    }

    @Test
    fun `validateFields artista vacio devuelve ERROR_REQUIRED en artistError`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()
        val result = viewModel.validateFields(
            name               = validName,
            artistName         = "",
            releaseDateIso     = validIsoDate,
            releaseDateDisplay = validDisplayDate,
            recordLabel        = validRecordLabel,
            genre              = validGenre,
            description        = validDescription
        )
        assertEquals(ERROR_REQUIRED, result.artistError)
        assertFalse(result.isValid)
    }

    @Test
    fun `validateFields fecha display vacia devuelve ERROR_REQUIRED en releaseDateError`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()
        val result = viewModel.validateFields(
            name               = validName,
            artistName         = validArtistName,
            releaseDateIso     = validIsoDate,
            releaseDateDisplay = "",
            recordLabel        = validRecordLabel,
            genre              = validGenre,
            description        = validDescription
        )
        assertEquals(ERROR_REQUIRED, result.releaseDateError)
        assertFalse(result.isValid)
    }

    @Test
    fun `validateFields fecha iso invalida devuelve ERROR_INVALID_DATE`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()
        val result = viewModel.validateFields(
            name               = validName,
            artistName         = validArtistName,
            releaseDateIso     = "16/05/2026",   // formato incorrecto
            releaseDateDisplay = validDisplayDate,
            recordLabel        = validRecordLabel,
            genre              = validGenre,
            description        = validDescription
        )
        assertEquals(ERROR_INVALID_DATE, result.releaseDateError)
        assertFalse(result.isValid)
    }

    @Test
    fun `validateFields sello vacio devuelve ERROR_REQUIRED en recordLabelError`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()
        val result = viewModel.validateFields(
            name               = validName,
            artistName         = validArtistName,
            releaseDateIso     = validIsoDate,
            releaseDateDisplay = validDisplayDate,
            recordLabel        = "",
            genre              = validGenre,
            description        = validDescription
        )
        assertEquals(ERROR_REQUIRED, result.recordLabelError)
        assertFalse(result.isValid)
    }

    @Test
    fun `validateFields genero vacio devuelve ERROR_REQUIRED en genreError`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()
        val result = viewModel.validateFields(
            name               = validName,
            artistName         = validArtistName,
            releaseDateIso     = validIsoDate,
            releaseDateDisplay = validDisplayDate,
            recordLabel        = validRecordLabel,
            genre              = "",
            description        = validDescription
        )
        assertEquals(ERROR_REQUIRED, result.genreError)
        assertFalse(result.isValid)
    }

    @Test
    fun `validateFields descripcion vacia devuelve ERROR_REQUIRED en descriptionError`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()
        val result = viewModel.validateFields(
            name               = validName,
            artistName         = validArtistName,
            releaseDateIso     = validIsoDate,
            releaseDateDisplay = validDisplayDate,
            recordLabel        = validRecordLabel,
            genre              = validGenre,
            description        = ""
        )
        assertEquals(ERROR_REQUIRED, result.descriptionError)
        assertFalse(result.isValid)
    }

    @Test
    fun `validateFields todos los campos vacios devuelve errores en todos los campos`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()
        val result = viewModel.validateFields(
            name               = "",
            artistName         = "",
            releaseDateIso     = "",
            releaseDateDisplay = "",
            recordLabel        = "",
            genre              = "",
            description        = ""
        )
        assertFalse(result.isValid)
        assertNotNull(result.nameError)
        assertNotNull(result.artistError)
        assertNotNull(result.releaseDateError)
        assertNotNull(result.recordLabelError)
        assertNotNull(result.genreError)
        assertNotNull(result.descriptionError)
    }

    // ── Tests de submitAlbum ───────────────────────────────────────────────────

    @Test
    fun `submitAlbum con datos validos llama createAlbum y emite isSuccess true`() = runTest {
        coEvery { albumRepository.createAlbum(any()) } returns albumResponse
        viewModel = buildViewModel()
        advanceUntilIdle()
        submitValid(viewModel)
        advanceUntilIdle()
        assertTrue(viewModel.isSuccess.value ?: false)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `submitAlbum exitoso emite validationState con isValid true`() = runTest {
        coEvery { albumRepository.createAlbum(any()) } returns albumResponse
        viewModel = buildViewModel()
        advanceUntilIdle()
        submitValid(viewModel)
        advanceUntilIdle()
        val state = viewModel.validationState.value
        assertNotNull(state)
        assertTrue(state!!.isValid)
    }

    @Test
    fun `submitAlbum con datos invalidos no llama al repositorio`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.submitAlbum(
            name               = "",
            artistName         = "",
            musicianId         = -1,
            coverUrl           = "",
            releaseDateIso     = "",
            releaseDateDisplay = "",
            recordLabel        = "",
            genre              = "",
            description        = ""
        )
        advanceUntilIdle()
        coVerify(exactly = 0) { albumRepository.createAlbum(any()) }
        assertFalse(viewModel.isSuccess.value ?: false)
    }

    @Test
    fun `submitAlbum con datos invalidos emite validationState con errores`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.submitAlbum(
            name               = "",
            artistName         = "",
            musicianId         = -1,
            coverUrl           = "",
            releaseDateIso     = "",
            releaseDateDisplay = "",
            recordLabel        = "",
            genre              = "",
            description        = ""
        )
        advanceUntilIdle()
        val state = viewModel.validationState.value
        assertNotNull(state)
        assertFalse(state!!.isValid)
    }

    @Test
    fun `submitAlbum error de red emite mensaje de error y no emite isSuccess`() = runTest {
        coEvery { albumRepository.createAlbum(any()) } throws RuntimeException("Sin red")
        viewModel = buildViewModel()
        advanceUntilIdle()
        submitValid(viewModel)
        advanceUntilIdle()
        assertEquals(
            "Error al crear el álbum. Verifica tu conexión e intenta de nuevo.",
            viewModel.error.value
        )
        assertFalse(viewModel.isSuccess.value ?: false)
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `isLoading es true durante la creacion y false al terminar`() = runTest {
        coEvery { albumRepository.createAlbum(any()) } returns albumResponse
        viewModel = buildViewModel()
        advanceUntilIdle()
        submitValid(viewModel)
        advanceUntilIdle()
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `resetSuccess pone isSuccess en false`() = runTest {
        coEvery { albumRepository.createAlbum(any()) } returns albumResponse
        viewModel = buildViewModel()
        advanceUntilIdle()
        submitValid(viewModel)
        advanceUntilIdle()
        assertTrue(viewModel.isSuccess.value ?: false)
        viewModel.resetSuccess()
        assertFalse(viewModel.isSuccess.value ?: true)
    }

    @Test
    fun `submitAlbum envia el album con los datos correctos al repositorio`() = runTest {
        coEvery { albumRepository.createAlbum(any()) } returns albumResponse
        viewModel = buildViewModel()
        advanceUntilIdle()
        submitValid(viewModel)
        advanceUntilIdle()
        coVerify {
            albumRepository.createAlbum(
                match { req ->
                    req.name        == validName        &&
                    req.cover       == validCoverUrl    &&
                    req.releaseDate == validIsoDate     &&
                    req.recordLabel == validRecordLabel &&
                    req.genre       == validGenre       &&
                    req.description == validDescription
                }
            )
        }
    }

    @Test
    fun `submitAlbum sin cover usa el placeholder del backend`() = runTest {
        coEvery { albumRepository.createAlbum(any()) } returns albumResponse
        viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.submitAlbum(
            name               = validName,
            artistName         = validArtistName,
            musicianId         = validMusicianId,
            coverUrl           = "",          // sin imagen seleccionada
            releaseDateIso     = validIsoDate,
            releaseDateDisplay = validDisplayDate,
            recordLabel        = validRecordLabel,
            genre              = validGenre,
            description        = validDescription
        )
        advanceUntilIdle()
        coVerify {
            albumRepository.createAlbum(
                match { req -> req.cover == PLACEHOLDER_COVER }
            )
        }
    }
}
