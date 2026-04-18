package com.misw.vinilos.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.misw.vinilos.R
import com.misw.vinilos.databinding.FragmentAlbumDetailBinding

class AlbumDetailFragment : Fragment() {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlbumViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recibir argumento manualmente desde el Bundle
        val albumId = arguments?.getInt("albumId") ?: -1

        // Cargar detalle
        viewModel.loadAlbumDetail(albumId)

        val trackAdapter = TrackAdapter()
        binding.tracksRecyclerView.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = trackAdapter
        }

        // Observar LiveData
        viewModel.albumDetail.observe(viewLifecycleOwner) { album ->
            binding.albumTitle.text = album.name
            
            val artistName = album.performers.joinToString { it.name }
            binding.albumArtist.text = artistName
            
            val year = album.releaseDate.takeIf { it.length >= 4 }?.substring(0, 4) ?: album.releaseDate
            binding.albumYear.text = "$year • ${album.recordLabel}"
            
            binding.albumGenre.visibility = View.GONE
            binding.albumDescription.text = album.description

            trackAdapter.albumArtist = artistName
            trackAdapter.submitList(album.tracks)

            // Cargar portada con Glide (igual que en AlbumRecentAdapter)
            Glide.with(binding.albumCover.context)
                .load(album.cover)
                .placeholder(R.drawable.ic_vinyl_record)
                .error(R.drawable.ic_vinyl_record)
                .centerCrop()
                .into(binding.albumCover)
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
