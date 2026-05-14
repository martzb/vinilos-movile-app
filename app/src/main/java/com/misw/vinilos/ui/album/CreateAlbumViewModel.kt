package com.misw.vinilos.ui.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.vinilos.data.model.Album
import com.misw.vinilos.data.model.Musician
import com.misw.vinilos.data.repository.AlbumRepository
import com.misw.vinilos.data.repository.MusicianRepository
import kotlinx.coroutines.launch

/**
 * ViewModel para el formulario de creación de álbum.
 *
 * Responsabilidades:
 *  - Cargar la lista de músicos para el DropdownMenu de Artista.
 *  - Validar los campos del formulario antes del envío ([HU07]).
 *  - Enviar el nuevo álbum al repositorio (POST /albums).
 *  - Exponer estados de validación, carga, éxito y error.
 */
class CreateAlbumViewModel : ViewModel() {

    private val albumRepository = AlbumRepository()
    private val musicianRepository = MusicianRepository()

    // Lista de músicos para el dropdown de Artista
    private val _musicians = MutableLiveData<List<Musician>>(emptyList())
    val musicians: LiveData<List<Musician>> get() = _musicians

    // Estado de carga
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Error de red/servidor (Toast)
    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> get() = _error

    // Álbum creado exitosamente
    private val _isSuccess = MutableLiveData<Boolean>(false)
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    /**
     * Estado de validación campo a campo.
     * El Fragment observa este LiveData para mostrar/ocultar
     * los mensajes de error en cada TextInputLayout.
     *
     * [HU07] Validación en ViewModel.
     */
    private val _validationState = MutableLiveData<AlbumFormValidation?>(null)
    val validationState: LiveData<AlbumFormValidation?> get() = _validationState

    // Expresión regular para validar el formato ISO 8601 generado por DatePicker
    private val ISO_DATE_REGEX =
        Regex("""^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}\.\d{3}Z$""")

    init {
        loadMusicians()
    }

    // ── Cargar músicos ─────────────────────────────────────────────────────────

    /**
     * Carga la lista de músicos desde el endpoint GET /musicians.
     */
    fun loadMusicians() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _musicians.value = musicianRepository.getMusicians()
            } catch (e: Exception) {
                _error.value = "No se pudo cargar la lista de artistas."
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ── Punto de entrada principal desde el Fragment ────────────────────────────

    /**
     * Valida el formulario y, si es válido, crea el álbum.
     *
     * El Fragment llama a este método al pulsar "Guardar".
     * No necesita hacer ninguna validación propia.
     *
     * @param name          Nombre del álbum
     * @param artistName    Nombre del artista seleccionado (para validar si está vacío)
     * @param musicianId    ID del músico seleccionado (-1 si no se eligió)
     * @param releaseDateIso Fecha ISO 8601 (guardada en el tag del EditText)
     * @param releaseDateDisplay Texto visible en el campo (dd/MM/yyyy)
     * @param recordLabel   Sello discográfico
     * @param genre         Género musical
     * @param description   Descripción del álbum
     */
    fun submitAlbum(
        name: String,
        artistName: String,
        musicianId: Int,
        releaseDateIso: String,
        releaseDateDisplay: String,
        recordLabel: String,
        genre: String,
        description: String
    ) {
        val validation = validateFields(
            name = name,
            artistName = artistName,
            releaseDateIso = releaseDateIso,
            releaseDateDisplay = releaseDateDisplay,
            recordLabel = recordLabel,
            genre = genre,
            description = description
        )

        // Emitir siempre el estado de validación para que el Fragment actualice la UI
        _validationState.value = validation

        if (validation.isValid) {
            createAlbum(
                name = name,
                musicianId = musicianId,
                releaseDate = releaseDateIso,
                recordLabel = recordLabel,
                genre = genre,
                description = description
            )
        }
    }

    // ── Validación (lógica pura, testeable) ────────────────────────────────────

    /**
     * Evalúa cada campo y devuelve un [AlbumFormValidation] con los errores.
     *
     * Reglas:
     *  - nombre: obligatorio (no vacío)
     *  - artista: obligatorio (debe haber seleccionado uno del dropdown)
     *  - fecha: obligatoria + formato ISO 8601 válido generado por DatePicker
     *  - sello discográfico: obligatorio
     *  - género: obligatorio (debe haber seleccionado uno del dropdown)
     *  - descripción: obligatoria
     */
    internal fun validateFields(
        name: String,
        artistName: String,
        releaseDateIso: String,
        releaseDateDisplay: String,
        recordLabel: String,
        genre: String,
        description: String
    ): AlbumFormValidation {

        val nameError = when {
            name.isBlank() -> ERROR_REQUIRED
            else -> null
        }

        val artistError = when {
            artistName.isBlank() -> ERROR_REQUIRED
            else -> null
        }

        val releaseDateError = when {
            releaseDateDisplay.isBlank() -> ERROR_REQUIRED
            !ISO_DATE_REGEX.matches(releaseDateIso) -> ERROR_INVALID_DATE
            else -> null
        }

        val recordLabelError = when {
            recordLabel.isBlank() -> ERROR_REQUIRED
            else -> null
        }

        val genreError = when {
            genre.isBlank() -> ERROR_REQUIRED
            else -> null
        }

        val descriptionError = when {
            description.isBlank() -> ERROR_REQUIRED
            else -> null
        }

        return AlbumFormValidation(
            nameError = nameError,
            artistError = artistError,
            releaseDateError = releaseDateError,
            recordLabelError = recordLabelError,
            genreError = genreError,
            descriptionError = descriptionError
        )
    }

    // ── Envío al API ───────────────────────────────────────────────────────────

    private fun createAlbum(
        name: String,
        musicianId: Int,
        releaseDate: String,
        recordLabel: String,
        genre: String,
        description: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _isSuccess.value = false
            try {
                val album = Album(
                    id = 0,
                    name = name,
                    cover = "",
                    releaseDate = releaseDate,
                    description = description,
                    genre = genre,
                    recordLabel = recordLabel
                )
                albumRepository.createAlbum(album)
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = "Error al crear el álbum. Verifica tu conexión e intenta de nuevo."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetSuccess() {
        _isSuccess.value = false
    }

    companion object {
        internal const val ERROR_REQUIRED = "Campo requerido"
        internal const val ERROR_INVALID_DATE = "Selecciona una fecha válida"
    }
}
