package com.misw.vinilos.ui.musician

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.misw.vinilos.R
import com.misw.vinilos.databinding.FragmentMusicianDetailBinding
import com.misw.vinilos.ui.album.AlbumRecentAdapter

class MusicianDetailFragment : Fragment() {

    private var _binding: FragmentMusicianDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MusicianDetailViewModel by viewModels()

    // Ahora el adapter no exige onItemClick
    private val albumAdapter = AlbumRecentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicianDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAlbums.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvAlbums.adapter = albumAdapter

        val musicianId = arguments?.getInt("musicianId") ?: -1
        if (musicianId != -1) {
            viewModel.loadMusicianDetail(musicianId)
        } else {
            binding.tvError.isVisible = true
            binding.tvError.text = "No se recibió un ID válido de artista."
        }

        viewModel.musician.observe(viewLifecycleOwner) { musician ->
            binding.tvName.text = musician.name
            binding.tvDescription.text = musician.description
            binding.tvBirthDate.text = musician.birthDate ?: "Fecha no disponible"

            Glide.with(this)
                .load(musician.image)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .centerCrop()
                .into(binding.ivPhoto)

            albumAdapter.submitList(musician.albums)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.isVisible = loading
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            binding.tvError.isVisible = errorMsg != null
            binding.tvError.text = errorMsg
        }

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
