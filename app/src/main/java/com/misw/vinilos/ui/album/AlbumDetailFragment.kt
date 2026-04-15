package com.misw.vinilos.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.misw.vinilos.databinding.FragmentAlbumDetailBinding

class AlbumDetailFragment : Fragment() {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlbumViewModel by viewModels()
    private val args: AlbumDetailFragmentArgs by navArgs() // recibe albumId

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cargar detalle
        viewModel.loadAlbumDetail(args.albumId)

        // Observar LiveData
        viewModel.albumDetail.observe(viewLifecycleOwner) { album ->
            binding.albumTitle.text = album.name
            binding.albumArtist.text = album.performers.joinToString { it.name }
            binding.albumYear.text = album.releaseDate
            binding.albumGenre.text = album.genre
            binding.albumDescription.text = album.description
            // Aquí luego añadimos portada y tracks
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
