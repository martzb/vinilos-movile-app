package com.misw.vinilos.ui.album

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.misw.vinilos.R
import com.misw.vinilos.databinding.FragmentCreateTrackBinding

class CreateTrackFragment : Fragment() {

    private var _binding: FragmentCreateTrackBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateTrackViewModel by viewModels()
    private lateinit var adapter: AddedTrackAdapter
    private var albumId: Int = -1

    private var successDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumId = arguments?.getInt("albumId") ?: -1

        setupUI()
        setupObservers()

        if (albumId != -1) {
            viewModel.fetchAlbum(albumId)
        }
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        adapter = AddedTrackAdapter()
        binding.rvAddedTracks.layoutManager = LinearLayoutManager(context)
        binding.rvAddedTracks.adapter = adapter

        binding.etTrackDuration.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita acción antes del cambio
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No se necesita acción durante el cambio
            }

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting || s == null) return
                isFormatting = true
                val digits = s.toString().filter { it.isDigit() }
                val formatted = if (digits.length >= 3) {
                    "${digits.substring(0, 2)}:${digits.substring(2).take(2)}"
                } else {
                    digits
                }
                s.replace(0, s.length, formatted)
                isFormatting = false
            }
        })

        binding.btnSubmit.setOnClickListener {
            val name = binding.etTrackName.text.toString().trim()
            val duration = binding.etTrackDuration.text.toString().trim()

            if (name.isEmpty()) {
                binding.etTrackName.error = "Campo requerido"
                return@setOnClickListener
            }
            if (duration.isEmpty() || !duration.matches(Regex("\\d{2}:\\d{2}"))) {
                binding.etTrackDuration.error = "Formato inválido (mm:ss)"
                return@setOnClickListener
            }

            viewModel.createTrack(albumId, name, duration)
        }
    }

    private fun setupObservers() {
        viewModel.album.observe(viewLifecycleOwner) { album ->
            binding.tvAlbumName.text = album.name
            binding.tvAlbumArtist.text = album.performers.joinToString { it.name }
            
            Glide.with(this)
                .load(album.cover)
                .placeholder(R.drawable.ic_vinyl_record)
                .error(R.drawable.ic_vinyl_record)
                .centerCrop()
                .into(binding.ivAlbumCover)
        }

        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            binding.tvTracksCount.text = "${tracks.size} tracks"
            adapter.submitList(tracks)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSubmit.isEnabled = !isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (!errorMsg.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                showSuccessDialog()
                binding.etTrackName.text?.clear()
                binding.etTrackDuration.text?.clear()
                viewModel.resetSuccess()
            }
        }
    }

    private fun showSuccessDialog() {
        showSuccessDialog(successDialog) { successDialog = it }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        successDialog?.dismiss()
        successDialog = null
    }
}
