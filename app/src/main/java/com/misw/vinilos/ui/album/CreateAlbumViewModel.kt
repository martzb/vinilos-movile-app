package com.misw.vinilos.ui.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.vinilos.data.model.AlbumRequest
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
class CreateAlbumViewModel(
    private val albumRepository: AlbumRepository = AlbumRepository(),
    private val musicianRepository: MusicianRepository = MusicianRepository()
) : ViewModel() {

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
    data class AlbumFormData(
        val name: String,
        val artistName: String,
        val musicianId: Int,
        val coverUrl: String,
        val releaseDateIso: String,
        val releaseDateDisplay: String,
        val recordLabel: String,
        val genre: String,
        val description: String
    )

    fun submitAlbum(formData: AlbumFormData) {
        val validation = validateFields(
            name               = formData.name,
            artistName         = formData.artistName,
            releaseDateIso     = formData.releaseDateIso,
            releaseDateDisplay = formData.releaseDateDisplay,
            recordLabel        = formData.recordLabel,
            genre              = formData.genre,
            description        = formData.description
        )

        _validationState.value = validation

        if (validation.isValid) {
            createAlbum(
                name        = formData.name,
                coverUrl    = formData.coverUrl,
                releaseDate = formData.releaseDateIso,
                recordLabel = formData.recordLabel,
                genre       = formData.genre,
                description = formData.description
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
        coverUrl: String,
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
                // El backend requiere una URL válida en el campo cover.
                // Si el usuario no seleccionó imagen usamos un placeholder.
                val effectiveCover = coverUrl.ifBlank { PLACEHOLDER_COVER }
                val request = AlbumRequest(
                    name        = name,
                    cover       = effectiveCover,
                    releaseDate = releaseDate,
                    description = description,
                    genre       = genre,
                    recordLabel = recordLabel
                )
                albumRepository.createAlbum(request)
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
        internal const val ERROR_REQUIRED    = "Campo requerido"
        internal const val ERROR_INVALID_DATE = "Selecciona una fecha válida"
        internal const val PLACEHOLDER_COVER =
            "https://via.placeholder.com/300x300.png?text=Sin+portada"
    }
}
