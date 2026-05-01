package com.misw.vinilos.ui.musician

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.misw.vinilos.R
import com.misw.vinilos.databinding.FragmentMusicianListBinding

class MusicianFragment : Fragment() {

    private var _binding: FragmentMusicianListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MusicianViewModel by viewModels()
    private lateinit var adapter: MusicianAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicianListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MusicianAdapter { musician ->
            val bundle = Bundle().apply {
                putInt("musicianId", musician.id)
            }
            findNavController().navigate(R.id.musicianDetailFragment, bundle)
        }

        binding.rvMusicians.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMusicians.adapter = adapter

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.musicians.observe(viewLifecycleOwner) { musicians ->
            adapter.submitList(musicians)
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
