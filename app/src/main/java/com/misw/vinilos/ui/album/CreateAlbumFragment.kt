package com.misw.vinilos.ui.album

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.misw.vinilos.R
import com.misw.vinilos.databinding.DialogSuccessBinding
import com.misw.vinilos.databinding.FragmentCreateAlbumBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Fragment con el formulario de creación de álbum.
 *
 * Criterios de aceptación:
 *  - Selector de portada con previsualización en la parte superior.
 *  - Campos: nombre, artista, año, disquera, género, descripción (texto blanco).
 *  - DropdownMenu para artista (cargado desde la API) y género (estático).
 *  - Botón "Guardar" visible al fondo.
 *  - Flecha de regreso al catálogo.
 *  - Validación de campos requeridos antes del envío.
 */
class CreateAlbumFragment : Fragment() {

    private var _binding: FragmentCreateAlbumBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateAlbumViewModel by viewModels()

    private var selectedMusicianId: Int = -1
    private var selectedCoverUri: Uri? = null
    private var successDialog: AlertDialog? = null

    // Géneros disponibles según el backend
    private val genres = listOf("Classical", "Salsa", "Rock", "Folk")

    // Launcher para seleccionar imagen de la galería
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedCoverUri = it
                // Mostrar imagen seleccionada y ocultar placeholder
                binding.ivCoverPreview.setImageURI(it)
                binding.ivCoverPreview.visibility = View.VISIBLE
                binding.llCoverPlaceholder.visibility = View.GONE
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupCoverPicker()
        setupGenreDropdown()
        setupDatePicker()
        setupSaveButton()
        setupObservers()
    }

    // ── Toolbar ────────────────────────────────────────────────────────────────

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // ── Selector de portada ────────────────────────────────────────────────────

    private fun setupCoverPicker() {
        binding.flCoverPicker.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    // ── Dropdown de Género (estático) ──────────────────────────────────────────

    private fun setupGenreDropdown() {
        val genreAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown_white,
            genres
        )
        binding.actvGenre.setAdapter(genreAdapter)
        binding.actvGenre.setOnItemClickListener { _, _, _, _ ->
            binding.tilGenre.error = null
        }
    }

    // ── DatePicker para Fecha de lanzamiento ───────────────────────────────────

    private fun setupDatePicker() {
        val pickDate = {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    // Formato ISO 8601 esperado por el backend
                    val isoDate = String.format(
                        Locale.US, "%04d-%02d-%02dT00:00:00.000Z",
                        year, month + 1, day
                    )
                    // Formato legible para el usuario: dd/MM/yyyy
                    val displayFmt = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                    cal.set(year, month, day)
                    binding.etReleaseDate.setText(displayFmt.format(cal.time))
                    // Guardar el valor ISO en el tag para usarlo al guardar
                    binding.etReleaseDate.tag = isoDate
                    binding.tilReleaseDate.error = null
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.etReleaseDate.setOnClickListener { pickDate() }
        binding.tilReleaseDate.setEndIconOnClickListener { pickDate() }
    }

    // ── Botón Guardar ──────────────────────────────────────────────────────────

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            if (validateForm()) {
                val isoDate = binding.etReleaseDate.tag as? String ?: ""
                viewModel.createAlbum(
                    name = binding.etAlbumName.text.toString().trim(),
                    musicianId = selectedMusicianId,
                    releaseDate = isoDate,
                    recordLabel = binding.etRecordLabel.text.toString().trim(),
                    genre = binding.actvGenre.text.toString().trim(),
                    description = binding.etDescription.text.toString().trim()
                )
            }
        }
    }

    // ── Validación ─────────────────────────────────────────────────────────────

    private fun validateForm(): Boolean {
        var isValid = true

        if (binding.etAlbumName.text.isNullOrBlank()) {
            binding.tilAlbumName.error = getString(R.string.error_required_field)
            isValid = false
        } else {
            binding.tilAlbumName.error = null
        }

        if (binding.actvArtist.text.isNullOrBlank()) {
            binding.tilArtist.error = getString(R.string.error_required_field)
            isValid = false
        } else {
            binding.tilArtist.error = null
        }

        if (binding.etReleaseDate.text.isNullOrBlank()) {
            binding.tilReleaseDate.error = getString(R.string.error_required_field)
            isValid = false
        } else {
            binding.tilReleaseDate.error = null
        }

        if (binding.etRecordLabel.text.isNullOrBlank()) {
            binding.tilRecordLabel.error = getString(R.string.error_required_field)
            isValid = false
        } else {
            binding.tilRecordLabel.error = null
        }

        if (binding.actvGenre.text.isNullOrBlank()) {
            binding.tilGenre.error = getString(R.string.error_required_field)
            isValid = false
        } else {
            binding.tilGenre.error = null
        }

        if (binding.etDescription.text.isNullOrBlank()) {
            binding.tilDescription.error = getString(R.string.error_required_field)
            isValid = false
        } else {
            binding.tilDescription.error = null
        }

        return isValid
    }

    // ── Observers ──────────────────────────────────────────────────────────────

    private fun setupObservers() {
        // Poblar el dropdown de Artista con los músicos de la API
        viewModel.musicians.observe(viewLifecycleOwner) { musicians ->
            val artistAdapter = ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown_white,
                musicians.map { it.name }
            )
            binding.actvArtist.setAdapter(artistAdapter)
            binding.actvArtist.setOnItemClickListener { _, _, position, _ ->
                selectedMusicianId = musicians[position].id
                binding.tilArtist.error = null
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSave.isEnabled = !isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (!errorMsg.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                showSuccessDialog()
                viewModel.resetSuccess()
            }
        }
    }

    // ── Diálogo de éxito ───────────────────────────────────────────────────────

    private fun showSuccessDialog() {
        if (successDialog?.isShowing == true) return

        val dialogBinding = DialogSuccessBinding.inflate(layoutInflater)
        successDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialogBinding.btnViewAlbum.setOnClickListener {
            successDialog?.dismiss()
            findNavController().navigateUp()
        }

        successDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        successDialog?.show()
    }

    // ── Ciclo de vida ──────────────────────────────────────────────────────────

    override fun onDestroyView() {
        super.onDestroyView()
        successDialog?.dismiss()
        successDialog = null
        _binding = null
    }
}
