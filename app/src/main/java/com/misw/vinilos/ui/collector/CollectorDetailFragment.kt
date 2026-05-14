package com.misw.vinilos.ui.collector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.misw.vinilos.R
import com.misw.vinilos.databinding.FragmentCollectorDetailBinding
import com.misw.vinilos.ui.album.AlbumCarouselAdapter

class CollectorDetailFragment : Fragment() {

    private var _binding: FragmentCollectorDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CollectorDetailViewModel by viewModels()
    private val albumAdapter = AlbumCarouselAdapter { album ->
        val bundle = Bundle().apply { putInt("albumId", album.id) }
        findNavController().navigate(R.id.action_collectorDetailFragment_to_albumDetailFragment, bundle)
    }
    private val performerAdapter = PerformerCarouselAdapter { performer ->
        val bundle = Bundle().apply { putInt("musicianId", performer.id) }
        findNavController().navigate(R.id.action_collectorDetailFragment_to_musicianDetailFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAlbums.adapter = albumAdapter
        binding.rvPerformers.adapter = performerAdapter

        val collectorId = arguments?.getInt("collectorId") ?: -1
        if (collectorId != -1) {
            viewModel.getCollectorDetail(collectorId)
        } else {
            binding.tvError.isVisible = true
            binding.tvError.text = getString(R.string.collector_invalid_id)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.collector.observe(viewLifecycleOwner) { collector ->
            binding.tvCollectorName.text = collector.name
            
            val email = collector.email
            val phone = collector.telephone ?: getString(R.string.collector_no_telephone)
            binding.tvCollectorContact.text = "$email • $phone"
            
            binding.tvAlbumsCount.text = collector.collectorAlbums.size.toString()

            val avatarUrl = "https://i.pravatar.cc/150?u=${collector.id}"
            Glide.with(this)
                .load(avatarUrl)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .transform(CircleCrop())
                .into(binding.ivCollectorAvatar)

            performerAdapter.submitList(collector.favoritePerformers)
        }

        viewModel.albums.observe(viewLifecycleOwner) { albums ->
            albumAdapter.submitList(albums)
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
