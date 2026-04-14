package com.misw.vinilos.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.misw.vinilos.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardVisitor.setOnClickListener {
            // TODO: navegar al catálogo como visitante
        }

        binding.cardCollector.setOnClickListener {
            // TODO: navegar al catálogo como coleccionista
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
