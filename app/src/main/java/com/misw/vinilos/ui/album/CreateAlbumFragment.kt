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
import com.misw.vinilos.R
import com.misw.vinilos.databinding.FragmentCreateAlbumBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Fragment del formulario de creación de álbum.
 *
 * Responsabilidades del Fragment (solo UI):
 *  - Capturar eventos del usuario y pasarlos al ViewModel.
 *  - Observar [AlbumFormValidation] y mostrar/ocultar errores en TextInputLayout.
 *  - Mostrar el diálogo de éxito y navegar de regreso.
 *
 * Toda la lógica de validación vive en [CreateAlbumViewModel.validateFields].
 * [HU07] Validar campos requeridos del formulario.
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

    // Sellos discográficos válidos según el backend
    private val recordLabels = listOf("Sony Music", "EMI", "Discos Fuentes", "Elektra", "Fania Records")

    // Launcher para seleccionar imagen de la galería
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedCoverUri = it
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
        setupDropdowns()
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

    // ── Dropdowns estáticos: Género y Sello discográfico ─────────────────────

    private fun setupDropdowns() {
        // Género
        val genreAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown_white,
            genres
        )
        binding.actvGenre.setAdapter(genreAdapter)
        binding.actvGenre.setOnItemClickListener { _, _, _, _ ->
            binding.tilGenre.error = null
        }

        // Sello discográfico
        val recordLabelAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown_white,
            recordLabels
        )
        binding.actvRecordLabel.setAdapter(recordLabelAdapter)
        binding.actvRecordLabel.setOnItemClickListener { _, _, _, _ ->
            binding.tilRecordLabel.error = null
        }
    }

    // ── DatePicker ─────────────────────────────────────────────────────────────

    private fun setupDatePicker() {
        val pickDate = {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    // Valor ISO 8601 para el backend — guardado en tag
                    val isoDate = String.format(
                        Locale.US, "%04d-%02d-%02dT00:00:00.000Z",
                        year, month + 1, day
                    )
                    // Texto legible para el usuario
                    val displayFmt = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                    cal.set(year, month, day)
                    binding.etReleaseDate.setText(displayFmt.format(cal.time))
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
    //
    // El Fragment ya no valida. Pasa los datos al ViewModel y este decide si
    // emitir errores de validación o continuar con el POST al API.

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            viewModel.submitAlbum(
                CreateAlbumViewModel.AlbumFormData(
                    name               = binding.etAlbumName.text.toString().trim(),
                    artistName         = binding.actvArtist.text.toString().trim(),
                    musicianId         = selectedMusicianId,
                    coverUrl           = selectedCoverUri?.toString() ?: "",
                    releaseDateIso     = binding.etReleaseDate.tag as? String ?: "",
                    releaseDateDisplay = binding.etReleaseDate.text.toString().trim(),
                    recordLabel        = binding.actvRecordLabel.text.toString().trim(),
                    genre              = binding.actvGenre.text.toString().trim(),
                    description        = binding.etDescription.text.toString().trim()
                )
            )
        }
    }

    // ── Observers ──────────────────────────────────────────────────────────────

    private fun setupObservers() {
        // Músicos para el dropdown de Artista
        viewModel.musicians.observe(viewLifecycleOwner) { musicians ->
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown_white,
                musicians.map { it.name }
            )
            binding.actvArtist.setAdapter(adapter)
            binding.actvArtist.setOnItemClickListener { _, _, position, _ ->
                selectedMusicianId = musicians[position].id
                binding.tilArtist.error = null
            }
        }

        // Estado de validación — muestra / borra errores en cada TextInputLayout
        viewModel.validationState.observe(viewLifecycleOwner) { state ->
            state ?: return@observe
            binding.tilAlbumName.error    = state.nameError
            binding.tilArtist.error       = state.artistError
            binding.tilReleaseDate.error  = state.releaseDateError
            binding.tilRecordLabel.error  = state.recordLabelError
            binding.tilGenre.error        = state.genreError
            binding.tilDescription.error  = state.descriptionError
        }

        // Estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSave.isEnabled = !isLoading
        }

        // Error de red (Toast)
        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (!errorMsg.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
            }
        }

        // Éxito → diálogo y regreso al catálogo
        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                showSuccessDialog()
                viewModel.resetSuccess()
            }
        }
    }

    // ── Diálogo de éxito ───────────────────────────────────────────────────────

    private fun showSuccessDialog() {
        showSuccessDialog(successDialog) { successDialog = it }
    }

    // ── Ciclo de vida ──────────────────────────────────────────────────────────

    override fun onDestroyView() {
        super.onDestroyView()
        successDialog?.dismiss()
        successDialog = null
        _binding = null
    }
}
