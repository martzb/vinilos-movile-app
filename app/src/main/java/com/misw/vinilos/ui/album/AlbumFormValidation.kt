package com.misw.vinilos.ui.album

/**
 * Estado de validación del formulario de creación de álbum.
 *
 * Cada campo tiene un error nullable:
 *  - null  → el campo es válido
 *  - String → mensaje de error a mostrar en el TextInputLayout
 *
 * [HU07] Validar campos requeridos del formulario.
 */
data class AlbumFormValidation(
    val nameError: String? = null,
    val artistError: String? = null,
    val releaseDateError: String? = null,
    val recordLabelError: String? = null,
    val genreError: String? = null,
    val descriptionError: String? = null
) {
    /** true si todos los campos son válidos (ningún error). */
    val isValid: Boolean
        get() = listOf(
            nameError,
            artistError,
            releaseDateError,
            recordLabelError,
            genreError,
            descriptionError
        ).all { it == null }
}
