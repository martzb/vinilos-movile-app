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
import com.google.android.material.chip.Chip
import com.misw.vinilos.databinding.FragmentCollectorDetailBinding

class CollectorDetailFragment : Fragment() {

    private var _binding: FragmentCollectorDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CollectorDetailViewModel by viewModels()

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

        val collectorId = arguments?.getInt("collectorId") ?: -1
        if (collectorId != -1) {
            viewModel.getCollectorDetail(collectorId)
        } else {
            binding.tvError.isVisible = true
            binding.tvError.text = getString(com.misw.vinilos.R.string.collector_invalid_id)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.collector.observe(viewLifecycleOwner) { collector ->
            binding.tvCollectorName.text = collector.name
            binding.tvCollectorEmail.text = collector.email
            binding.tvCollectorTelephone.text = collector.telephone ?: getString(com.misw.vinilos.R.string.collector_no_telephone)

            val avatarUrl = "https://i.pravatar.cc/150?u=${collector.id}"
            Glide.with(this)
                .load(avatarUrl)
                .placeholder(com.misw.vinilos.R.drawable.ic_person)
                .error(com.misw.vinilos.R.drawable.ic_person)
                .transform(CircleCrop())
                .into(binding.ivCollectorAvatar)

            binding.cgGenres.removeAllViews()
            collector.favoritePerformers.forEach { performer ->
                val chip = Chip(requireContext()).apply {
                    text = performer.name
                    isClickable = false
                    isCheckable = false
                }
                binding.cgGenres.addView(chip)
            }
            
            if (collector.favoritePerformers.isEmpty()) {
                val chip = Chip(requireContext()).apply {
                    text = getString(com.misw.vinilos.R.string.collector_no_tastes)
                    isClickable = false
                    isCheckable = false
                }
                binding.cgGenres.addView(chip)
            }
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
