package com.misw.vinilos.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.misw.vinilos.R
import com.misw.vinilos.databinding.FragmentAlbumListBinding

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlbumViewModel by viewModels()

    // Adaptadores reciben un callback de click
    private val trendingAdapter = AlbumTrendingAdapter { album ->
        val action = AlbumFragmentDirections
            .actionAlbumFragmentToAlbumDetailFragment(album.id)
        findNavController().navigate(action)
    }

    private val recentAdapter = AlbumRecentAdapter { album ->
        val action = AlbumFragmentDirections
            .actionAlbumFragmentToAlbumDetailFragment(album.id)
        findNavController().navigate(action)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Carrusel horizontal — álbumes en tendencia
        binding.rvTrending.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTrending.adapter = trendingAdapter

        // Grid 2 columnas — agregados recientemente
        binding.rvRecent.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvRecent.adapter = recentAdapter

        val profileName = arguments?.getString("profileName") ?: getString(R.string.visitor_title)
        binding.tvProfileTitle.text = profileName

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.albums.observe(viewLifecycleOwner) { albums ->
            val sorted = albums.sortedByDescending { it.releaseDate }
            trendingAdapter.submitList(sorted.take(5))
            recentAdapter.submitList(sorted.drop(5))
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.isVisible = loading
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            binding.tvError.isVisible = errorMsg != null
            binding.tvError.text = errorMsg
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
